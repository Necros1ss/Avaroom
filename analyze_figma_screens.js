const fs = require('fs');

const figma = JSON.parse(fs.readFileSync('figma_full.json', 'utf8'));

const nodeMap = {};
function mapNodes(node) {
  if (node.id) nodeMap[node.id] = node;
  if (node.children) node.children.forEach(mapNodes);
}
mapNodes(figma.document);

// Map of unique screens
// Group into 3 main domains:
// 1. Auth & Onboarding & eKYC
// 2. Student (Sinh viên)
// 3. Landlord (Chủ trọ)

const screens = [];

function analyzeNode(node, pageName) {
  if (node.type !== 'FRAME') return;
  const bbox = node.absoluteBoundingBox || {};
  if (bbox.width < 250 || bbox.height < 300) return; // skip tiny sub-components
  
  const texts = [];
  const buttons = [];
  const interactions = [];

  function walk(c) {
    if (c.characters && c.characters.trim()) {
      texts.push(c.characters.trim().replace(/\n/g, ' '));
    }
    if (c.name && (c.name.toLowerCase().includes('button') || c.name.toLowerCase().includes('cta') || c.name.toLowerCase().includes('link'))) {
      buttons.push(c.name);
    }
    if (c.transitionNodeID) {
      interactions.push({
        source: c.name,
        targetId: c.transitionNodeID,
        targetName: nodeMap[c.transitionNodeID]?.name || c.transitionNodeID
      });
    }
    if (c.children) c.children.forEach(walk);
  }
  walk(node);

  screens.push({
    id: node.id,
    page: pageName,
    name: node.name,
    width: Math.round(bbox.width),
    height: Math.round(bbox.height),
    textSample: texts.slice(0, 10),
    buttonCount: buttons.length,
    interactions: interactions
  });
}

(figma.document.children || []).forEach(p => {
  (p.children || []).forEach(c => {
    if (c.type === 'FRAME') analyzeNode(c, p.name);
    else if (c.type === 'GROUP') {
      (c.children || []).forEach(gc => {
        if (gc.type === 'FRAME') analyzeNode(gc, p.name);
      });
    }
  });
});

console.log('Analyzed unique screens:', screens.length);
fs.writeFileSync('analyzed_screens.json', JSON.stringify(screens, null, 2));

// Print categorized list
const uniqueNames = new Set();
screens.forEach(s => uniqueNames.add(s.name));
console.log('Unique Screen Names (' + uniqueNames.size + '):');
Array.from(uniqueNames).forEach((n, idx) => console.log(`${idx + 1}. ${n}`));
