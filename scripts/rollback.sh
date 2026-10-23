#!/bin/bash
# ==============================================================================
# Script Rollback về phiên bản release trước trên VPS
# ==============================================================================

set -euo pipefail

VPS_IP="YOUR_VPS_IP"          # ⚠️ Điền địa chỉ IP của VPS
VPS_USER="deploy"
REMOTE_PATH="/home/deploy/avaroom"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

if [ "$VPS_IP" = "YOUR_VPS_IP" ]; then
  echo -e "${RED}❌ Lỗi: Bạn chưa cấu hình biến VPS_IP trong file scripts/rollback.sh!${NC}"
  exit 1
fi

echo -e "${YELLOW}Đang kết nối tới VPS để lấy danh sách bản phát hành...${NC}"

ssh -tt "${VPS_USER}@${VPS_IP}" << 'EOF'
  cd /home/deploy/avaroom/releases
  PREV_RELEASE=$(ls -1dt * | sed -n '2p')

  if [ -z "$PREV_RELEASE" ]; then
    echo "❌ Không tìm thấy bản phát hành trước đó để rollback!"
    exit 1
  fi

  echo "🔄 Đang rollback về bản: $PREV_RELEASE"
  ln -sfn /home/deploy/avaroom/releases/$PREV_RELEASE /home/deploy/avaroom/current

  cd /home/deploy/avaroom/current
  pm2 reload ecosystem.config.js --env production
  pm2 save
  echo "✅ Rollback thành công về bản $PREV_RELEASE!"
EOF
