const mongoose = require('mongoose');
const Message = require('./Message.js')
const Schema = mongoose.Schema

const messageSchema = Message.messageSchema;

const contactSchema = new Schema({
    chatId: {
        type: Number,
        required: true
    },
    users: [
        {
            username: {
                type: String,
                required: true
            },
            displayName: {
                type: String,
                required: true
            },
            profilePic: {
                type: String,
                required: true
            }
        }
        
    ],
    lastMessage: {
        type: messageSchema,
        default: null
    },
    messages: [
       messageSchema
    ]
});


const contactModel = mongoose.model('Contact', contactSchema, 'Contact');

module.exports = contactModel;