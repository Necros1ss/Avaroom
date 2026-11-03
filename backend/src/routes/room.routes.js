const express = require('express');
const {
  getRooms,
  getRoomDetail,
} = require('../controllers/room.controller');

const router = express.Router();

router.get('/', getRooms);
router.get('/:id', getRoomDetail);

module.exports = router;