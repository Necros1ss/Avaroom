const express = require('express');
const router = express.Router();
const {
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
} = require('../controllers/auth.controller');
const { auth } = require('../middleware/auth.middleware');
const { loginLimiter, registerLimiter } = require('../middleware/rateLimiter');

// Public endpoints
router.post('/register', registerLimiter, registerValidation, register);
router.post('/login', loginLimiter, loginValidation, login);
router.post('/refresh-token', refreshToken);

// Protected endpoints (yêu cầu JWT)
router.get('/me', auth, getMe);
router.post('/logout', auth, logout);

router.post('/forgot-password', loginLimiter, forgotPassword);
router.post('/verify-otp', verifyOtp);
router.post('/reset-password', resetPassword);

module.exports = router;
