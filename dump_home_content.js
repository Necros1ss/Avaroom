const fs = require('fs');
const figma = JSON.parse(fs.readFileSync('figma_full.json', 'utf8'));

const nodeMap = {};
function mapNodes(node) {
  if (node.id) nodeMap[node.id] = node;
  if (node.children) node.children.forEach(mapNodes);
}
mapNodes(figma.document);

function dumpAllTextsAndButtons(node) {
  if (!node) return;
  if (node.characters) {
    console.log('[TEXT]', node.name, '->', JSON.stringify(node.characters.replace(/\n/g, ' ')));
  }
  if (node.transitionNodeID) {
    console.log('[NAV BUTTON]', node.name, '-> GOTO:', (nodeMap[node.transitionNodeID]?.name || node.transitionNodeID));
  }
  if (node.children) node.children.forEach(dumpAllTextsAndButtons);
}

console.log('--- Top Greeting & Search ---');
dumpAllTextsAndButtons(nodeMap['251:634'].children.find(c => c.name === 'Main'));
console.log('--- Header ---');
dumpAllTextsAndButtons(nodeMap['251:634'].children.find(c => c.name === 'Header'));
