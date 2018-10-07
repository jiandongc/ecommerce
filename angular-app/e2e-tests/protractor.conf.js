exports.config = {
  allScriptsTimeout: 11000,

  specs: [
    '*spec.js'
  ],

  capabilities: {
    'browserName': 'chrome'
  },

  baseUrl: 'http://localhost:8000/',

  framework: 'jasmine2',

  jasmineNodeOpts: {
    defaultTimeoutInterval: 30000
  },

  rootElement: 'div#store'
};
