const express = require('express');
const http = require('http');
const path = require('path');
const formatMessage = require('./src/main/webapp/message');
const {userJoin, getCurrentUser, userLeave, getRoomUsers} = require('./src/main/webapp/user');
const socketio = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = socketio(server);

//Set static folder
app.use(express.static(path.join(__dirname, './src/main/webapp')));

const botName = 'Bot Name';

// RUn when client connects
io.on('connection', socket => {
    socket.on('joinRoom', ({username, room}) => {
        const user = userJoin(socket.id, username, room);
        socket.join(user.room);
        socket.emit('message', formatMessage(botName, 'welcome to chatcord'));

        //Broadcast when a user connects
        socket.broadcast.to(user.room).emit('message', formatMessage(botName, `${user.username} has joined the chat`));

        //Send users and room info
        io.to(user.room).emit('roomUsers', {
            room: user.room, 
            users: getRoomUsers(user.room)
        })
    })

    //Listen for chatMessage 
    socket.on('chatMessage', msg => {
        const user = getCurrentUser(socket.id);

        io.to(user.room).emit('message', formatMessage(user.username, msg));
    });

    //Runs when client disconnects
    socket.on('disconnect', () => {
        const user = userLeave(socket.id);
        if (user) {
            io.emit('message', formatMessage(botName, `${user.username} has left the chat`)); 

            //Send users and room info
            io.to(user.room).emit('roomUsers', {
                room: user.room, 
                users: getRoomUsers(user.room)
            })
        }
    });
});

const PORT = 8080;

server.listen(PORT, () => console.log(`Server running on port ${PORT}`));