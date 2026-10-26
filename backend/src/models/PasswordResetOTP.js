const { DataTypes, Model } = require('sequelize');
const { sequelize } = require('../config/database');

class PasswordResetOtp extends Model {}

PasswordResetOtp.init(
  {
    id: {
      type: DataTypes.BIGINT,
      autoIncrement: true,
      primaryKey: true,
    },
    email: {
      type: DataTypes.STRING(255),
      allowNull: false,
      validate: { isEmail: true },
    },
    otp_code: {
      type: DataTypes.STRING(6),
      allowNull: false,
    },
    reset_token: {
      type: DataTypes.STRING(255),
      allowNull: true, // Sinh ra sau khi người dùng verify OTP thành công
    },
    expires_at: {
      type: DataTypes.DATE,
      allowNull: false,
    },
    is_used: {
      type: DataTypes.BOOLEAN,
      defaultValue: false,
    },
  },
  {
    sequelize,
    modelName: 'PasswordResetOtp',
    tableName: 'password_reset_otps',
    timestamps: true,
    createdAt: 'created_at',
    updatedAt: 'updated_at',
  }
);

module.exports = PasswordResetOtp;