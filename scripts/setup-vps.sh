#!/bin/bash
# ==============================================================================
# Avaroom VPS Setup Script (Ubuntu 22.04 / 24.04 LTS)
# Cài đặt: Node.js 20 LTS, PostgreSQL 16, PM2, Nginx, UFW Firewall
# ==============================================================================

set -euo pipefail

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}======================================================${NC}"
echo -e "${BLUE}   🚀 BẮT ĐẦU CÀI ĐẶT MÔI TRƯỜNG POSTGRESQL & NODE.JS ${NC}"
echo -e "${BLUE}======================================================${NC}"

if [ "$EUID" -ne 0 ]; then
  echo -e "${RED}❌ Vui lòng chạy script với quyền root hoặc sudo.${NC}"
  exit 1
fi

# 1. Cập nhật hệ thống
echo -e "\n${YELLOW}[1/7] Cập nhật gói hệ thống...${NC}"
apt update -y && apt upgrade -y
apt install -y curl wget git ufw htop build-essential gnupg lsb-release

# 2. Cài đặt Node.js 20 LTS
echo -e "\n${YELLOW}[2/7] Cài đặt Node.js LTS...${NC}"
if ! command -v node &> /dev/null; then
  curl -fsSL https://deb.nodesource.com/setup_20.x | bash -
  apt install -y nodejs
else
  echo -e "${GREEN}Node.js đã tồn tại: $(node -v)${NC}"
fi

# 3. Cài đặt PM2
echo -e "\n${YELLOW}[3/7] Cài đặt PM2...${NC}"
npm install -g pm2

# 4. Cài đặt PostgreSQL 16
echo -e "\n${YELLOW}[4/7] Cài đặt PostgreSQL Database...${NC}"
if ! command -v psql &> /dev/null; then
  apt install -y postgresql postgresql-contrib
  systemctl start postgresql
  systemctl enable postgresql

  # Tạo DB và User mặc định cho dự án
  echo -e "${YELLOW}Khởi tạo database 'avaroom' và user 'postgres'...${NC}"
  sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'postgres';"
  sudo -u postgres psql -c "CREATE DATABASE avaroom;" || true
  echo -e "${GREEN}PostgreSQL đã được cài đặt và tạo DB avaroom thành công.${NC}"
else
  echo -e "${GREEN}PostgreSQL đã tồn tại: $(psql --version)${NC}"
fi

# 5. Cài đặt Nginx
echo -e "\n${YELLOW}[5/7] Cài đặt Nginx...${NC}"
apt install -y nginx
systemctl start nginx
systemctl enable nginx

# 6. Tạo người dùng deploy và cấu trúc thư mục
echo -e "\n${YELLOW}[6/7] Cấu hình user deploy và thư mục ứng dụng...${NC}"
if ! id "deploy" &>/dev/null; then
  useradd -m -s /bin/bash deploy
  usermod -aG sudo deploy
  echo "deploy ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/deploy
  mkdir -p /home/deploy/.ssh
  if [ -f /root/.ssh/authorized_keys ]; then
    cp /root/.ssh/authorized_keys /home/deploy/.ssh/
    chown -R deploy:deploy /home/deploy/.ssh
    chmod 700 /home/deploy/.ssh
    chmod 600 /home/deploy/.ssh/authorized_keys
  fi
fi

mkdir -p /home/deploy/avaroom/releases
mkdir -p /home/deploy/avaroom/current
chown -R deploy:deploy /home/deploy/avaroom

# Cấu hình Nginx Reverse Proxy
cat << 'EOF' > /etc/nginx/sites-available/avaroom
server {
    listen 80;
    server_name _;

    client_max_body_size 20M;

    location / {
        proxy_pass http://127.0.0.1:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
    }
}
EOF

ln -sf /etc/nginx/sites-available/avaroom /etc/nginx/sites-enabled/
rm -f /etc/nginx/sites-enabled/default
nginx -t && systemctl reload nginx

# 7. Cấu hình Firewall UFW
echo -e "\n${YELLOW}[7/7] Kích hoạt Firewall...${NC}"
ufw allow OpenSSH
ufw allow 'Nginx Full'
ufw --force enable

echo -e "\n${GREEN}======================================================${NC}"
echo -e "${GREEN}   ✅ SETUP VPS THÀNH CÔNG (POSTGRESQL + NODE.JS)!   ${NC}"
echo -e "${GREEN}======================================================${NC}"
