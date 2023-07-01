const path = require('path');

module.exports = {
  mode: "development",
  devtool: "inline-source-map",

  entry: {
	 highlighting: './src/highlighting.js',
  },

  output: {
    filename: 'highlighting.js',
    path: path.resolve(__dirname, 'dist'),
    clean: true,
  },

  experiments: {
    topLevelAwait: true,
  },

};
