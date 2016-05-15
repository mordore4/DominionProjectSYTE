var tempHand = ["adventurer", "bureaucrat", "gold", "curse", "cellar", "chancellor", "copper", "chapel"];
var kingdomCards = ["militia", "remodel", "smithy", "market", "mine", "cellar", "moat", "village", "woodcutter", "workshop"];
var fixedCards = ["copper", "silver", "gold", "curse", "province", "duchy", "estate"];
var forbiddenCards = ["province", "duchy", "estate", "curse"];

var io_isLocal = true;
var io_port = 4;

var nodeserver = io_isLocal ? "http://localhost:" + io_port : "http://178.117.107.177:" + io_port;
var nickname = "farmboy" + Math.floor((Math.random() * 899) + 100);
var lobbyname = "";
var cardset = "";
var isHost = false;
var isMyTurn = false;
var phase = 0;

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
                if (phase == 0) //Action phase
                {
                    if (!($.inArray(cardName, fixedCards) >= 0)) //Don't allow treasures
                    {
                        ajaxPutCardOnTable(cardName, ajaxRetrieveHand);
                    }
                    else $(ui.sender).sortable('cancel');
                }
                else if (phase == 1) //Treasure phase
                {
                    if ($.inArray(cardName, fixedCards) >= 0)
                    {
                        ajaxPutCardOnTable(cardName);
                    }
                    else $(ui.sender).sortable('cancel');
                }
                else
                {
                    $(ui.sender).sortable('cancel');
                }
            }
        }
    });

    $("label[for=state_id]").parent().load("assets/fragments/states.html");
    $('#play').on('click', enterNickName);
    $("#card-info").on('click', function () {
        $(this).hide();
    }).hide().removeClass("hidden");
    $("#end-turn").on('click', ajaxEndTurn).hide();

    $(document).keypress(function(e){
        /*var chatMessage = $("#chat-message");

        if (e.which == 116 && !chatMessage.is(":focus")) {
            $("#chat").toggleClass("visible");
            e.preventDefault();
            chatMessage.focus();
        }*/
    });

    $("#chat-message").on('blur', function() {
        //$("#chat").removeClass("visible");
    }).on('keypress', sendChatMessage);

    ajaxGetCardsets();

    ioInitialize(nodeserver);
    ioBindOnChatReceive(receiveChatMessage);
    //enterNickName();
    //playGame();
});

var sendChatMessage = function(e) {
    var message = $(this).val();

    if (e.which == 13 || e.keyCode == 13)
    {
        ioSendChatMessage(nickname, message);
        $(this).val("");
    }
};

var receiveChatMessage = function(nickname, message)
{
    var html = '<li><span class="username">' + nickname + '</span>' + message + '</li>';

    $("#chat").find(".message-list").append(html).stop()
        .animate({ scrollTop: $('#chat .message-list').prop("scrollHeight")}, 350);
};

var enterGame = function (ishost)
{
    $("#enternickname").hide();
    nickname = $("#nickname").val();
    lobbyname = $("#lobbyname").val();
    cardset = $("#cardset").val();
    isHost = ishost;
    ioSetRoom(lobbyname);
    playGame();
};

var enterNickName = function () {
    $('#menu').hide();
    $("div.mastfoot").hide();

    $("#enternickname").show();

    $("#createlobby").on('click', function () {
        enterGame(true);
    });

    $("#joinlobby").on('click', function () {
        enterGame(false);
    })
};

var playGame = function () {
    createHand(tempHand);

    $('body').addClass("game");

    $('#menu').hide();
    $("div.mastfoot").hide();
    $("#hand").hide();
    $("#kingdomcards").hide();
    $("#topstates").hide();
    $("#coins").hide();

    $("#gamewindow").show();
    $('#cardsComeCenter').show();

    if (isHost) ajaxCreateLobby();
    else ajaxJoinLobby();

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

var addFixedCards = function (cardsArray) {
    var topStates = $("#topstates");
    var coins = $("#coins");

    topStates.empty();
    coins.empty();

    for (var i = cardsArray.length - 1; i >= 0; i--) {
        var cardName = cardsArray[i].name;

        var imgsrc = "images/" + cardName + "_top.png";

        var html = '<li><figure data-cardname="' + cardName + '">' +
            '<div class="amount">' + cardsArray[i].amount + '</div><a href="#" class="info"></a><img class="nobuy" alt="'
            + cardName + '" title="' + cardName + '" src="' + imgsrc + '" />' +
            '</figure></li>';

        if (i > 3)
            coins.append(html);
        else
            topStates.append(html);
    }

    topStates.find('figure').on('click', buyCard);
    coins.find('figure').on('click', buyCard);

    topStates.find("a.info").on('click', showCardInfo);
    coins.find("a.info").on('click', showCardInfo);

    topStates.show();
    coins.show();
};

var addKingdomCards = function (cardsArray) {
    var element = $("#kingdomcards").find('ul');

    element.empty();

    for (var i = 0, len = cardsArray.length; i < len; i++) {
        var cardName = cardsArray[i].name;

        var html = '<li><div data-cardname="' + cardName + '" class="kingdomcard">';
        html += '<div class="kingdomcard-top nobuy" style="background-image: url(\'images/cards/' + cardName + '.jpg\');"></div>';
        html += '<div class="kingdomcard-bottom nobuy" style="background-image: url(\'images/cards/' + cardName + '.jpg\');"></div>';
        html += '<div class="amount">' + cardsArray[i].amount + '</div><a href="#" class="info"></a></div></li>';

        element.append(html);
    }

    element.find('div.kingdomcard').on('click', buyCard);
    $("a.info").on('click', showCardInfo);
    $("#kingdomcards").show();
};

var buyCard = function() {
    var cardName = $(this).attr("data-cardname");

    if (getCardBuyable(cardName) && getTurnInfo("buys") > 0)
        ajaxBuyCard(cardName);
};

var showCardInfo = function (e) {
    var cardName = $(this).parent().attr("data-cardname");

    $("#card-info").show().find("img").attr("src", "images/cards/" + cardName + ".jpg");

    e.stopPropagation();
};

var findCardElement = function(cardName)
{
    var isFixedCard = $.inArray(cardName, fixedCards);
    var foundCard;
    var containerElement = $("#coins");

    if (isFixedCard >= 0) {
        if (isFixedCard > 3) containerElement = $("#topstates");

        foundCard = containerElement.find('figure[data-cardname="' + cardName + '"]');
    }
    else //Kingdom cards
    {
        foundCard = $("#kingdomcards").find('div[data-cardname="' + cardName + '"]');
    }

    return foundCard;
};

var setCardBuyable = function (cardName, buyable) {
    var isFixedCard = $.inArray(cardName, fixedCards);
    var foundCard = findCardElement(cardName);

    //States or coins
    if (isFixedCard >= 0) {
        foundCard = foundCard.find("img");
    }
    else //Kingdom cards
    {
        foundCard = foundCard.find(".kingdomcard-top, .kingdomcard-bottom");
    }

    if (buyable) foundCard.removeClass("nobuy");
    else foundCard.addClass("nobuy");
};

var getCardBuyable = function (cardName) {
    var isFixedCard = $.inArray(cardName, fixedCards);
    var foundCard = findCardElement(cardName);

    //States or coins
    if (isFixedCard >= 0) {
        foundCard = foundCard.find("img");
    }
    else //Kingdom cards
    {
        foundCard = foundCard.find(".kingdomcard-top, .kingdomcard-bottom");
    }

    return !foundCard.hasClass("nobuy");
};

var setCardAmount = function (cardName, amount) {
    var isFixedCard = $.inArray(cardName, fixedCards);
    var foundCard = findCardElement(cardName);
    var containerElement = $("#coins");

    foundCard = foundCard.find("div.amount");

    foundCard.text(amount);
};

var highlightCardsOfType = function(type)
{
    var hand = $("#hand");

    hand.find("li").removeClass("contextaware");

    hand.find("li").each(function() {
        var cardName = $(this).attr("data-cardname");

        switch(type)
        {
            case "treasure":
                var index = $.inArray(cardName, fixedCards);
                if (index >=0 && index < 3) $(this).addClass("contextaware");
        }

    });
};

var setTurnInfo = function(type, value)
{
    $("#player-turninfo").find("#turn-" + type).find("span").text(value);
};

var getTurnInfo = function(type)
{
    return parseInt($("#player-turninfo").find("#turn-" + type).find("span").text());
};

var setUpGame = function ()
{
    ajaxRetrieveHand();
    $("#hand").show();
    isMyTurn = false;
    ajaxRetrieveKingdomCards();
    ajaxCheckGameStatus();
};

var addCardSets = function(cardSets) {
    var element = $("#cardset");

    element.empty();

    for (var i = 0; i < cardSets.length; i++)
    {
        var setName = cardSets[i];

        var html = "<option>" + setName + "</option>";
        element.append(html);
    }

    element.val("first game");
};

var ajaxGetCardsets = function() {
    $.ajax
        ({
            method: "GET",
            url: "server/gamemanager",
            data:
            {
                command: "getcardsets"
            }
        })
        .done(function (data)
        {
            var cardSets = JSON.parse(data);

            addCardSets(cardSets);
        });
};

var ajaxCreateLobby = function () {
    $.ajax
    ({
        method: "GET",
        url: "server/gamemanager",
        data:
        {
            command: "createlobby",
            nickname: nickname,
            lobbyname: lobbyname,
            cardset: cardset
        }
    })
        .done(function ()
        {
            console.log("Created lobby " + lobbyname);
            ajaxCheckLobbyReady();
        });
};

var ajaxCheckLobbyReady = function () {
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
                setUpGame();
            }
            else {
                setTimeout(ajaxCheckLobbyReady, pollInterval);
            }
        });
};

var ajaxJoinLobby = function () {
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
            setUpGame();
        });
};

var ajaxRetrieveKingdomCards = function() {
    $.ajax({
        method: "GET",
        url: "server/gamemanager",
        data: {
            command: "retrievekingdomcards",
            lobbyname: lobbyname
        }
    })
        .done(function (data) {
            var cards = JSON.parse(data);

            addKingdomCards(cards.kingdomCards);
            addFixedCards(cards.fixedCards);

            ajaxRetrieveBuyableCards();
        });
};

var ajaxRetrieveKingdomCardSatus = function () {
    $.ajax({
        method: "GET",
        url: "server/gamemanager",
        data: {
            command: "retrievekingdomcardstatus",
            lobbyname: lobbyname
        }
    })
};

var clearBuyableCards = function() {
    for (var card in kingdomCards)
    {
        setCardBuyable(kingdomCards[card], false);
    }

    for (var card in fixedCards)
    {
        setCardBuyable(fixedCards[card], false);
    }
};

var ajaxRetrieveBuyableCards = function() {
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "retrievebuyablecards",
                lobbyname: lobbyname
            }
        })
        .done(function (data) {
            var buyableCards = JSON.parse(data);

            for (var card in buyableCards)
            {
                currentCardName = buyableCards[card][0];
                setCardBuyable(currentCardName, buyableCards[card][2]);
                setCardAmount(currentCardName, buyableCards[card][1])
            }
        });
};

var ajaxEndTurn = function() {
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "endturn",
                lobbyname: lobbyname,
                nickname: nickname
            }
        })
        .done(function (data) {
            $("#end-turn").hide();
            clearBuyableCards();
            ajaxRetrieveHand();
        });
};

var ajaxCheckGameStatus = function () {
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "fetchgamestatus",
                nickname: nickname,
                lobbyname: lobbyname
            }
        })
        .done(function (data) {
            var status = JSON.parse(data);

            //First time we notice the phase is after-treasure mode

            phase = status.phase;

            //Update kingdom cards in case of buy
            if (phase == 1 || phase == 3)
            {
                ajaxRetrieveBuyableCards();
            }

            //This is the first time we notice it's our turn
            //So this can be seen as stuff to do on turn start
            if (!isMyTurn && status.isMyTurn)
            {
                $("body").addClass("myturn");
                $("#cardsComeCenter").empty();
                $("#end-turn").show();
                clearBuyableCards();
                $("#hand").sortable("enable");
            }

            isMyTurn = status.isMyTurn;

            setTurnInfo("actions", status.actions);
            setTurnInfo("buys", status.buys);
            setTurnInfo("coins", status.coins);

            if (!isMyTurn) {
                var cardNames = [];
                status.cardsOnTable.forEach(function (item) {
                    cardNames.push(item.name);
                });
                createCardsOnTable(cardNames);

                $("body").removeClass("myturn");
                $("#hand").sortable("disable");
            }

            setTimeout(ajaxCheckGameStatus, pollInterval);
        });
};


var ajaxRetrieveHand = function () {
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

            $("#hand").show();

            createHand(cardNames);
        });
};

var ajaxPutCardOnTable = function (cardname, callback) {
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "putcardontable",
                cardname: cardname,
                lobbyname: lobbyname,
                nickname: nickname
            }
        })
        .done(function() {
            if (typeof callback !== 'undefined') callback();
        });
};

var ajaxBuyCard = function(cardname) {
    $.ajax({
        method: "GET",
        url: "server/gamemanager",
        data: {
            command: "buycard",
            cardname: cardname,
            lobbyname: lobbyname,
            nickname: nickname
        }
    })
};