module.exports = {
  entry: './webpack.js',
  output: {
		path: "./resources/public/js/lib/",
		filename: "webpacked.js"
//		chunkFilename: "[hash]/js/[id].js",
//		hotUpdateMainFilename: "[hash]/update.json",
//		hotUpdateChunkFilename: "[hash]/js/[id].update.js"
	},
    externals: {
        "react": "React",
        "react-dom": "ReactDOM"
    }
};
