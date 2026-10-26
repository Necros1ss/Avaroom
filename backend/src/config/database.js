const { Sequelize } = require('sequelize');
const env = require('./env');

let sequelize;

if (env.DATABASE_URL) {
  sequelize = new Sequelize(env.DATABASE_URL, {
    dialect: 'postgres',
    logging: env.NODE_ENV === 'development' ? console.log : false,
    dialectOptions: env.NODE_ENV === 'production' ? {
      ssl: {
        require: true,
        rejectUnauthorized: false,
      },
    } : {},
  });
} else {
  sequelize = new Sequelize(env.DB_NAME, env.DB_USER, env.DB_PASSWORD, {
    host: env.DB_HOST,
    port: env.DB_PORT,
    dialect: 'postgres',
    logging: env.NODE_ENV === 'development' ? console.log : false,
    pool: {
      max: 10,
      min: 0,
      acquire: 30000,
      idle: 10000,
    },
  });
}

const connectDB = async () => {
  try {
    await sequelize.authenticate();
    console.log(`✅ Kết nối PostgreSQL thành công tới [${env.DB_HOST}:${env.DB_PORT}/${env.DB_NAME}]`);

    // Nạp đầy đủ các models để Sequelize nhận diện toàn bộ bảng trước khi sync
    require('../models');

    // Tự động đồng bộ / tạo bảng nếu chưa có trên cả môi trường Dev lẫn Production
    await sequelize.sync({ alter: env.NODE_ENV === 'development' });
    console.log('✅ Đã đồng bộ cấu trúc bảng PostgreSQL (Sequelize sync)');
  } catch (error) {
    console.error('❌ Không thể kết nối cơ sở dữ liệu PostgreSQL:', error.message);
    process.exit(1);
  }
};

module.exports = { sequelize, connectDB };
