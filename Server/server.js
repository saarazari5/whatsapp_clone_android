const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const cors = require('cors');
const appEnv = require('custom-env');
const http = require('http');
const { Server } = require('socket.io');

const userRoutes = require('./routes/userRoutes');
const loginRoutes = require('./routes/loginRoutes.js');
const chatScreenRoutes = require('./routes/chatScreenRoutes.js');
const registerRoutes = require('./routes/registerRoutes.js');
const chatRoutes = require('./routes/chatRoutes.js');
const notExistsRoutes = require('./routes/notExistsRoutes.js');

// Initialize Express APP
const app = express();
app.set('view engine', 'ejs');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.json());
app.use(express.static('public'));
app.use('/register', express.static('public'));
// app.use('/login', express.static('public'));
app.use('/chat', express.static('public'));
// app.use('/*', express.static('public'));

// app.use(express.static('public/build'));
app.use(cors());
appEnv.env(process.env.NODE_ENV, './config');

// API routes
app.use('/api/Users', userRoutes);
app.use('/api/Token', loginRoutes);
app.use('/api/Chats', chatScreenRoutes);
// app.use('/api/chatScreen', chatScreenRoutes);
// app.use('/register', registerRoutes); // do we need this?
// app.use('/chat', chatRoutes);         // do we need this?
// app.use('/*', notExistsRoutes);       // do we need this? 

const server = http.createServer(app);
const io = new Server(server, {
    cors: {
        origin: "http://localhost:3000",
        methods: ["GET", "POST", "DELETE"],
    },
});

io.on("connection", (socket) => {
    console.log(`User Connected: ${socket.id}`);

    socket.on("join_room", (data) => {
        socket.join(data);
        console.log(`User with ID: ${socket.id} joined room: ${data}`);
    });

    socket.on("send_message", (data) => {
        socket.to(data.room).emit("receive_message", data);
    });

    socket.on("create_contact", (data) => {
        socket.broadcast.emit("add_new_contact", data);
    });

    socket.on("disconnect", () => {
        console.log("User Disconnected", socket.id);
    });

    socket.on("delete_contact", (data) => {
        socket.to(data.room).emit("recived_delete", data);
        socket.leave(data.room);
    });
});

// server.listen(5000, () => {
//     console.log("SERVER RUNNING");
// });


// Connect to MongoDB
mongoose.connect(process.env.CONNECTION_STRING, {
    useNewUrlParser: true,
    useUnifiedTopology: true
})
    .then(() => console.log('Connected to MongoDB...'))
    .catch((error) => console.error('Error connecting to MongoDB...', error));

// Connect the application to some PORT
const PORT = process.env.PORT || 5000;
server.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
