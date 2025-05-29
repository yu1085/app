const express = require('express');
const app = express();
const http = require('http').createServer(app);
const io = require('socket.io')(http, {
    cors: {
        origin: "*",
        methods: ["GET", "POST"]
    }
});

const rooms = new Map();

io.on('connection', (socket) => {
    console.log('User connected:', socket.id);

    socket.on('create-room', (roomId) => {
        rooms.set(roomId, socket.id);
        socket.join(roomId);
        socket.emit('room-created', roomId);
    });

    socket.on('join-room', (roomId) => {
        const roomCreator = rooms.get(roomId);
        if (roomCreator) {
            socket.join(roomId);
            socket.to(roomId).emit('user-joined', socket.id);
        } else {
            socket.emit('room-not-found');
        }
    });

    socket.on('offer', (data) => {
        socket.to(data.roomId).emit('offer', {
            sdp: data.sdp,
            from: socket.id
        });
    });

    socket.on('answer', (data) => {
        socket.to(data.roomId).emit('answer', {
            sdp: data.sdp,
            from: socket.id
        });
    });

    socket.on('ice-candidate', (data) => {
        socket.to(data.roomId).emit('ice-candidate', {
            candidate: data.candidate,
            from: socket.id
        });
    });

    socket.on('disconnect', () => {
        console.log('User disconnected:', socket.id);
        // Clean up rooms
        for (const [roomId, creatorId] of rooms.entries()) {
            if (creatorId === socket.id) {
                rooms.delete(roomId);
                io.to(roomId).emit('room-closed');
            }
        }
    });
});

const PORT = process.env.PORT || 3000;
http.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
}); 