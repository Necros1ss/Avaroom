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

function dumpDetails(node, depth = 0) {
    const pad = '  '.repeat(depth);
    let fill = '';
    if (node.fills && node.fills.length > 0) {
        const f = node.fills[0];
        if (f.type === 'SOLID') {
            const r = Math.round((f.color ? f.color.r : 0) * 255);
            const g = Math.round((f.color ? f.color.g : 0) * 255);
            const b = Math.round((f.color ? f.color.b : 0) * 255);
            fill = ` rgb(${r},${g},${b})`;
        } else if (f.type === 'IMAGE') {
            fill = ' [IMAGE]';
        }
    }
    let stroke = '';
    if (node.strokes && node.strokes.length > 0) {
        const s = node.strokes[0];
        if (s.type === 'SOLID' && s.color) {
            stroke = ` stroke:rgb(${Math.round(s.color.r*255)},${Math.round(s.color.g*255)},${Math.round(s.color.b*255)})`;
        }
    }
    let radius = node.cornerRadius ? ` r:${node.cornerRadius}` : '';
    let box = node.absoluteBoundingBox ? ` (${Math.round(node.absoluteBoundingBox.width)}x${Math.round(node.absoluteBoundingBox.height)})` : '';
    let txt = node.characters ? ` text=${JSON.stringify(node.characters)}` : '';
    console.log(`${pad}${node.name} [${node.type}]${box}${fill}${stroke}${radius}${txt}`);
    if (node.children && depth < 4) {
        for (const c of node.children) {
            dumpDetails(c, depth + 1);
        }
    }
}

dumpDetails(frame);
