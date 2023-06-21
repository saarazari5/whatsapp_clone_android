const UserService = require('../services/UserService');


const getAllUsers = async (_, res) => {
    res.json(await UserService.getAllUsers(req, res));
}

const getUserById = async (req, res) => {
    await UserService.getUserById(req, res);
}

const updateUser = async (req, res) => {
    res.json(await UserService.updateUser(req, res));
}

const deleteUser = async (req, res) => {
    res.json(await UserService.deleteUser(req, res));
}

const createUser = async (req, res) => {

    
    // res.json(await UserService.createUser(
    //     req.body.username,
    //     req.body.password,
    //     req.body.displayName,
    //     req.body.profilePic));
    const toSend = await UserService.createUser(
        req.body.username,
        req.body.password,
        req.body.displayName,
        req.body.profilePic);

    res.status(toSend.status).send(toSend.body);
    console.log(res.statusCode);

};




module.exports = { getAllUsers, createUser, getUserById, updateUser, deleteUser };