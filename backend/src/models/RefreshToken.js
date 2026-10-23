const { DataTypes, Model } = require('sequelize');
const { sequelize } = require('../config/database');
const User = require('./User');

class RefreshToken extends Model {}

RefreshToken.init(
  {
    id: {
      type: DataTypes.BIGINT,
      autoIncrement: true,
      primaryKey: true,
    },
    user_id: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'users',
        key: 'id',
      },
    },
    token: {
      type: DataTypes.STRING(500),
      allowNull: false,
      unique: true,
    },
    device_info: {
      type: DataTypes.STRING(255),
      allowNull: true,
    },
    expires_at: {
      type: DataTypes.DATE,
      allowNull: false,
    },
    is_revoked: {
      type: DataTypes.BOOLEAN,
      allowNull: false,
      defaultValue: false,
    },
  },
  {
    sequelize,
    modelName: 'RefreshToken',
    tableName: 'refresh_tokens',
    timestamps: true,
    createdAt: 'created_at',
    updatedAt: false,
  }
);

RefreshToken.belongsTo(User, { as: 'user', foreignKey: 'user_id' });
User.hasMany(RefreshToken, { as: 'refreshTokens', foreignKey: 'user_id' });

module.exports = RefreshToken;
