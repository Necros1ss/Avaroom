const fs = require('fs');

const figma = JSON.parse(fs.readFileSync('figma_full.json', 'utf8'));

console.log('Figma Document Name:', figma.name);
console.log('Pages:');

const pages = figma.document.children || [];
const screenInventory = [];

pages.forEach((page, pIdx) => {
  console.log(`\nPage [${pIdx + 1}]: ${page.name} (${(page.children || []).length} top-level nodes)`);
  (page.children || []).forEach((c, cIdx) => {
    if (c.type === 'FRAME' || c.type === 'GROUP' || c.type === 'COMPONENT') {
      const bbox = c.absoluteBoundingBox || {};
      screenInventory.push({
        pageIndex: pIdx,
        pageName: page.name,
        id: c.id,
        name: c.name,
        type: c.type,
        width: Math.round(bbox.width || 0),
        height: Math.round(bbox.height || 0),
        childrenCount: (c.children || []).length
      });
      console.log(`  - [${c.type}] "${c.name}" id=${c.id} (${Math.round(bbox.width || 0)}x${Math.round(bbox.height || 0)})`);
    }
  });
});

fs.writeFileSync('all_screens.json', JSON.stringify(screenInventory, null, 2));
console.log('\nTotal top-level screens/frames identified:', screenInventory.length);
