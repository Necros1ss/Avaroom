#!/usr/bin/env bash
# ==============================================================================
# Script tự động đóng gói và Deploy Backend lên VPS
# Tự động đọc cấu hình từ file scripts/.env
# ==============================================================================

set -euo pipefail

# Xác định thư mục chứa script để đọc đúng file .env dù chạy từ bất kỳ đâu
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
ENV_FILE="${SCRIPT_DIR}/.env"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

if [ ! -f "${ENV_FILE}" ]; then
  echo -e "${RED}❌ Không tìm thấy file cấu hình: ${ENV_FILE}!${NC}"
  echo -e "Vui lòng tạo file scripts/.env chứa các biến: VPS_HOST, VPS_PORT, VPS_USER, VPS_REMOTE_DIR"
  exit 1
fi

# Nạp các biến từ scripts/.env (hỗ trợ cả định dạng CRLF của Windows)
set -a
# shellcheck disable=SC1090
source <(sed 's/\r$//' "${ENV_FILE}" | grep -v '^[[:space:]]*#' | grep -v '^[[:space:]]*$')
set +a

# Map các biến từ file .env
VPS_HOST="${VPS_HOST:-card.nekos.work}"
VPS_PORT="${VPS_PORT:-22}"
VPS_USER="${VPS_USER:-zenith}"
REMOTE_PATH="${VPS_REMOTE_DIR:-/home/zenith/avaroom}"

BACKEND_LOCAL_DIR="${ROOT_DIR}/backend"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
ARCHIVE_NAME="avaroom_backend_${TIMESTAMP}.tar.gz"

echo -e "${BLUE}======================================================${NC}"
echo -e "${BLUE}   🚀 BẮT ĐẦU QUY TRÌNH DEPLOY AVAROOM BACKEND        ${NC}"
echo -e "${BLUE}   Host: ${VPS_USER}@${VPS_HOST}:${VPS_PORT}          ${NC}"
echo -e "${BLUE}   Thư mục remote: ${REMOTE_PATH}                      ${NC}"
echo -e "${BLUE}======================================================${NC}"

# 1. Đóng gói mã nguồn backend
echo -e "\n${YELLOW}[1/4] Đóng gói backend (loại trừ node_modules, logs)...${NC}"
tar --exclude='node_modules' \
    --exclude='.env' \
    --exclude='uploads' \
    --exclude='*.log' \
    -czf "/tmp/${ARCHIVE_NAME}" -C "${BACKEND_LOCAL_DIR}" .

echo -e "${GREEN}✅ Đã tạo gói: /tmp/${ARCHIVE_NAME}${NC}"

# 2. Upload gói lên VPS
echo -e "\n${YELLOW}[2/4] Upload gói nén lên VPS (${VPS_USER}@${VPS_HOST} port ${VPS_PORT})...${NC}"
scp -P "${VPS_PORT}" "/tmp/${ARCHIVE_NAME}" "${VPS_USER}@${VPS_HOST}:/tmp/"

# 3. Giải nén, cài đặt thư viện và khởi động lại PM2 trên VPS
echo -e "\n${YELLOW}[3/4] Cài đặt & Khởi động lại service trên VPS...${NC}"
ssh -p "${VPS_PORT}" -tt "${VPS_USER}@${VPS_HOST}" << EOF
  set -e
  mkdir -p ${REMOTE_PATH}/releases/${TIMESTAMP}
  tar -xzf /tmp/${ARCHIVE_NAME} -C ${REMOTE_PATH}/releases/${TIMESTAMP}
  rm -f /tmp/${ARCHIVE_NAME}

  # Copy file .env nếu đã có từ bản trước
  if [ -f ${REMOTE_PATH}/current/.env ]; then
    cp ${REMOTE_PATH}/current/.env ${REMOTE_PATH}/releases/${TIMESTAMP}/.env
  elif [ ! -f ${REMOTE_PATH}/releases/${TIMESTAMP}/.env ]; then
    cp ${REMOTE_PATH}/releases/${TIMESTAMP}/.env.example ${REMOTE_PATH}/releases/${TIMESTAMP}/.env
  fi

  # Cài đặt production dependencies
  cd ${REMOTE_PATH}/releases/${TIMESTAMP}
  npm install --omit=dev

  # Cập nhật symlink current
  ln -sfn ${REMOTE_PATH}/releases/${TIMESTAMP} ${REMOTE_PATH}/current

  # Restart PM2
  cd ${REMOTE_PATH}/current
  pm2 describe avaroom > /dev/null && pm2 reload ecosystem.config.js --env production || pm2 start ecosystem.config.js --env production
  pm2 save

  # Giữ lại 5 bản backup gần nhất
  cd ${REMOTE_PATH}/releases && ls -1dt * | tail -n +6 | xargs -I {} rm -rf {} || true
EOF

# 4. Kiểm tra Health check
echo -e "\n${YELLOW}[4/4] Kiểm tra trạng thái API...${NC}"
sleep 3
HEALTH_URL="http://${VPS_HOST}/api/health"
echo "Kiểm tra: ${HEALTH_URL}"
if curl -sf "${HEALTH_URL}" > /dev/null; then
  echo -e "\n${GREEN}======================================================${NC}"
  echo -e "${GREEN}   🎉 DEPLOY THÀNH CÔNG RỰC RỠ!                     ${NC}"
  echo -e "${GREEN}   API Endpoint: http://${VPS_HOST}/api/            ${NC}"
  echo -e "${GREEN}======================================================${NC}"
else
  echo -e "${YELLOW}⚠️ API chưa phản hồi ngay hoặc đang khởi động, vui lòng ssh vào VPS kiểm tra: pm2 logs avaroom${NC}"
fi

# Dọn dẹp file nén ở local
rm -f "/tmp/${ARCHIVE_NAME}"
