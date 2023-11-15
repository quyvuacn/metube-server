const admin = require("firebase-admin");

const serviceAccount = require("./config/metube-fmc.json");
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const fmc = {
  async sendMulticast(notification, registrationTokens, data = {}) {
    try {
      const res = await admin.messaging().sendMulticast({
        tokens: registrationTokens,
        data,
        notification,
      });
      return res;
    } catch (error) {
      throw error;
    }
  },
};

module.exports = fmc;
