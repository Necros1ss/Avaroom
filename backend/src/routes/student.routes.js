const express = require('express');
const {
  getSavedRooms,
  saveRoom,
  removeSavedRoom,
} = require('../controllers/savedRoom.controller');
const { auth, authorize } = require('../middleware/auth.middleware');

const router = express.Router();

// Tất cả API bên dưới yêu cầu đăng nhập và vai trò STUDENT
router.use(auth, authorize('STUDENT'));

router.get('/saved-rooms', getSavedRooms);
router.post('/saved-rooms/:roomId', saveRoom);
router.delete('/saved-rooms/:roomId', removeSavedRoom);

module.exports = router;