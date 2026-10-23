const { DataTypes, Model } = require('sequelize');
const { sequelize } = require('../config/database');

class LandlordUser extends Model {}

LandlordUser.init(
  {
    id: {
      type: DataTypes.BIGINT,
      autoIncrement: true,
      primaryKey: true,
    },
    user_id: {
      type: DataTypes.BIGINT,
      allowNull: false, // Có gắn nhãn NN (Not Null) trên sơ đồ
      unique: true,     // Đảm bảo quan hệ 1-1 với bảng users
      references: {
        model: 'users',
        key: 'id',
      },
      onDelete: 'CASCADE',
      onUpdate: 'CASCADE',
    },
    phone_number: {
      type: DataTypes.STRING(20),
      allowNull: true,
    },
    identity_card_number: {
      type: DataTypes.STRING(20),
      allowNull: true, // Để true vì ở bước đăng ký tài khoản ban đầu chưa có CCCD, sẽ cập nhật ở màn hình eKYC
    },
    identity_card_front: {
      type: DataTypes.STRING(500),
      allowNull: true,
    },
    identity_card_back: {
      type: DataTypes.STRING(500),
      allowNull: true,
    },
    contact_address: {
      type: DataTypes.TEXT,
      allowNull: true,
    },
  },
  {
    sequelize,
    modelName: 'Landlord',
    tableName: 'landlord_profiles',
    timestamps: true,
    createdAt: 'created_at',
    updatedAt: 'updated_at',
  }
);

module.exports = LandlordUser;