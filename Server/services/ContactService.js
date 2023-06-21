const Contact = require("../models/Contact.js");
const User = require('../models/User.js');
const Message = require('../models/Message.js');
const { messageModel } = require('../models/Message.js');

const UserById = async (id) => {
    const user = await User.findOne({ _id: id });
    return user;
}

const shouldCreateChat = async (user1, user2) => {
    const chats = await Contact.find();
    for (const chat of chats) {
        if (
            (chat.users[0].username === user1.username || chat.users[1].username === user1.username)
            &&
            (chat.users[0].username === user2.username || chat.users[1].username === user2.username)
        ) {
            return false;
        }
    }
    return true;
}

const getHighestChatId = async () => {
    try {
        const chats = await Contact.find({})
            .sort({ chatId: -1 })
            .limit(1);

        if (chats.length === 0) { return 0; }
        return chats[0].chatId;
    } catch (err) {
        throw err;
    }
}

const createChat = async (currentUser, newContact) => {
    try {
        const result = await shouldCreateChat(currentUser, newContact);
        console.log(result)
        if (!result) { return false }
        const chatId = await getHighestChatId() + 1;
        const users = [
            {
                username: currentUser.username,
                displayName: currentUser.displayName,
                profilePic: currentUser.profilePic
            },
            {
                username: newContact.username,
                displayName: newContact.displayName,
                profilePic: newContact.profilePic
            }
        ];
        const messages = [];

        const newChat = new Contact({
            chatId,
            users,
            messages
        });

        await newChat.save();
        return chatId;

    } catch (error) {
        console.log(error);
        return false;
    }

};

const getChats = async (username) => {
    let allChats = [];
    try {
        const results = await Contact.find({ "users.username": username });

        results.forEach(result => {
            const chat = {
                id: result.chatId,
                users: result.users.map(user => ({
                    username: user.username,
                    displayName: user.displayName,
                    profilePic: user.profilePic
                })),
                lastMessage: result.lastMessage
            }
            allChats = [...allChats, chat];
        });
        return allChats;
    } catch (error) {
        return null;
    }
}

const getChat = async (user1, user2) => {
    try {
        const chatQuery = {
            $and: [
                { 'users.username': user1 },
                { 'users.username': user2 }
            ]
        };

        const chat = await Contact.find(chatQuery);
        if (!chat) {
            throw new Error('Falied to find Chat');
        }
        return chat;
    } catch (error) {
        console.error('Error:', error);
        return null;
    }
}

const deleteChat = async (chatId) => {
    try {
        console.log("chatId: ", chatId) //test
        const targetChat = await Contact.findOne({ "chatId": chatId });
        console.log("targetChat: ", targetChat)
        for (const message of targetChat.messages) {
            const msg = await messageModel.findOne({ "messageId": message.messageId });
            await msg.deleteOne();
        }

        await targetChat.deleteOne();
        return true;
    } catch {
        return false
    }
}
const chatsService = {
    createChat,
    getChats,
    getChat,
    deleteChat,
    UserById,
    isChatExists: shouldCreateChat
};

module.exports = chatsService;