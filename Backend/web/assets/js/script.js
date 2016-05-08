var tempHand = ["adventurer", "bureaucrat", "cellar", "chancellor", "chapel", "copper", "council room"];
var kingdomCards = ["militia", "remodel", "smithy", "market", "mine", "cellar", "moat", "village", "woodcutter", "workshop"];
var fixedCards = ["copper", "silver", "gold", "curse", "province", "duchy", "estate"];
var forbiddenCards = ["province", "duchy", "estate", "curse"];

var nickname = "mingebag";
var lobbyname = "";
var isHost = false;
var isMyTurn = false;

var pollInterval = 2000;

$(document).ready(function () {
    $(".register").on('click', function () {
        $('#menu').hide();
        $("#form").show();
    });

    $("#hand").sortable({
        revert: true,
        connectWith: "#cardsComeCenter"
    });

    $('#cardsComeCenter').sortable({
        handle: "none",
        placeholder: false,
        items: "li:not('#cardsComeCenter li')",
        receive: function (event, ui) {
            var cardName = ui.item.attr("data-cardname");

            if (!isMyTurn || $.inArray(cardName, forbiddenCards) != -1) {
                $(ui.sender).sortable('cancel');
            } else {
                putCardOnTable(cardName);
            }
        }
    });

    $("label[for=state_id]").parent().load("assets/fragments/states.html");
    $('#play').on('click', enterNickName);
});

var enterNickName = function () {
    $('#menu').hide();
    $("div.mastfoot").hide();

    $("#enternickname").show();

    $("#createlobby").on('click', function () {
        $("#enternickname").hide();
        nickname = $("#nickname").val();
        lobbyname = $("#lobbyname").val();
        isHost = true;
        playGame();
    });

    $("#joinlobby").on('click', function () {
        $("#enternickname").hide();
        nickname = $("#nickname").val();
        lobbyname = $("#lobbyname").val();
        isHost = false;
        playGame();
    })
};

var playGame = function () {
    addFixedCards();
    addKingdomCards(kingdomCards);
    createHand(tempHand);

    $('body').addClass("game");

    $('#menu').hide();
    $("div.mastfoot").hide();

    $("#gamewindow").show();
    $('#cardsComeCenter').show();

    if (isHost) createLobby();
    else joinLobby();

};

var createHand = function (cardsArray) {
    var hand = $('#hand');
    hand.empty();

    for (var i = 0, len = cardsArray.length; i < len; i++) {
        var imageSource = "'images/cards/" + cardsArray[i] + ".jpg'";
        var html = '<li data-cardname="' + cardsArray[i] + '" style="background-image: url(' + imageSource + '");">';

        hand.append(html);
    }
};

var createCardsOnTable = function (cardsArray) {
    var hand = $('#cardsComeCenter');
    hand.empty();

    for (var i = 0, len = cardsArray.length; i < len; i++) {
        var imageSource = "'images/cards/" + cardsArray[i] + ".jpg'";
        var html = '<li data-cardname="' + cardsArray[i] + '" style="background-image: url(' + imageSource + '");">';

        hand.append(html);
    }
};

var addFixedCards = function () {
    for (var i = 0, len = fixedCards.length; i < len; i++) {
        var imgsrc = "images/" + fixedCards[i] + "_top.png";

        var html = '<li><figure>' +
            '<img alt="' + fixedCards[i] + '" title="' + fixedCards[i] + '" src="' + imgsrc + '" />' +
            '</figure></li>';

        if (i > 3)
            $('#topstates').append(html);
        else
            $('#coins').append(html);
    }
};

var addKingdomCards = function (cardsArray) {
    for (var i = 0, len = cardsArray.length; i < len; i++) {
        var cardName = cardsArray[i];

        var html = '<li><div class="kingdomcard">';
        html += '<div class="kingdomcard-top" style="background-image: url(images/cards/' + cardName + '.jpg);"></div>';
        html += '<div class="kingdomcard-bottom" style="background-image: url(images/cards/' + cardName + '.jpg);"></div>';
        html += '</div></li>';

        $("#kingdomcards").find('ul').append(html);
    }
};

var createLobby = function () {
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "createlobby",
                nickname: nickname,
                lobbyname: lobbyname
            }
        })
        .done(function (data) {
            console.log("Created lobby " + lobbyname);
            checkLobbyReady();
        });
};

var checkLobbyReady = function () {
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "haslobbystarted",
                lobbyname: lobbyname
            }
        })
        .done(function (data) {
            var status = JSON.parse(data);

            if (status) {
                retrieveHand();
                isMyTurn = false;
                checkGameStatus();
            }
            else {
                setTimeout(checkLobbyReady, pollInterval);
            }
        });
};

var checkGameStatus = function () {
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "fetchlobbystatus",
                nickname: nickname,
                lobbyname: lobbyname
            }
        })
        .done(function (data) {
            var status = JSON.parse(data);

            isMyTurn = status.isMyTurn;

            if (isMyTurn) { 
                $("#hand").sortable("enable");
            }
            else {
                var cardNames = [];
                status.cardsOnTable.forEach(function (item) {
                    cardNames.push(item.name);
                });
                createCardsOnTable(cardNames);

                $("#hand").sortable("disable");
            }
            
            setTimeout(checkGameStatus, pollInterval);
        });
};

var joinLobby = function () {
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "joinlobby",
                nickname: nickname,
                lobbyname: lobbyname
            }
        })
        .done(function (data) {
            console.log("Joined lobby " + lobbyname);
            retrieveHand();
            isMyTurn = false;
            checkGameStatus();
        });
};

var retrieveHand = function () {
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "retrievehand",
                nickname: nickname,
                lobbyname: lobbyname
            }
        })
        .done(function (data) {
            var cardArray = JSON.parse(data);
            var cardNames = [];

            cardArray.forEach(function (item) {
                cardNames.push(item.name);
            });

            createHand(cardNames);
        });
};

var putCardOnTable = function (cardname) {
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "putcardontable",
                cardname: cardname,
                lobbyname: lobbyname
            }
        })
        .done(function (data) {
            //Don't do jack shit
        });
};