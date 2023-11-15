require("dotenv").config();

const express = require("express");
const cookieParser = require("cookie-parser");
const logger = require("morgan");
var cors = require("cors");

const router = require("./routes");
const connectDB = require("./db/dbConnect");
const eurekaClient = require("./bin/eureka.config");
const app = express();

app.use(logger("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(cookieParser());

connectDB();

const corsOptions = {
  origin: process.env.GATEWAY_URL,
  optionsSuccessStatus: 200,
};

app.use(cors(corsOptions));

app.use("/api/v1/notify", router);

// Error handler middleware for API
app.use((err, req, res, next) => {
  console.error(err.stack);

  // Send a JSON response with the error details
  res.status(err.status || 500).json({
    error: {
      message: err.message || "Internal Server Error",
    },
  });
});

eurekaClient.start();

eurekaClient.on("started", () => {
  console.log("Eureka client started");
});
eurekaClient.on("started", () => {});

eurekaClient.on("error", (err) => {
  console.error("Error with Eureka client:", err);
});

module.exports = app;
