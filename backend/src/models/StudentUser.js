const { DataTypes, Model } = require('sequelize');
const { sequelize } = require('../config/database');

class StudentUser extends Model {}

StudentUser.init(
  {
    id: {
      type: DataTypes.BIGINT,
      autoIncrement: true,
      primaryKey: true,
    },
    user_id: {
      type: DataTypes.BIGINT,
      allowNull: false,
      unique: true,
      references: {
        model: 'users',
        key: 'id',
      },
      onDelete: 'CASCADE',
      onUpdate: 'CASCADE',
    },
    university_id: {
      type: DataTypes.BIGINT,
      allowNull: true,
      references: {
          model: 'universities',
          key: 'id',
      },
      onDelete: 'SET NULL',
      onUpdate: 'CASCADE',
    },
    student_code: {
     type: DataTypes.STRING(50),
     allowNull: true,
    },
    academic_year: {
     type: DataTypes.STRING(20),
     allowNull: true,
    },
    student_card_image: {
     type: DataTypes.STRING(500),
     allowNull: true,
    },
    is_edu_verified: {
     type: DataTypes.BOOLEAN,
     allowNull: true,
    },
  },
  {
    sequelize,
    modelName: 'Students',
    tableName: 'student_profiles',
  }
);

module.exports = StudentUser;
