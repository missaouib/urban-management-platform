const path = require('path');
const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
    entry: './src/bpmn.js',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'dist')
    },
    plugins: [
        new CopyWebpackPlugin([
            {from: '**/*.{html,css}', context: 'src/'},
            {from: '**', to: 'vendor/bpmn-js', context: 'node_modules/bpmn-js/dist/assets/'},
            {from: '**', to: 'vendor/bpmn-js-properties-panel', context: 'node_modules/bpmn-js-properties-panel/dist/assets/'},
            {from: '**', to: 'vendor/diagram-js-minimap', context: 'node_modules/diagram-js-minimap/assets/'},
        ])
    ]
};