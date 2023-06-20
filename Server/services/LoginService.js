const User = require('../models/User.js');
const nodemailer = require('nodemailer');

const getLogin = async (_, res) => {
    try {
        return res.status(200).json({ message: 'Welcome to the Login page' });
    } catch (error) {
        res.status(500).json({ message: 'Error retrieving login page' });
    }
}

const handleLogin = async (req, res) => {
    try {
        // check if the user request is valid
        if (!validateLogin(req.body)) {
            res.status(400).json({ message: 'Invalid inputs' });
            return;
        }
        // check if user exits
        let user = await User.findOne({ username: req.body.username });
        if (!user) {
            res.status(400).json({ message: 'Incorrect Username Or Password' });
            return;
        }
        // check if the inserted password is the correct password of the user
        if (req.body.password != user.password) {
            res.status(400).json({ message: 'Incorrect Username Or Password' });
            return;
        }
        // create a jwt for the login user
        const token = await user.generateAuthToken();
        // send Ok status meaning successfull connect
        res.status(200).json({ authorization: token, message: 'Login successful' });
        console.log(`${user.username} has connected`);
    } catch (e) {
        console.log(`An Error has occurred: ${e.message}`);
        res.status(500).json({ message: 'Error to login' });
    }
}

const handleLogout = async (req, res) => {
    try {
        const user = req.user;
        user.isConnected = false;
        res.setHeader('Authorization', null);
        console.log('token removed');
    } catch (e) {
        console.log(`An Error has occurred: ${e.message}`);
        res.status(500).json({ message: 'Error to logout' });
    }
}

const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'chatapp707@gmail.com',
        pass: 'chatapp12345',
    }
});

const sendEmail = (to, subject, text) => {
    const mailOptions = {
        from: 'chatapp707@gmail.com',
        to,
        subject,
        text
    };

    transporter.sendMail(mailOptions, (error, info) => {
        if (error) {
            console.log('Error sending email:', error);
        } else {
            console.log('Email sent:', info.response);
        }
    });
};

const handleForgot = async (req, res) => {
    try {
        // check if the user request is valid
        if (!validateForgot(req.body)) {
            res.status(400).json({ message: 'Invalid inputs' });
            return;
        }
        // check if user exits 
        let user = await User.findOne({ username: req.body.username });
        if (!user) {
            res.status(400).json({ message: 'Username was not fround' });
            return;
        }
        // Set a new password for the user
        const randomPassword = generateRandomPassword(10);
        // Save the new password to the user
        user.password = randomPassword;
        await user.save();
        // Email the new password to the user
        sendEmail(user.username, 'ChatApp Password Reset', `This is your new password: ${randomPassword}`);
        // Return OK status
        res.status(200).json({ message: 'New password was sent to the username' });
    } catch (e) {
        console.log(`An Error has occurred: ${e.message}`);
        res.status(500).json({ message: 'Error to reset password' });
    }
}

function validateLogin(req) {
    // Check if the required fields exist
    if (!req.username || !req.password) {
        return false;
    }
    // Check if the username and password are strings
    if (typeof req.username !== 'string' || typeof req.password !== 'string') {
        return false;
    }
    // Check the length of the username and password
    if (req.username.length < 1 || req.password.length < 8) {
        return false;
    }
    // Return true if the login is valid
    return true;
}

function validateForgot(req) {
    // Check if username provided and if the username is string
    if (!req.username || typeof req.username !== 'string') {
        return false;
    }
    // Check the length of the username
    if (req.username.length < 1) {
        return false;
    }
    // Return true if the login is valid
    return true;
}

function generateRandomPassword(length) {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+';
    let password = '';

    for (let i = 0; i < length; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length);
        password += characters.charAt(randomIndex);
    }

    return password;
}

module.exports = { getLogin, handleLogin, handleLogout, handleForgot };