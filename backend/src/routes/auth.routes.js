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

module.exports = router;
