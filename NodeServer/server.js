var http = require('http').Server();
var io = require('socket.io')(http);
var port = 1025;

http.listen(port, function(){
	console.log('Dominion-chatserver running on *:' + port);
	console.log(getColor(31) + "\\~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~/");
	console.log(getColor(32) + "|                                                     |");
	console.log(getColor(36) + "|  DDD    OOO   M   M  III  N   N  III   OOO   N   N  |");
	console.log(getColor(34) + "|  D  D  O   O  MM MM   I   NN  N   I   O   O  NN  N  |");
	console.log(getColor(35) + "|  D  D  O   O  M M M   I   N N N   I   O   O  N N N  |");
	console.log(getColor(31) + "|  D  D  O   O  M   M   I   N  NN   I   O   O  N  NN  |");
	console.log(getColor(32) + "|  DDD    OOO   M   M  III  N   N  III   OOO   N   N  |");
	console.log(getColor(36) + "|                                                     |");
	console.log(getColor(34) + "/~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~\\");
});

io.on('connection', function(socket){
	console.log(getColor(32) + socket.id + getColor(0));

	socket.on('chat message', function(msg){
		io.emit('chat message', msg);
	});
});

var getColor = function(number) {
	return String.fromCharCode(27) + '[' + number + 'm';
};