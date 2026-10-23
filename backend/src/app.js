const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const morgan = require('morgan');

const env = require('./config/env');
const { connectDB } = require('./config/database');
const { errorHandler, notFound } = require('./middleware/errorHandler');

// Routes
const authRoutes = require('./routes/auth.routes');

const app = express();

// Middleware
app.use(helmet());
app.use(cors({
  origin: '*',
  methods: ['GET', 'POST', 'PUT', 'DELETE', 'PATCH'],
  allowedHeaders: ['Content-Type', 'Authorization'],
}));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

if (env.NODE_ENV !== 'test') {
  app.use(morgan(env.NODE_ENV === 'production' ? 'combined' : 'dev'));
}

// Health Check
app.get('/api/health', (req, res) => {
  res.json({
    success: true,
    message: 'Avaroom Auth API (PostgreSQL) đang hoạt động bình thường!',
    environment: env.NODE_ENV,
    timestamp: new Date().toISOString(),
  });
});

// Mount Routes (Chỉ giữ Auth & JWT)
app.use('/api/auth', authRoutes);

// Error handlers
app.use(notFound);
app.use(errorHandler);

// Start Server
const startServer = async () => {
  try {
    await connectDB();

    const PORT = env.PORT || 3000;
    app.listen(PORT, () => {
      console.log('');
      console.log('🛡️  ═══════════════════════════════════════════════');
      console.log(`🚀 Avaroom Auth Service (PostgreSQL) đang chạy!`);
      console.log(`📍 URL: http://localhost:${PORT}`);
      console.log(`🔑 Auth Endpoint: http://localhost:${PORT}/api/auth`);
      console.log(`❤️  Health: http://localhost:${PORT}/api/health`);
      console.log('🛡️  ═══════════════════════════════════════════════');
      console.log('');
    });
  } catch (error) {
    console.error('❌ Lỗi khởi động server:', error.message);
    process.exit(1);
  }
};

startServer();

module.exports = app;
