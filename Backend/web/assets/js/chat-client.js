var socket;

var ioInitialize = function(server) {
    socket = io(server);
};

var ioBindOnChatReceive = function(callback) {
    socket.on('chat message', function(msg){
        callback(msg);
    });
};

var ioSendChatMessage = function(message) {
    socket.emit('chat message', message);
};
