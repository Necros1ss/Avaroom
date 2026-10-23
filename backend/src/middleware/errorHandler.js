const env = require('../config/env');

/**
 * Middleware xử lý lỗi toàn cục (Sequelize + Express)
 */
const errorHandler = (err, req, res, next) => {
  let error = {
    statusCode: err.statusCode || 500,
    message: err.message || 'Lỗi máy chủ nội bộ',
  };

  // Sequelize Unique Constraint Error (Trùng email,...)
  if (err.name === 'SequelizeUniqueConstraintError') {
    error.statusCode = 409;
    error.message = err.errors.map((e) => `${e.path} "${e.value}" đã được sử dụng`).join(', ');
  }

  // Sequelize Validation Error
  if (err.name === 'SequelizeValidationError') {
    error.statusCode = 400;
    error.message = err.errors.map((e) => e.message).join(', ');
  }

  // JWT Errors
  if (err.name === 'JsonWebTokenError') {
    error.statusCode = 401;
    error.message = 'Token không hợp lệ';
  }

  if (err.name === 'TokenExpiredError') {
    error.statusCode = 401;
    error.message = 'Token đã hết hạn. Vui lòng đăng nhập lại';
  }

  if (env.NODE_ENV === 'development') {
    console.error('❌ Error Log:', err);
  }

  res.status(error.statusCode).json({
    success: false,
    message: error.message,
    ...(error.errors && { errors: error.errors }),
    ...(env.NODE_ENV === 'development' && { stack: err.stack }),
  });
};

const notFound = (req, res, next) => {
  res.status(404).json({
    success: false,
    message: `Không tìm thấy route: ${req.method} ${req.originalUrl}`,
  });
};

module.exports = { errorHandler, notFound };
