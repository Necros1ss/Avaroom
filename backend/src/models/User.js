const { DataTypes, Model } = require('sequelize');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { sequelize } = require('../config/database');
const env = require('../config/env');
const StudentUser = require('./StudentUser');
const LandlordUser = require('./LandlordUser');

class User extends Model {
  async comparePassword(userPassword){
    return bcrypt.compare(userPassword, this.password_hash);
  }

  generateAccessToken(){
    return jwt.sign(
      {
        id: this.id,
        email: this.email,
        role: this.role,
      },
      env.JWT_SECRET,
      { 
        expiresIn: env.JWT_EXPIRES_IN 
      }
    )
  }

  toJSON() {
    const values = { ...this.get() };
    delete values.password_hash;
    return values;
  }
}

User.init(
  {
    id: {
      type: DataTypes.BIGINT,
      autoIncrement: true,
      primaryKey: true,
    },
    email: {
      type: DataTypes.STRING(255),
      allowNull: false,
      unique: true,
      validate: {
        isEmail: true,
      },
    },
    phone_number: {
      type: DataTypes.STRING(20),
      allowNull: true,
      unique: true,
    },
    password_hash: {
      type: DataTypes.STRING(255),
      allowNull: false,
    },
    full_name: {
      type: DataTypes.STRING(100),
      allowNull: false,
    },
    avatar_url: {
      type: DataTypes.STRING(500),
      allowNull: true,
    },
    role: {
      type: DataTypes.ENUM('STUDENT', 'LANDLORD', 'ADMIN'),
      allowNull: false,
      defaultValue: 'STUDENT',
    },
    status: {
      type: DataTypes.ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'DELETED'),
      allowNull: false,
      defaultValue: 'ACTIVE',
    },
  },
  {
    sequelize,
    modelName: 'User',
    tableName: 'users',
    timestamps: true,
    createdAt: 'created_at',
    updatedAt: 'updated_at',
    hooks: {
      beforeSave: async (user) => {
        if (user.changed('password_hash')) {
          const salt = await bcrypt.genSalt(12);
          user.password_hash = await bcrypt.hash(user.password_hash, salt);
        }
      },
    },
  }
);

User.hasOne(StudentUser, { as: 'studentProfile', foreignKey: 'user_id', onDelete: 'CASCADE' });
StudentUser.belongsTo(User, { as: 'user', foreignKey: 'user_id' });

User.hasOne(LandlordUser, { as: 'landlordProfile', foreignKey: 'user_id', onDelete: 'CASCADE' });
LandlordUser.belongsTo(User, { as: 'user', foreignKey: 'user_id' });

module.exports = User;
