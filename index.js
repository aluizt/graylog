let logger = require("gelf-pro");

logger.setConfig({
  fields: {
    app_name: "busca-service",
    facility: "example", 
    owner: "Tom (a cat)",
    os_name: "linux",
    os_version: 10,
    short_message: "LALA",
    full_message: "LALAL",
    level: 1,
    line: 2,
    file: "busca.js",
    version: "1.0"
  },
  // adapterName: 'udp',
  adapterOptions: {
    host: '10.11.102.127',
    // host: '127.0.0.1',
    port: 12201,
  }
});


logger.info(
  'a new msg goes here'
);

// console.log('All done - Verify your Graylog');
// process.exit();
