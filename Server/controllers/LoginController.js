const LoginService = require('../services/LoginService');

const getLogin = async (_, res) => {
    res.json(await LoginService.getLogin());
}

const handleLogin = async (req, res) => {
    await LoginService.handleLogin(req, res);
}

const handleLogout = async (req, res) => {
    res.json(await LoginService.handleLogout(req, res));
}

const handleForgot = async (req, res) => {
    res.json(await LoginService.handleForgot(req, res));
}

module.exports = { getLogin, handleLogin, handleLogout, handleForgot };