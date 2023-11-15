const mongoose = require("mongoose");

const fmcTokenSchema = new mongoose.Schema(
  {
    token: { type: String, require: true, unique: true },
  },
  {
    timestamps: true,
  },
);

const FMCToken = mongoose.model("FMCToken", fmcTokenSchema);
module.exports = FMCToken;
