const jwt = require('jsonwebtoken');

module.exports = async function (req, res, next) {
    const authHeaderToken = req.header('Authorization');
    // check if token was provided
    if (!authHeaderToken) {
        console.log('Access denied. No token provided.');
        return res.status(401).json({ messgae: 'Access denied: No token provided' });
    }
    // Token provided - verify it 
    try {
        const token = authHeaderToken.split(' ')[1];
        const decoded = await jwt.verify(token, process.env.TOKEN_SECRET);
        req.user = decoded;
        next();
    } catch (e) {
        // Invalid Token provided
        console.log(e.message);
        return res.status(403).json({ messgae: 'Access denied: Invalid Token' });
    }
}