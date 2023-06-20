const chatsService = require("../services/ContactService.js");
const userService = require("../services/UserService.js");


const findUser = async (req, res, next) => {
    try {
        const newContact = await userService.getUser(req.body.username);
        if (!newContact) {
            throw new Error();
        } else {

            const currentUser = await chatsService.UserById(req.user._id);
            req.body = { currentUser, newContact };
            return next();
        }
    } catch (error) {
        console.log(error);
        res.status(404).send(`${req.body.newContact} was not found`);
        return false;
    }
};

const createChat = async (req, res) => {
    var { username, displayName, profilePic } = req.body.currentUser;
    const currentUser = { username, displayName, profilePic };
    var { username, displayName, profilePic } = req.body.newContact;
    const newContact = { username, displayName, profilePic };
    const result = await chatsService.createChat(currentUser, newContact);
    if (!result) {
        res.status(503).send('Database Error');
    } else {
        const id = result;
        const user = newContact;
        res.status(200).json({ id, user });
    }
};

const getChats = async (req, res) => {
    const currentUser = await chatsService.UserById(req.user._id);

    try {
        const allChats = await chatsService.getChats(currentUser.username);
        if (!allChats) {
            throw new Error();
        } else {
            res.status(200).json(allChats);
        }
    } catch (error) {
        res.status(503).send('Database Error');
    }
};

const getChat = async (req, res) => {
    try {
        const user1 = req.query.user1;
        const user2 = req.query.user2;
        const chat = await chatsService.getChat(user1, user2);
        if (!chat) {
            throw new Error('Error finding given chat');
        } else {
            res.status(200).json(chat);
        }
    } catch (error) {
        res.status(503).send('Database Error');
    }
};


const deleteChat = async (req, res) => {
    console.log("req: ", req.params.id)
    const result = await chatsService.deleteChat(req.params.id);
    console.log(result);
    if (result) {
        res.status(200).send('Deleted succesfully')
    } else {
        res.status(404).send('Not Found');
    }
};

module.exports = { getChats, findUser, createChat, deleteChat, getChat };