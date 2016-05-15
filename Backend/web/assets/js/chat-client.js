var socket;

var ioInitialize = function(server) {
    socket = io(server);
};

var ioBindOnChatReceive = function(callback) {
    socket.on('chat message', function(nickname, msg){
        callback(nickname, msg);
    });
};

var ioBindOnNoticeReceive = function(callback) {
    socket.on('chat notice', function(message){
        callback(message);
    });
};

var ioSendChatMessage = function(nickname, message) {
    socket.emit('chat message', nickname, message);
};

var ioSendChatNotice = function(message) {
    socket.emit('chat notice', message);
};

var ioSetRoom = function(room) {
    socket.emit('join room', room);
};
