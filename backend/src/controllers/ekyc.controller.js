const path = require('path');
const fs = require('fs');
const multer = require('multer');
const LandlordUser = require('../models/LandlordUser');
const User = require('../models/User');
const { extractCccdFromFiles } = require('../services/ekyc.service');

// Cấu hình thư mục uploads
const uploadDir = path.join(__dirname, '../../uploads/ekyc');
if (!fs.existsSync(uploadDir)) {
  fs.mkdirSync(uploadDir, { recursive: true });
}

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, uploadDir);
  },
  filename: (req, file, cb) => {
    const ext = path.extname(file.originalname) || '.jpg';
    const uniqueSuffix = `${Date.now()}-${Math.round(Math.random() * 1e9)}`;
    cb(null, `${file.fieldname}-${uniqueSuffix}${ext}`);
  },
});

const upload = multer({
  storage,
  limits: { fileSize: 10 * 1024 * 1024 }, // 10MB
});

// Sử dụng upload.any() thay vì upload.fields() cố định tên field
// Giúp nhận diện linh hoạt mọi tên field (frontImage, backImage, front_image, cccd_front, images, v.v.)
// và tránh triệt để lỗi MulterError: Unexpected file field
const uploadCccdFiles = upload.any();

/**
 * Controller xử lý upload eKYC cho chủ trọ
 * POST /api/landlord/ekyc
 */
const submitLandlordEkyc = async (req, res, next) => {
  try {
    const rawFiles = req.files || [];
    
    // Tìm file mặt trước và mặt sau dựa trên fieldname hoặc thứ tự upload
    let frontFile = null;
    let backFile = null;

    if (Array.isArray(rawFiles)) {
      for (const file of rawFiles) {
        const fname = (file.fieldname || '').toLowerCase();
        if (fname.includes('front') || fname.includes('truoc')) {
          frontFile = file;
        } else if (fname.includes('back') || fname.includes('sau')) {
          backFile = file;
        }
      }

      // Nếu không khớp theo tên, tự động lấy file đầu tiên làm mặt trước, file thứ hai làm mặt sau
      if (!frontFile && rawFiles.length > 0) frontFile = rawFiles[0];
      if (!backFile && rawFiles.length > 1) backFile = rawFiles[1];
    } else if (typeof rawFiles === 'object') {
      frontFile = (rawFiles.frontImage && rawFiles.frontImage[0]) || (rawFiles.front && rawFiles.front[0]);
      backFile = (rawFiles.backImage && rawFiles.backImage[0]) || (rawFiles.back && rawFiles.back[0]);
    }

    if (!frontFile && !backFile) {
      return res.status(400).json({
        success: false,
        message: 'Vui lòng cung cấp ít nhất ảnh mặt trước CCCD',
      });
    }

    const frontPath = frontFile ? frontFile.path : null;
    const backPath = backFile ? backFile.path : null;

    // Phân tích OCR
    const ocrData = await extractCccdFromFiles(frontPath, backPath, req.user);

    // Cập nhật thông tin profile của chủ trọ trong DB
    let landlordProfile = await LandlordUser.findOne({ where: { user_id: req.user.id } });
    if (!landlordProfile) {
      landlordProfile = await LandlordUser.create({
        user_id: req.user.id,
        phone_number: req.user.phone_number,
      });
    }

    await landlordProfile.update({
      identity_card_number: ocrData.id_number,
      identity_card_front: frontFile ? `/uploads/ekyc/${frontFile.filename}` : landlordProfile.identity_card_front,
      identity_card_back: backFile ? `/uploads/ekyc/${backFile.filename}` : landlordProfile.identity_card_back,
      contact_address: ocrData.place_of_residence || landlordProfile.contact_address,
    });

    // Kích hoạt tài khoản chủ trọ ACTIVE - Hoàn tất toàn bộ luồng đăng ký
    await User.update(
      { status: 'ACTIVE' },
      { where: { id: req.user.id } }
    );

    return res.json({
      success: true,
      message: 'Quét và xác thực CCCD thành công',
      data: {
        success: true,
        message: 'Trích xuất thông tin thành công',
        data: ocrData,
      },
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  uploadCccdFiles,
  submitLandlordEkyc,
};
