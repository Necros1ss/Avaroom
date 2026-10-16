const fs = require('fs');
const data = JSON.parse(fs.readFileSync('figma_full.json', 'utf8'));

function getNodeById(node, id) {
    if (node.id === id) return node;
    if (node.children) {
        for (const c of node.children) {
            const res = getNodeById(c, id);
            if (res) return res;
        }
    }
    return null;
}

const frame = getNodeById(data.document, '251:1741');

function findNodeByName(node, name) {
    if (node.name === name) return node;
    if (node.children) {
        for (const c of node.children) {
            const res = findNodeByName(c, name);
            if (res) return res;
        }
    }
    return null;
}

const mainContent = findNodeByName(frame, 'Main Details Content');

function dump(node, depth = 0) {
    const pad = '  '.repeat(depth);
    let fill = '';
    if (node.fills && node.fills.length > 0) {
        const f = node.fills[0];
        if (f.type === 'SOLID') {
            const r = Math.round((f.color ? f.color.r : 0) * 255);
            const g = Math.round((f.color ? f.color.g : 0) * 255);
            const b = Math.round((f.color ? f.color.b : 0) * 255);
            fill = ` fill:rgb(${r},${g},${b})`;
        } else if (f.type === 'IMAGE') {
            fill = ' [IMAGE]';
        }
    }
    let radius = node.cornerRadius ? ` r:${node.cornerRadius}` : '';
    let box = node.absoluteBoundingBox ? ` (${Math.round(node.absoluteBoundingBox.width)}x${Math.round(node.absoluteBoundingBox.height)})` : '';
    let txt = node.characters ? ` text=${JSON.stringify(node.characters)}` : '';
    console.log(`${pad}${node.name} [${node.type}]${box}${fill}${radius}${txt}`);
    if (node.children && depth < 3) {
        for (const c of node.children) {
            dump(c, depth + 1);
        }
    }
}

dump(mainContent);
