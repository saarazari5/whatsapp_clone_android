const androidConnection = new Map();

const admin = require('firebase-admin');

const serviceAccount = require('../whatsappcloneandroid-74e5e-firebase-adminsdk-rodxt-edceba7f6b.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
  });

function addFcmToken(username, fcmToken) {
    if(fcmToken === "") {
        if(androidConnection.has(username)){
            androidConnection.delete(username)
            return;
        }
    }
  androidConnection.set(username,fcmToken);
}


module.exports = { androidConnection, addFcmToken};