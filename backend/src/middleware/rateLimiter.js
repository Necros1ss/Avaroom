const rateLimit = require('express-rate-limit');

// Giới hạn cho Đăng nhập: Tối đa 5 lần thử sai / 15 phút trên mỗi IP
const loginLimiter = rateLimit({
  windowMs: 15 * 60 * 1000,
  max: 5,
  standardHeaders: true,
  legacyHeaders: false,
  message: {
    success: false,
    message: 'Bạn đã thử đăng nhập quá nhiều lần. Vui lòng thử lại sau 15 phút.',
  },
});

// Giới hạn cho Đăng ký: Tối đa 10 tài khoản / 1 giờ trên mỗi IP
const registerLimiter = rateLimit({
  windowMs: 60 * 60 * 1000,
  max: 10,
  standardHeaders: true,
  legacyHeaders: false,
  message: {
    success: false,
    message: 'Quá nhiều yêu cầu đăng ký từ địa chỉ IP này. Vui lòng thử lại sau 1 giờ.',
  },
});

module.exports = {
  loginLimiter,
  registerLimiter,
};