const express = require('express');
const router = express.Router();
const path = require('path');

router.get('/*', (_, res) => {
    res.sendFile(path.join(__dirname, '../public/build/index.html'));
});

module.exports = router;