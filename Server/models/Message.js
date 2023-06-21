const mongoose = require('mongoose');
const schema = mongoose.Schema;

const messageSchema = new schema({
    messageId: {
        type: Number,
        required: true
    },
    created: {
        type: Date,
        default: Date.now
    }
    ,
    sender: {
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
    ,
    content: {
        type: String,
        required: true
    }
});

const messageModel = mongoose.model('Message', messageSchema, 'Messages');

module.exports = { messageModel, messageSchema };