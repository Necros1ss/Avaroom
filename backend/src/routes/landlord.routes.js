const express = require('express');
const router = express.Router();
const { authForEkyc } = require('../middleware/auth.middleware');
const { uploadCccdFiles, submitLandlordEkyc } = require('../controllers/ekyc.controller');

// ==================== LANDLORD EKYC ROUTE ====================
// Dùng authForEkyc thay vì auth + authorize('LANDLORD')
// vì tài khoản chủ trọ ở bước này vẫn đang INACTIVE (chờ eKYC xong mới ACTIVE)
router.post('/ekyc', authForEkyc, uploadCccdFiles, submitLandlordEkyc);

module.exports = router;
