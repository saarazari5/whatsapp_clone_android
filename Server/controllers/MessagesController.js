const messagesService = require('../services/MessagesService');
const chatsService = require('../services/ContactService');

const postMessage = async (req, res) => {
    const currentUser = await chatsService.UserById(req.user._id);
    const sender = {
        username: currentUser.username ,
        displayName: currentUser.displayName,
        profilePic: currentUser.profilePic
    };

    const chatId = req.params.id;
    const msgContent = req.body.msg;

    const result = await messagesService.postMessage(sender, chatId, msgContent);
    if (!result) {
        res.status(503).send('Database Error');
    } else {
        res.status(200).json(result);
    }
};

const getMessages = async (req, res) => {
    const chatId = req.params.id;
    const conversation = await messagesService.getMessages(chatId);

    if (!conversation) {
        res.status(503).send('Database Error');
    } else {
        res.status(200).json(conversation);
    }
}


module.exports = {getMessages, postMessage}