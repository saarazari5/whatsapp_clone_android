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
    console.log("heeeeeeeeyyyyyy");// test

    res.json(await UserService.createUser(
        req.body.username,
        req.body.password,
        req.body.displayName,
        req.body.profilePic));

    console.log(res.status);

};


module.exports = { getAllUsers, createUser, getUserById, updateUser, deleteUser };