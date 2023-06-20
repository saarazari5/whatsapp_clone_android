const express = require('express');
const LoginController = require('../controllers/LoginController.js');

const router = express.Router();

router.get('/', LoginController.getLogin);
router.post('/', LoginController.handleLogin);
router.get('/logout', LoginController.handleLogout);
router.post('/forgot', LoginController.handleForgot);

module.exports = router;