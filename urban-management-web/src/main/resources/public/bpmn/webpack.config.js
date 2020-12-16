const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
// const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
    mode: 'development',
    devtool: 'inline-source-map',
    entry: './src/bpmn.js',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'dist')
    },
    module: {
        rules: [
            {
                test: /\.css$/i,
                use: ['style-loader', 'css-loader'],
            },
            {
                test: /\.(png|svg|jpg|jpeg|gif)$/i,
                type: 'asset/resource',
            },
            {
                test: /\.(woff|woff2|eot|ttf|otf)$/i,
                type: 'asset/resource',
            },
        ],
    },
    devServer: {
        contentBase: './dist',
    },
    // plugins: [
    //     new CopyWebpackPlugin([
    //         {from: '**/*.{html,css}', context: 'src/'},
    //         {from: '**', to: 'vendor/bpmn-js', context: 'node_modules/bpmn-js/dist/assets/'},
    //         {from: '**', to: 'vendor/bpmn-js-properties-panel', context: 'node_modules/bpmn-js-properties-panel/dist/assets/'},
    //         {from: '**', to: 'vendor/diagram-js-minimap', context: 'node_modules/diagram-js-minimap/assets/'},
    //     ])
    // ]
    plugins: [
        new HtmlWebpackPlugin({
            title: 'Output Management',
        }),
    ]
};