const fs = require('fs');
const figma = JSON.parse(fs.readFileSync('figma_full.json', 'utf8'));

const nodeMap = {};
function mapNodes(node) {
  if (node.id) nodeMap[node.id] = node;
  if (node.children) node.children.forEach(mapNodes);
}
mapNodes(figma.document);

function dumpDeep(id, maxDepth = 5) {
  const node = nodeMap[id];
  if (!node) return;
  function walk(n, depth = 0) {
    if (depth > maxDepth) return;
    const indent = '  '.repeat(depth);
    let extra = '';
    if (n.characters) extra += ' txt=' + JSON.stringify(n.characters.trim().substring(0, 50));
    if (n.transitionNodeID) extra += ' [GOTO: ' + (nodeMap[n.transitionNodeID]?.name || n.transitionNodeID) + ']';
    console.log(indent + '[' + n.type + '] ' + n.name + extra);
    if (n.children) n.children.forEach(c => walk(c, depth + 1));
  }
  walk(node, 0);
}

console.log('=== HEADER & MAIN DETAILS ===');
dumpDeep('251:634', 5);
