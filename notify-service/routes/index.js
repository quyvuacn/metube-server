const express = require("express");
const fmc = require("../service/fmc.service");
const router = express.Router();
const User = require("../models/User.model");
const FMCToken = require("../models/FMCToken.model");
const Notification = require("../models/Notification.model");
const { ObjectId } = require("mongodb");

router.post("/register-user", async function (req, res) {
  const { email } = req.body;

  try {
    let user = await User.findOne({ email });
    if (!user) {
      user = new User({ email });
      await user.save();
    }
    res.json(user);
  } catch (error) {
    res.status(500).json({ error: "Không thể khởi tạo user" });
  }
});

router.post("/send-notification", async function (req, res) {
  const { emails, notification, data } = req.body;
  const users = await User.find({ email: { $in: emails } })
    .populate("fmcTokens")
    .exec();

  const tokens = users
    .map((user) => user.fmcTokens)
    .flat()
    .map((fmc) => fmc.token)
    .filter((token) => !!token);
    console.log("token:", tokens);
    console.log("user:", users.fmctokens);

  try {
    const newNotification = await Notification.create({
      users,
      data,
      ...notification,
    });

    data._id = newNotification.id;
    console.log(data);

    const response = await fmc.sendMulticast(notification, tokens, data);
    res.json(response);
  } catch (error) {
    console.log(error);
    res.status(500).json({ error });
  }
});

router.get("/notifications", async function (req, res) {
  try {
    const { email } = req.query;
    const { fmctoken } = req.headers;

    console.log("headers", req.headers)

    const user = await User.findOneAndUpdate(
      { email },
      {
        $setOnInsert: { email },
      },
      { upsert: true, new: true },
    );

    const existingToken = await FMCToken.findOne({ token: fmctoken });

    if (!existingToken) {
      const newFMCToken = new FMCToken({ token: fmctoken });
      user.fmcTokens.push(newFMCToken);
      await newFMCToken.save();
      await user.save();
    }

    const notifications = await Notification.find({ users: user._id })
      .select("data body title createdAt updatedAt")
      .exec();

    res.json(notifications);
  } catch (error) {
    console.log(error);
    res.status(500);
  }
});

module.exports = router;
