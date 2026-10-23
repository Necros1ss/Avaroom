const jwt = require('jsonwebtoken');
const User = require('../models/User');
const env = require('../config/env');
const { sequelize } = require('../config/database');


/**
 * Middleware xác thực Bearer JWT token
 */
const auth = async (req, res, next) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return res.status(401).json({
        success: false,
        message: 'Vui lòng đăng nhập để tiếp tục (Thiếu Bearer Token)',
      });
    }

    const token = authHeader.split(' ')[1];
    const decoded = jwt.verify(token, env.JWT_SECRET);

    const user = await User.findByPk(decoded.id);
    if (!user) {
      return res.status(401).json({
        success: false,
        message: 'Token không hợp lệ hoặc người dùng không còn tồn tại',
      });
    }

    if (user.status !== 'ACTIVE') {
      return res.status(403).json({
        success: false,
        message: 'Tài khoản của bạn hiện đang bị khóa hoặc chưa kích hoạt',
      });
    }

    req.user = user;
    req.userId = user.id;
    next();
  } catch (error) {
    if (error.name === 'JsonWebTokenError') {
      return res.status(401).json({
        success: false,
        message: 'Token không hợp lệ',
      });
    }
    if (error.name === 'TokenExpiredError') {
      return res.status(401).json({
        success: false,
        message: 'Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại hoặc làm mới token',
      });
    }
    next(error);
  }
};

/**
 * Middleware phân quyền (Role-based access control)
 * @param {...string} roles - Ví dụ: 'STUDENT', 'LANDLORD', 'ADMIN'
 */
const authorize = (...roles) => {
  return (req, res, next) => {
    if (!req.user) {
      return res.status(401).json({
        success: false,
        message: 'Yêu cầu đăng nhập',
      });
    }

    if (!roles.includes(req.user.role)) {
      return res.status(403).json({
        success: false,
        message: `Bạn không có quyền thực hiện hành động này. Yêu cầu quyền: ${roles.join(', ')}`,
      });
    }

    next();
  };
};

module.exports = { auth, authorize };
