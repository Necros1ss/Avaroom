const { sequelize } = require('../config/database');
const { QueryTypes } = require('sequelize');

const getRooms = async (req, res, next) => {
  try {
    const {
      q,
      province,
      district,
      ward,
      roomType,
      minPrice,
      maxPrice,
      minArea,
      maxArea,
      page = '1',
      limit = '10',
      sort = 'newest',
    } = req.query;

    // Kiểm tra và giới hạn phân trang
    const pageNumber = Math.max(1, parseInt(page, 10) || 1);
    const pageSize = Math.min(
      50,
      Math.max(1, parseInt(limit, 10) || 10)
    );
    const offset = (pageNumber - 1) * pageSize;

    // Điều kiện mặc định: chỉ lấy phòng đang trống và đã duyệt
    const conditions = [
      `r.room_status = 'AVAILABLE'`,
      `r.approval_status = 'APPROVED'`,
    ];

    const replacements = {};

    // Tìm kiếm theo tiêu đề và địa chỉ
    if (q && q.trim()) {
      conditions.push(`
        (
          r.title ILIKE :keyword
          OR r.street_address ILIKE :keyword
          OR r.ward ILIKE :keyword
          OR r.district ILIKE :keyword
          OR r.province ILIKE :keyword
        )
      `);
      replacements.keyword = `%${q.trim()}%`;
    }

    // Lọc theo khu vực
    if (province) {
      conditions.push('r.province = :province');
      replacements.province = province;
    }

    if (district) {
      conditions.push('r.district = :district');
      replacements.district = district;
    }

    if (ward) {
      conditions.push('r.ward = :ward');
      replacements.ward = ward;
    }

    // Lọc loại phòng
    const allowedRoomTypes = [
      'DORMITORY',
      'PRIVATE_ROOM',
      'SHARED_ROOM',
      'MINI_APARTMENT',
      'WHOLE_HOUSE',
    ];

    if (roomType) {
      if (!allowedRoomTypes.includes(roomType)) {
        return res.status(400).json({
          success: false,
          message: 'Loại phòng không hợp lệ.',
        });
      }

      conditions.push('r.room_type = :roomType');
      replacements.roomType = roomType;
    }

    // Lọc giá thuê và diện tích
    const numericFilters = [
      ['minPrice', minPrice, 'r.price >= :minPrice'],
      ['maxPrice', maxPrice, 'r.price <= :maxPrice'],
      ['minArea', minArea, 'r.area_sqm >= :minArea'],
      ['maxArea', maxArea, 'r.area_sqm <= :maxArea'],
    ];

    for (const [key, value, condition] of numericFilters) {
      if (value !== undefined && value !== '') {
        const number = Number(value);

        if (!Number.isFinite(number) || number < 0) {
          return res.status(400).json({
            success: false,
            message: `Tham số ${key} phải là số không âm.`,
          });
        }

        conditions.push(condition);
        replacements[key] = number;
      }
    }

    // Chỉ cho phép những kiểu sắp xếp được quy định
    const sortOptions = {
      newest: 'r.created_at DESC',
      price_asc: 'r.price ASC',
      price_desc: 'r.price DESC',
      area_desc: 'r.area_sqm DESC',
    };

    if (!sortOptions[sort]) {
      return res.status(400).json({
        success: false,
        message: 'Kiểu sắp xếp không hợp lệ.',
      });
    }

    const whereClause = conditions.join(' AND ');

    // Đếm tổng số phòng phù hợp
    const countResult = await sequelize.query(
      `
        SELECT COUNT(*)::int AS total
        FROM rooms r
        WHERE ${whereClause}
      `,
      {
        replacements,
        type: QueryTypes.SELECT,
      }
    );

    const total = countResult[0].total;

    // Lấy danh sách phòng và ảnh đại diện
    const rooms = await sequelize.query(
      `
        SELECT
          r.id,
          r.landlord_id,
          r.title,
          r.description,
          r.room_type,
          r.price,
          r.deposit,
          r.electricity_cost,
          r.water_cost,
          r.internet_cost,
          r.parking_fee,
          r.area_sqm,
          r.max_occupants,
          r.province,
          r.district,
          r.ward,
          r.street_address,
          r.latitude,
          r.longitude,
          r.view_count,
          r.is_featured,
          r.created_at,
          (
            SELECT ri.image_url
            FROM room_images ri
            WHERE ri.room_id = r.id
            ORDER BY ri.is_cover DESC, ri.display_order ASC, ri.id ASC
            LIMIT 1
          ) AS cover_image
        FROM rooms r
        WHERE ${whereClause}
        ORDER BY ${sortOptions[sort]}, r.id DESC
        LIMIT :limit OFFSET :offset
      `,
      {
        replacements: {
          ...replacements,
          limit: pageSize,
          offset,
        },
        type: QueryTypes.SELECT,
      }
    );

    return res.status(200).json({
      success: true,
      message: 'Lấy danh sách phòng trọ thành công.',
      data: {
        rooms,
        pagination: {
          page: pageNumber,
          limit: pageSize,
          total,
          totalPages: Math.ceil(total / pageSize),
        },
      },
    });
  } catch (error) {
    next(error);
  }
};

const getRoomDetail = async (req, res, next) => {
  try {
    const { id } = req.params;

    // Kiểm tra ID hợp lệ
    if (!/^\d+$/.test(id) || Number(id) <= 0) {
      return res.status(400).json({
        success: false,
        message: 'ID phòng trọ không hợp lệ.',
      });
    }

    // Lấy thông tin phòng trọ
    const rooms = await sequelize.query(
      `
        SELECT
          r.id,
          r.landlord_id,
          r.title,
          r.description,
          r.room_type,
          r.price,
          r.deposit,
          r.electricity_cost,
          r.water_cost,
          r.internet_cost,
          r.parking_fee,
          r.area_sqm,
          r.max_occupants,
          r.province,
          r.district,
          r.ward,
          r.street_address,
          r.latitude,
          r.longitude,
          r.view_count,
          r.is_featured,
          r.created_at
        FROM rooms r
        WHERE r.id = :id
          AND r.room_status = 'AVAILABLE'
          AND r.approval_status = 'APPROVED'
        LIMIT 1
      `,
      {
        replacements: { id },
        type: QueryTypes.SELECT,
      }
    );

    if (rooms.length === 0) {
      return res.status(404).json({
        success: false,
        message: 'Không tìm thấy phòng trọ hoặc phòng chưa được công khai.',
      });
    }

    // Lấy danh sách hình ảnh của phòng
    const images = await sequelize.query(
      `
        SELECT id, image_url, is_cover, display_order
        FROM room_images
        WHERE room_id = :id
        ORDER BY is_cover DESC, display_order ASC, id ASC
      `,
      {
        replacements: { id },
        type: QueryTypes.SELECT,
      }
    );

    return res.status(200).json({
      success: true,
      message: 'Lấy chi tiết phòng trọ thành công.',
      data: {
        ...rooms[0],
        images,
      },
    });
  } catch (error) {
    next(error);
  }
};

module.exports = {
  getRooms,
  getRoomDetail,
};