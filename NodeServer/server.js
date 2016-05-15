var http = require('http').Server();
var io = require('socket.io')(http);
var Entities = require('html-entities').AllHtmlEntities; 
entities = new Entities();

var port = 4;

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
	var myRoom = "all";
	socket.join(myRoom);

	socket.on('chat message', function(nickname, message){
        if (message.trim() != "")
        {
            io.to(myRoom).emit('chat message', entities.encode(nickname), entities.encode(message));
        }
	});

	socket.on('chat notice', function(message){
		if (message.trim() != "")
		{
			io.to(myRoom).emit('chat notice',entities.encode(message));
		}
	});

	socket.on('join room', function(room){
		socket.leave(myRoom);
		myRoom = room;
		socket.join(myRoom);
	});
});

var getColor = function(number) {
	return String.fromCharCode(27) + '[' + number + 'm';
};