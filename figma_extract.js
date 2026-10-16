const https = require('https');

function fetchNode(ids) {
  return new Promise((resolve, reject) => {
    const options = {
      hostname: 'api.figma.com',
      path: '/v1/files/DiHZEesg49LjjcnbgmGttP/nodes?ids=' + encodeURIComponent(ids),
      headers: { 'X-Figma-Token': process.env.FIGMA_TOKEN || 'YOUR_FIGMA_TOKEN' }
    };
    https.get(options, res => {
      let data = '';
      res.on('data', c => data += c);
      res.on('end', () => {
        const j = JSON.parse(data);
        if (j.err) reject(j.err);
        else resolve(j);
      });
    }).on('error', reject);
  });
}

async function main() {
  const resp = await fetchNode('251:634');
  const node = resp.nodes['251:634'].document;
  
  function extract(n, depth) {
    const indent = '  '.repeat(depth);
    let info = indent + '[' + n.type + '] ' + n.name;
    if (n.characters) {
      const txt = n.characters.replace(/\n/g, ' ').substring(0, 60);
      info += ' TEXT="' + txt + '"';
    }
    if (n.absoluteBoundingBox) {
      info += ' w=' + Math.round(n.absoluteBoundingBox.width) + ' h=' + Math.round(n.absoluteBoundingBox.height);
    }
    if (n.fills && n.fills[0] && n.fills[0].color) {
      const c = n.fills[0].color;
      const hex = [c.r, c.g, c.b].map(v => Math.round(v * 255).toString(16).padStart(2, '0')).join('');
      info += ' fill=#' + hex;
    }
    if (n.cornerRadius) info += ' radius=' + n.cornerRadius;
    if (n.transitionNodeID) info += ' DEST=' + n.transitionNodeID;
    console.log(info);
    if (n.children && depth < 4) {
      n.children.forEach(c => extract(c, depth + 1));
    }
  }
  extract(node, 0);
}
main().catch(e => console.error('Error:', e));
