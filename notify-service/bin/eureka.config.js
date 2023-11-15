require('dotenv').config();

const Eureka = require("eureka-js-client").Eureka;

const eurekaConfig = {
  instance: {
    app: "NOTIFY-SERVICE",
    hostName: process.env.HOST,
    ipAddr: process.env.HOST,
    port: {
      $: process.env.PORT,
      "@enabled": "true",
    },
    vipAddress: "NOTIFY-SERVICE",
    dataCenterInfo: {
      "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
      name: "MyOwn",
    },
  },
  eureka: {
    host: process.env.EUREKA_HOST,
    port: process.env.EUREKA_PORT,
    servicePath: "/eureka/apps",
  },
};

module.exports = new Eureka(eurekaConfig);
