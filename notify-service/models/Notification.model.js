const mongoose = require("mongoose");

const notificationSchema = new mongoose.Schema(
  {
    users: [
      {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
        require: true,
      },
    ],
    data: {
      type: mongoose.Schema.Types.Mixed,
      require: true,
    },
    body: {
      type: String,
      require: true,
    },
    title: {
      type: String,
      require: true,
    },
  },
  {
    timestamps: true,
  },
);

const Notification = mongoose.model("Notification", notificationSchema);
module.exports = Notification;
