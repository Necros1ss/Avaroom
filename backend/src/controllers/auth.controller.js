const crypto = require('crypto');
const { validationResult, body } = require('express-validator');
const User = require('../models/User');
const RefreshToken = require('../models/RefreshToken');
const env = require('../config/env');
const { sequelize } = require('../config/database');
const StudentUser = require('../models/StudentUser');
const LandlordUser = require('../models/LandlordUser');
const PasswordResetOtp = require('../models/PasswordResetOTP');
const { sendOtpMail } = require('../services/mail.service');

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

    const otpCode = Math.floor(100000 + Math.random() * 900000).toString();
    const expiresAtOtp = new Date(Date.now() + 10 * 60 * 1000); // 10 phút

    await PasswordResetOtp.update(
       { is_used: true },
       { where: { email, is_used: false }, transaction }
    );

    await PasswordResetOtp.create({
      email,
      otp_code: otpCode,
      expires_at: expiresAtOtp,
      is_used: false,
    }, { transaction });
    // Gửi email chứa OTP
    await sendOtpMail(email, otpCode);

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

const forgotPassword = async (req, res, next) => {
  try {
    const { email } = req.body;
    if (!email) {
      return res.status(400).json({ success: false, message: 'Vui lòng cung cấp email' });
    }
    const user = await User.findOne({ where: { email } });
    if (!user) {
      return res.status(404).json({ success: false, message: 'Email này chưa được đăng ký tài khoản' });
    }
    // Sinh mã OTP ngẫu nhiên 6 chữ số (100000 - 999999)
    const otpCode = Math.floor(100000 + Math.random() * 900000).toString();
    const expiresAt = new Date(Date.now() + 10 * 60 * 1000); // 10 phút
    // Vô hiệu hoá các OTP cũ của email này nếu còn tồn tại
    await PasswordResetOtp.update({ is_used: true }, { where: { email, is_used: false } });
    // Lưu OTP mới
    await PasswordResetOtp.create({
      email,
      otp_code: otpCode,
      expires_at: expiresAt,
      is_used: false,
    });
    // Gửi email
    await sendOtpMail(email, otpCode);
    res.json({
      success: true,
      message: 'Mã xác thực OTP đã được gửi đến email của bạn',
    });
  } catch (error) {
    next(error);
  }
};

const verifyOtp = async (req, res, next) => {
  try {
    const { email, otp } = req.body;
    if (!email || !otp) {
      return res.status(400).json({ success: false, message: 'Vui lòng nhập đầy đủ email và mã OTP' });
    }
    const record = await PasswordResetOtp.findOne({
      where: {
        email,
        otp_code: otp,
        is_used: false,
      },
      order: [['created_at', 'DESC']],
    });
    if (!record || new Date() > new Date(record.expires_at)) {
      return res.status(400).json({
        success: false,
        message: 'Mã OTP không chính xác hoặc đã hết hạn',
      });
    }
    // Sinh reset_token ngẫu nhiên an toàn cho luồng Quên mật khẩu
    const resetToken = crypto.randomBytes(32).toString('hex');
    await record.update({ reset_token: resetToken });

    // Lấy thông tin user và cấp token nếu là xác thực sau Đăng ký
    const user = await User.findOne({ where: { email } });
    let tokens = null;
    if (user) {
      if (user.status !== 'ACTIVE') {
        user.status = 'ACTIVE';
        await user.save();
      }
      const accessToken = user.generateAccessToken();
      const refreshTokenString = crypto.randomBytes(40).toString('hex');
      const expiresAt = new Date();
      expiresAt.setDate(expiresAt.getDate() + 7);
      await RefreshToken.create({
        user_id: user.id,
        token: refreshTokenString,
        device_info: req.headers['user-agent'] || 'Mobile App',
        expires_at: expiresAt,
      });
      tokens = {
        access_token: accessToken,
        refresh_token: refreshTokenString,
        token_type: 'Bearer',
        expires_in: env.JWT_EXPIRES_IN,
      };
    }

    res.json({
      success: true,
      message: 'Xác thực OTP thành công',
      data: {
        reset_token: resetToken,
        user: user ? {
          id: user.id,
          email: user.email,
          full_name: user.full_name,
          role: user.role,
        } : null,
        tokens,
      },
    });
  } catch (error) {
    next(error);
  }
};

const resetPassword = async (req, res, next) => {
  try {
    const { reset_token, new_password } = req.body;
    if (!reset_token || !new_password) {
      return res.status(400).json({ success: false, message: 'Thiếu reset_token hoặc mật khẩu mới' });
    }
    if (new_password.length < 6) {
      return res.status(400).json({ success: false, message: 'Mật khẩu mới tối thiểu phải có 6 ký tự' });
    }
    const record = await PasswordResetOtp.findOne({
      where: {
        reset_token,
        is_used: false,
      },
    });
    if (!record || new Date() > new Date(record.expires_at)) {
      return res.status(400).json({
        success: false,
        message: 'Phiên đặt lại mật khẩu không hợp lệ hoặc đã hết hạn',
      });
    }
    const user = await User.findOne({ where: { email: record.email } });
    if (!user) {
      return res.status(404).json({ success: false, message: 'Không tìm thấy người dùng' });
    }
    // Cập nhật mật khẩu mới (hook beforeSave trong User.js sẽ tự động băm)
    user.password_hash = new_password;
    await user.save();
    // Đánh dấu mã OTP đã sử dụng xong
    await record.update({ is_used: true });
    // Thu hồi toàn bộ Refresh Token cũ để ép đăng nhập lại trên mọi thiết bị
    await RefreshToken.update({ is_revoked: true }, { where: { user_id: user.id } });
    res.json({
      success: true,
      message: 'Đặt lại mật khẩu thành công. Vui lòng đăng nhập lại.',
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  register,
  login,
  refreshToken,
  logout,
  getMe,
  registerValidation,
  loginValidation,
  forgotPassword, 
  verifyOtp,      
  resetPassword,
};
