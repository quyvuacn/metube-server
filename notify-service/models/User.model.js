const mongoose = require("mongoose");

const userSchema = new mongoose.Schema(
  {
    email: { type: String },
    fmcTokens: [
      {
        type: mongoose.Schema.Types.ObjectId,
        ref: "FMCToken",
        require: true,
        unique: true,
      },
    ],
  },
  {
    timestamps: true,
  },
);

const User = mongoose.model("User", userSchema);
module.exports = User;
