const { sequelize } = require('../config/database');
const { QueryTypes } = require('sequelize');

// GET /api/student/saved-rooms
// Lấy danh sách phòng đã lưu của sinh viên đang đăng nhập
const getSavedRooms = async (req, res, next) => {
  try {
    const rooms = await sequelize.query(
      `
      SELECT
        sr.id AS saved_room_id,
        sr.created_at AS saved_at,
        r.id AS room_id,
        r.title,
        r.description,
        r.price,
        r.deposit,
        r.area_sqm,
        r.max_occupants,
        r.room_type,
        r.room_status,
        r.approval_status,
        r.province,
        r.district,
        r.ward,
        r.street_address,
        cover.image_url AS cover_image
      FROM saved_rooms sr
      JOIN rooms r ON r.id = sr.room_id
      LEFT JOIN LATERAL (
        SELECT image_url
        FROM room_images
        WHERE room_id = r.id
        ORDER BY is_cover DESC, display_order ASC, id ASC
        LIMIT 1
      ) cover ON TRUE
      WHERE sr.student_id = :studentId
      ORDER BY sr.created_at DESC, sr.id DESC
      `,
      {
        replacements: { studentId: req.userId },
        type: QueryTypes.SELECT,
      }
    );

    return res.status(200).json({
      success: true,
      message: 'Lấy danh sách phòng đã lưu thành công.',
      data: { rooms },
    });
  } catch (error) {
    next(error);
  }
};

// POST /api/student/saved-rooms/:roomId
// Thêm phòng vào danh sách đã lưu
const saveRoom = async (req, res, next) => {
  try {
    const { roomId } = req.params;

    if (!/^[1-9]\d*$/.test(roomId)) {
      return res.status(400).json({
        success: false,
        message: 'ID phòng không hợp lệ.',
      });
    }

    const [room] = await sequelize.query(
      `
      SELECT id
      FROM rooms
      WHERE id = :roomId
        AND room_status = 'AVAILABLE'
        AND approval_status = 'APPROVED'
      LIMIT 1
      `,
      {
        replacements: { roomId },
        type: QueryTypes.SELECT,
      }
    );

    if (!room) {
      return res.status(404).json({
        success: false,
        message: 'Không tìm thấy phòng hoặc phòng hiện không khả dụng.',
      });
    }

    // Kiểm tra phòng đã được lưu trước đó chưa
    const [existing] = await sequelize.query(
      `
      SELECT id
      FROM saved_rooms
      WHERE student_id = :studentId AND room_id = :roomId
      LIMIT 1
      `,
      {
        replacements: {
          studentId: req.userId,
          roomId,
        },
        type: QueryTypes.SELECT,
      }
    );

    if (existing) {
      return res.status(200).json({
        success: true,
        message: 'Phòng này đã có trong danh sách đã lưu.',
      });
    }

    await sequelize.query(
      `
      INSERT INTO saved_rooms (student_id, room_id, created_at)
      VALUES (:studentId, :roomId, CURRENT_TIMESTAMP)
      `,
      {
        replacements: {
          studentId: req.userId,
          roomId,
        },
      }
    );

    return res.status(201).json({
      success: true,
      message: 'Lưu phòng thành công.',
      data: {
        room_id: Number(roomId),
      },
    });
  } catch (error) {
    // PostgreSQL: 23505 = vi phạm unique constraint.
    // Có thể xảy ra nếu hai yêu cầu lưu cùng một phòng đến đồng thời.
    if (error.original?.code === '23505') {
      return res.status(200).json({
        success: true,
        message: 'Phòng này đã có trong danh sách đã lưu.',
      });
    }

    next(error);
  }
};

// DELETE /api/student/saved-rooms/:roomId
// Xóa phòng khỏi danh sách đã lưu của sinh viên hiện tại
const removeSavedRoom = async (req, res, next) => {
  try {
    const { roomId } = req.params;

    // Kiểm tra ID phòng hợp lệ
    if (!/^[1-9]\d*$/.test(roomId)) {
      return res.status(400).json({
        success: false,
        message: 'ID phòng không hợp lệ.',
      });
    }

    // Xóa phòng chỉ thuộc danh sách của sinh viên đang đăng nhập
    const deleted = await sequelize.query(
      `
      DELETE FROM saved_rooms
      WHERE student_id = :studentId
        AND room_id = :roomId
      RETURNING id
      `,
      {
        replacements: {
          studentId: req.userId,
          roomId,
        },
        type: QueryTypes.SELECT,
      }
    );

    // Không tìm thấy phòng trong danh sách đã lưu
    if (deleted.length === 0) {
      return res.status(404).json({
        success: false,
        message: 'Phòng này chưa có trong danh sách đã lưu.',
      });
    }

    // Xóa thành công
    return res.status(200).json({
      success: true,
      message: 'Đã xóa phòng khỏi danh sách đã lưu.',
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getSavedRooms,
  saveRoom,
  removeSavedRoom,
};