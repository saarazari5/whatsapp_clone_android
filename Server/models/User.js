const mongoose = require('mongoose');
const messageSchema = require('./Message.js')
const jwt = require('jsonwebtoken');

const Schema = mongoose.Schema;


const userSchema = new Schema({
    username: {
        type: String,
        required: true,
        unique: true
    },
    password: {
        type: String,
        required: true,
    },
    displayName: {
        type: String,
        required: true
    },
    profilePic: {
        type: String,
        required: true
    }
});

userSchema.methods.generateAuthToken = function () {
    return jwt.sign({ _id: this._id }, process.env.TOKEN_SECRET, { expiresIn: 10800 });
}

const User = mongoose.model('users', userSchema);

function validateUser(user) {
    const { username, email, password } = user;
    // Check if all required fields exist
    if (!username || !email || !password) {
        return false;
    }
    // Check username length
    if (username.length < 1 || username.length > 50) {
        return false;
    }
    // Check email length and format
    if (email.length < 5 || !isValidEmail(email)) {
        return false;
    }
    // Check password length
    if (password.length < 8) {
        return false;
    }
    // Validation passed
    return true;
}

// Function to validate email format
function isValidEmail(email) {
    // Regular expression to validate email format
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}


module.exports = User;
module.exports.validateUser = validateUser;
