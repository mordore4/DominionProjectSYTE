var socket;

var ioInitialize = function(server) {
    socket = io(server);
};

var ioBindOnChatReceive = function(callback) {
    socket.on('chat message', function(nickname, msg){
        callback(nickname, msg);
    });
};

var ioSendChatMessage = function(nickname, message) {
    socket.emit('chat message', nickname, message);
};

var ioSetRoom = function(room) {
    socket.emit('join room', room);
};
