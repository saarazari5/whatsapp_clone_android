const registerService = require('../services/Register');

const createUser = async (req, res) => {
    res.jason(await registerService.createUser(req.body.username,
        req.body.password,
        req.body.displayName,
        req.body.profilePic));
    

};

module.exports = { createUser };