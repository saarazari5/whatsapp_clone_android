const Message = require('../models/Message');
const contactModel = require('../models/Contact');
const messageModel = Message.messageModel;
const socketMap = require('../socketMap');
const androidConnection = require('../models/androidConnection.js')
const connections = androidConnection.androidConnection
const admin = require('firebase-admin');


const getHighestMessageId = async () => {
    try {
        const messages = await messageModel.find({})
            .sort({ messageId: -1 })
            .limit(1);

        if (messages.length === 0) { return 0; }
        return messages[0].messageId;
    } catch (err) {
        console.log(err)
        throw err;
    }
}

const postMessage = async (sender, chatId, content) => {
    try {
        const chat = await contactModel.findOne({ "chatId": chatId });
        const messageId = await getHighestMessageId() + 1;
        const reciever = chat.users.find(user => user.username != sender.username)
        const newMessage = new messageModel({
            messageId: messageId,
            sender: sender,
            content: content
        });
        await newMessage.save();
        chat.messages.push(newMessage);
        chat.lastMessage = newMessage;
        await chat.save();
        if (connections.has(reciever.username)) {
            const messageToSend = await messageModel.findOne({ "messageId": messageId })
            const fcmToken = connections.get(reciever.username)
            const message = {
                token: fcmToken,
                data: {
                    chatId: chat.chatId.toString(),
                    sender: sender.username,
                    displayName: sender.displayName,
                    content: content,
                    created: messageToSend.created.toString()
                },
            };

            console.log(" message To Send is : ", message)

            admin.messaging().send(message).then((response) => {
                console.log('Notification sent successfully:', response);
            }).catch(error => {
                console.error("Error: ", error)
            })
        }
        // Construct a data object of the message
        const data = { room: chat.chatId, msg: "messageToSend" };
        // Loop through the socketsMap
        Object.entries(socketMap).forEach(([socketId, socketData]) => {
            // Check if the username matches the desired value
            if (socketData.username === reciever.username) {
                // Emit the "receive_message" event to the socket
                socketData.socket.emit("receive_message", data);
            }
        });

        return {
            messageId: messageId,
            id: messageId,
            sender: newMessage.sender,
            created: newMessage.created,
            content: newMessage.content
        };

    } catch (error) {
        console.log(error);
        return null;
    }
};

const getMessages = async (chatId) => {
    try {
        const chat = await contactModel.findOne({ "chatId": chatId });
        let conversation = [];

        chat.messages.forEach(msg => {
            const message = {
                id: msg.messageId,
                created: msg.created,
                sender: msg.sender,
                content: msg.content
            }

            conversation = [...conversation, message];
        });

        return conversation;

    } catch (error) {
        console.log(error)
        return null;
    }
}

module.exports = { getMessages, postMessage };