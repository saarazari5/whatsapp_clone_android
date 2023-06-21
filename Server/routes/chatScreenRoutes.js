const express = require('express');
const chatController = require('../controllers/ContactController.js');
const messageController = require('../controllers/MessagesController.js');

const auth = require('../middlewares/auth');

const chatsRouter = express.Router();

chatsRouter.route('/')
    .post(auth, chatController.findUser, chatController.createChat)
    .get(auth, chatController.getChats);

chatsRouter.route('/chat')
    .get(auth, chatController.getChat);

chatsRouter.route('/:id/Messages')
    .post(auth, messageController.postMessage)
    .get(auth, messageController.getMessages);

chatsRouter.route('/:id')
    .delete(auth, chatController.deleteChat);

module.exports = chatsRouter;