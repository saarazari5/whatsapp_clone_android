const express = require('express');
const NotExistController = require('../controllers/NotExistController.js');

const router = express.Router();

router.get('/', NotExistController.getNotFoundPage);

module.exports = router;