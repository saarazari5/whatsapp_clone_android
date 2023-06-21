const express = require('express');
const UserController = require('../controllers/UserController');
const auth = require('../middlewares/auth');

const router = express.Router();

router.get('/', auth, UserController.getAllUsers);
router.post('/', UserController.createUser);
router.get('/:id', auth, UserController.getUserById);
router.put('/:id', auth, UserController.updateUser);
router.delete('/:id', auth, UserController.deleteUser);

module.exports = router;
