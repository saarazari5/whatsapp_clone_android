
const Message = require('../models/Message');
const contactModel = require('../models/Contact');

const messageModel = Message.messageModel;

const getHighestMessageId = async () => {
    try {
        const messages = await Message.find({})
            .sort({ messageId: -1 })
            .limit(1);

        if (messages.length === 0) { return 0; }
        return messages[0].messageId;
    } catch (err) {
        throw err;
    }
}

const postMessage = async (sender, chatId, content) => {
    try {
        const chat = await contactModel.findOne({"chatId": chatId});
        const messageId = await getHighestMessageId + 1;
        const newMessage = new messageModel({
            messageId: messageId,
            sender: sender,
            content: content
        });
        await newMessage.save();

        chat.messages.push(newMessage);
        chat.lastMessage = newMessage;
        await chat.save();
        return {messageId: messageId, id: messageId, sender: newMessage.sender, content: newMessage.content};

    } catch (error) {
        return null;
    }
};

const getMessages = async (chatId) => {
    try {
        const chat = await contactModel.findOne({"chatId": chatId});
        let conversation = [];

        chat.messages.forEach(msg => {
            const message = {
                id: msg.messageId,
                created: msg.created,
                sender: msg.sender,
                content: msg.content
            }

            conversation = [ ...conversation, message];
        });

        return conversation;
        
    } catch (error) {
        console.log(error)
        return null;
    }
}

module.exports = {getMessages, postMessage};