const mongoose = require("mongoose");

const connectDB = async () => {
  try {
    await mongoose.connect(
      "mongodb://" +
        process.env.DB_HOST +
        ":" +
        process.env.DB_PORT +
        "/" +
        process.env.DB_NAME,
      {
        useNewUrlParser: true,
        useUnifiedTopology: true,
      }
    );
    console.log("successfully connected to the server");
  } catch (error) {
    console.log(error);
    process.exit(1);
  }
};
module.exports = connectDB;
