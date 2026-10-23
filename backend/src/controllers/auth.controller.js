const crypto = require('crypto');
const { validationResult, body } = require('express-validator');
const User = require('../models/User');
const RefreshToken = require('../models/RefreshToken');
const env = require('../config/env');
const { sequelize } = require('../config/database');
const StudentUser = require('../models/StudentUser');
const LandlordUser = require('../models/LandlordUser');

/**
 * Đăng ký tài khoản
 * POST /api/auth/register
 */
const register = async (req, res, next) => {
  const transaction = await sequelize.transaction();
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      await transaction.rollback();
      return res.status(400).json({
        success: false,
        message: 'Dữ liệu không hợp lệ',
        errors: errors.array().map((e) => e.msg),
      });
    }

    const { email, password, full_name, phone_number, role } = req.body;

    const userRole = role || 'STUDENT';

    // Kiểm tra email trùng
    const existingEmail = await User.findOne({ where: { email } });
    if (existingEmail) {
      await transaction.rollback();
      return res.status(409).json({
        success: false,
        message: 'Email này đã được sử dụng trong hệ thống',
      });
    }

    // Kiểm tra số điện thoại trùng (nếu có cung cấp)
    if (phone_number) {
      const existingPhone = await User.findOne({ where: { phone_number } });
      if (existingPhone) {
        await transaction.rollback();
        return res.status(409).json({
          success: false,
          message: 'Số điện thoại này đã được sử dụng trong hệ thống',
        });
      }
    }

    // Tạo user mới
    const user = await User.create({
      email,
      password_hash: password,
      full_name,
      phone_number: phone_number || null,
      role: userRole,
      status: 'ACTIVE',
    }, { transaction });

    if (userRole === 'STUDENT') {
      await StudentUser.create({
        user_id: user.id,
      }, { transaction });
    } 
    else if (userRole === 'LANDLORD') {
      await LandlordUser.create({
        user_id: user.id,
        phone_number: phone_number || null,
      }, { transaction });
    }

    // Tạo Access Token & Refresh Token
    const accessToken = user.generateAccessToken();
    const refreshTokenString = crypto.randomBytes(40).toString('hex');
    const expiresAt = new Date();
    expiresAt.setDate(expiresAt.getDate() + 7); // 7 ngày

    await RefreshToken.create({
      user_id: user.id,
      token: refreshTokenString,
      device_info: req.headers['user-agent'] || 'Mobile App',
      expires_at: expiresAt,
    }, { transaction });

    await transaction.commit();


    res.status(201).json({
      success: true,
      message: 'Đăng ký tài khoản thành công',
      data: {
        user,
        tokens: {
          access_token: accessToken,
          refresh_token: refreshTokenString,
          token_type: 'Bearer',
          expires_in: env.JWT_EXPIRES_IN,
        },
      },
    });
  } catch (error) {
      await transaction.rollback();
      next(error);
  }
};

/**
 * Đăng nhập hệ thống
 * POST /api/auth/login
 */
const login = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return res.status(400).json({
        success: false,
        message: 'Dữ liệu không hợp lệ',
        errors: errors.array().map((e) => e.msg),
      });
    }

    const { email, password } = req.body;

    const user = await User.findOne({ where: { email } });
    if (!user) {
      return res.status(401).json({
        success: false,
        message: 'Email hoặc mật khẩu không chính xác',
      });
    }

    if (user.status !== 'ACTIVE') {
      return res.status(403).json({
        success: false,
        message: 'Tài khoản của bạn hiện đang bị khóa hoặc ngưng hoạt động',
      });
    }

    const isMatch = await user.comparePassword(password);
    if (!isMatch) {
      return res.status(401).json({
        success: false,
        message: 'Email hoặc mật khẩu không chính xác',
      });
    }

    // Sinh access token & refresh token
    const accessToken = user.generateAccessToken();
    const refreshTokenString = crypto.randomBytes(40).toString('hex');
    const expiresAt = new Date();
    expiresAt.setDate(expiresAt.getDate() + 7);

    await RefreshToken.create({
      user_id: user.id,
      token: refreshTokenString,
      device_info: req.headers['user-agent'] || 'Unknown Device',
      expires_at: expiresAt,
    });

    res.json({
      success: true,
      message: 'Đăng nhập thành công',
      data: {
        user,
        tokens: {
          access_token: accessToken,
          refresh_token: refreshTokenString,
          token_type: 'Bearer',
          expires_in: env.JWT_EXPIRES_IN,
        },
      },
    });
  } catch (error) {
    next(error);
  }
};

/**
 * Làm mới Access Token thông qua Refresh Token
 * POST /api/auth/refresh-token
 */
const refreshToken = async (req, res, next) => {
  try {
    const { refresh_token } = req.body;
    if (!refresh_token) {
      return res.status(400).json({
        success: false,
        message: 'Vui lòng cung cấp refresh_token',
      });
    }

    const storedToken = await RefreshToken.findOne({
      where: {
        token: refresh_token,
        is_revoked: false,
      },
      include: [{ model: User, as: 'user' }],
    });

    if (!storedToken || new Date() > new Date(storedToken.expires_at)) {
      return res.status(401).json({
        success: false,
        message: 'Refresh token không hợp lệ hoặc đã hết hạn. Vui lòng đăng nhập lại.',
      });
    }

    const user = storedToken.user;
    if (!user || user.status !== 'ACTIVE') {
      return res.status(403).json({
        success: false,
        message: 'Tài khoản không hợp lệ hoặc đã bị khóa.',
      });
    }

    // Cấp access token mới
    const newAccessToken = user.generateAccessToken();

    res.json({
      success: true,
      message: 'Làm mới token thành công',
      data: {
        access_token: newAccessToken,
        token_type: 'Bearer',
        expires_in: env.JWT_EXPIRES_IN,
      },
    });
  } catch (error) {
    next(error);
  }
};

/**
 * Đăng xuất và thu hồi Refresh Token
 * POST /api/auth/logout
 */
const logout = async (req, res, next) => {
  try {
    const { refresh_token } = req.body;

    if (refresh_token) {
      await RefreshToken.update(
        { is_revoked: true },
        { where: { token: refresh_token, user_id: req.userId } }
      );
    }

    res.json({
      success: true,
      message: 'Đăng xuất thành công',
    });
  } catch (error) {
    next(error);
  }
};

/**
 * Lấy thông tin cá nhân của phiên đăng nhập hiện tại
 * GET /api/auth/me
 */
const getMe = async (req, res, next) => {
  try {
    const user = await User.findByPk(req.userId);
    res.json({
      success: true,
      data: { user },
    });
  } catch (error) {
    next(error);
  }
};

// Validation rules
const registerValidation = [
  body('email').trim().isEmail().withMessage('Email không đúng định dạng').normalizeEmail(),
  body('password').isLength({ min: 6 }).withMessage('Mật khẩu tối thiểu 6 ký tự'),
  body('full_name').trim().notEmpty().withMessage('Vui lòng nhập họ và tên'),
  body('phone_number')
    .optional({ nullable: true })
    .matches(/^(0|\+84)[0-9]{9,10}$/)
    .withMessage('Số điện thoại không hợp lệ'),
  body('role')
    .optional()
    .isIn(['STUDENT', 'LANDLORD', 'ADMIN'])
    .withMessage('Role phải là STUDENT, LANDLORD hoặc ADMIN'),
];

const loginValidation = [
  body('email').trim().isEmail().withMessage('Email không đúng định dạng').normalizeEmail(),
  body('password').notEmpty().withMessage('Vui lòng nhập mật khẩu'),
];

module.exports = {
  register,
  login,
  refreshToken,
  logout,
  getMe,
  registerValidation,
  loginValidation,
};
