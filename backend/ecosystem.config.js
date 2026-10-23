// Cấu hình PM2 cho triển khai production
module.exports = {
  apps: [{
    name: 'avaroom',
    script: 'src/app.js',
    instances: 'max',
    exec_mode: 'cluster',
    env: {
      NODE_ENV: 'development'
    },
    env_production: {
      NODE_ENV: 'production'
    }
  }]
};
