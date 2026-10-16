const fs = require('fs');
const figma = JSON.parse(fs.readFileSync('figma_full.json', 'utf8'));

const nodeMap = {};
function mapNodes(node) {
  if (node.id) nodeMap[node.id] = node;
  if (node.children) node.children.forEach(mapNodes);
}
mapNodes(figma.document);

function dumpTextsAndNav(id) {
  const node = nodeMap[id];
  if (!node) return;
  console.log('=== SCREEN:', node.name, '(' + id + ') ===');
  function walk(n) {
    if (n.characters) console.log('  [TEXT]', n.name, '->', JSON.stringify(n.characters.replace(/\n/g, ' ')));
    if (n.transitionNodeID) console.log('  [NAV]', n.name, '==>', (nodeMap[n.transitionNodeID]?.name || n.transitionNodeID));
    if (n.children) n.children.forEach(walk);
  }
  walk(node);
}

dumpTextsAndNav('169:453'); // Chi tiết phòng trọ
dumpTextsAndNav('169:801'); // Đặt lịch xem phòng
dumpTextsAndNav('169:1062'); // Bộ lọc tìm kiếm
