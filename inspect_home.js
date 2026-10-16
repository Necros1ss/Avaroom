const fs = require('fs');
const figma = JSON.parse(fs.readFileSync('figma_full.json', 'utf8'));

const nodeMap = {};
function mapNodes(node) {
  if (node.id) nodeMap[node.id] = node;
  if (node.children) node.children.forEach(mapNodes);
}
mapNodes(figma.document);

function inspectFrame(id, maxDepth = 3) {
  const node = nodeMap[id];
  if (!node) return console.log('Node not found:', id);
  console.log('=== FRAME:', node.name, '(' + id + ') ===');
  function dump(n, depth = 0) {
    if (depth > maxDepth) return;
    const indent = '  '.repeat(depth);
    let str = indent + '[' + n.type + '] ' + n.name;
    if (n.characters) str += ' -> ' + JSON.stringify(n.characters.replace(/\n/g, ' '));
    if (n.transitionNodeID) str += ' [PROTOTYPE -> ' + (nodeMap[n.transitionNodeID]?.name || n.transitionNodeID) + ']';
    console.log(str);
    if (n.children) n.children.forEach(c => dump(c, depth + 1));
  }
  dump(node, 0);
}

inspectFrame('251:634', 3); // Trang chu sinh vien
