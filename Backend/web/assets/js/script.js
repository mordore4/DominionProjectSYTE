var kingdomCards;// = ["militia", "remodel", "smithy", "market", "mine", "cellar", "moat", "village", "woodcutter", "workshop"];
var fixedCards;// = ["copper", "silver", "gold", "curse", "province", "duchy", "estate"];
var forbiddenCards = ["province", "duchy", "estate", "curse"];

var io_isLocal = false;
var io_port = 3000;

var nodeserver = io_isLocal ? "http://localhost:" + io_port : "http://178.117.107.177:" + io_port,
    nickname = "farmboy" + Math.floor((Math.random() * 899) + 100),
    lobbyname = "",
    cardset = "",
    isHost = false,
    isMyTurn = false,
    phase = 0,
    isInGame = false,
    allowDiscard = false,
    discardMultiple = false,
    cardsToDiscard = [];

var pollInterval = 2000;

$(document).ready(function ()
{
    /*
     $("#dialog-confirm").hide();
     */
    $("#imgVis").hide();

    $("#imgChat").on('click', function ()
    {
        $("#chat").hide();
        /*
         $(function() {
         $( "#dialog-confirm" ).dialog({
         resizable: false,
         height:140,
         modal: true,
         buttons: {
         "Delete all items": function() {
         $( this ).dialog( "close" );
         },
         Cancel: function() {
         $( this ).dialog( "close" );
         }
         }
         });
         });
         */
        $("#imgVis").show();
    });

    $("#imgVis").on('click', function ()
    {
        $("#chat").show();
        $("#imgVis").hide();
    });

    var isCtrl = false;
    $(document).keyup(function (e)
    {
        if (e.which == 18) isCtrl = false;
    }).keydown(function (e)
    {
        if (e.which == 18) isCtrl = true;
        if (e.which == 67 && isCtrl == true)
        {
            if ($("#chat").is(":visible"))
            {
                $("#chat").hide();
                $("#imgVis").show();
            }
            else
            {
                $("#chat").show();
                $("#imgVis").hide();
            }

            return false;
        }
    });

    $("#hand").sortable({
        revert: true,
        connectWith: "#cardsComeCenter"
    });

    $('#cardsComeCenter').sortable({
        handle: "none",
        placeholder: false,
        items: "li:not('#cardsComeCenter li')",
        receive: function (event, ui)
        {
            var cardName = ui.item.attr("data-cardname");

            if (!isMyTurn || $.inArray(cardName, forbiddenCards) != -1)
            {
                $(ui.sender).sortable('cancel');
            }
            else
            {
                if (phase == 0) //Action phase
                {
                    if (!isInFixedCards(cardName)) //Don't allow treasures
                    {
                        ajaxPutCardOnTable(cardName, ajaxRetrieveHand);
                    }
                    else $(ui.sender).sortable('cancel');
                }
                else if (phase == 1) //Treasure phase
                {
                    if ($.inArray(cardName, ["copper", "silver", "gold"]) >= 0)
                    {
                        ajaxPutCardOnTable(cardName, ajaxRetrieveBuyableCards);
                    }
                    else $(ui.sender).sortable('cancel');
                }
                else
                {
                    $(ui.sender).sortable('cancel');
                }

                hideHandIfEmpty();
            }
        }
    });

    $('#play').on('click', enterNickName);
    $("#card-info").on('click', function ()
    {
        $(this).hide();
    }).hide().removeClass("hidden");
    $("#end-turn").on('click', endTurn).hide();
    $("#end-actions").on('click', endActions).hide();
    $("#play-treasures").on('click', playTreasures).hide();
    $("#finish-discarding").on('click', finishDiscarding).hide();

    $("#start-game").on('click', ajaxStartGame);

    $(document).keypress(function (e)
    {
        /*var chatMessage = $("#chat-message");

         if (e.which == 116 && !chatMessage.is(":focus")) {
         $("#chat").toggleClass("visible");
         e.preventDefault();
         chatMessage.focus();
         }*/
    });

    $("#chat-message").on('blur', function ()
    {
        //$("#chat").removeClass("visible");
    }).on('keypress', sendChatMessage);

    $("body").on('click', "#hand li", discardCard);

    ajaxGetCardsets();

    ioInitialize(nodeserver);
    ioBindOnChatReceive(receiveChatMessage);
    ioBindOnNoticeReceive(receiveChatNotice);
    //ioSendChatNotice(nickname + " has joined");
    //enterNickName();
    //playGame();
});

var hideHandIfEmpty = function ()
{
    if ($("#hand").is(":empty"))
    {
        $("#handdecor").hide();
    }
};

var isInFixedCards = function (cardName)
{
    for (var i = 0; i < fixedCards.length; i++)
    {
        if (fixedCards[i].name == cardName)
        {
            return true;
        }
    }

    return false;
};

var sendChatMessage = function (e)
{
    var message = $(this).val();

    if (e.which == 13 || e.keyCode == 13)
    {
        if (!checkMessageIsCommand(message))
        {
            ioSendChatMessage(nickname, message);
        }
        else
        {
            executeCommand(message);
        }

        $(this).val("");
    }
};

var checkMessageIsCommand = function (message)
{
    return (message.indexOf("/") == 0); //Starts with a /
};

var executeCommand = function (message)
{
    message = message.substr(1);


    var command = message.split(' ')[0];
    var data = message.substr(message.indexOf(' ') + 1);

    switch (command)
    {
        case "nickname":
        case "nick":
            if (!isInGame)
            {
                ioSendChatNotice(nickname + " changed his nickname to " + data);
                nickname = data;
            }
            else
            {
                receiveChatNotice("You can't change nicknames while in a game");
            }
            break;
    }
};

var receiveChatMessage = function (nickname, message)
{
    var html = '<li><strong class="username">' + nickname + ': </strong> ' + message + '</li>';

    appendToChat(html);
};

var receiveChatNotice = function (message)
{
    var html = '<li><strong class="notice">' + message + '</strong></li>';

    appendToChat(html);
};

var appendToChat = function (html)
{
    $("#chat").find(".message-list").append(html).stop()
        .animate({scrollTop: $('#chat .message-list').prop("scrollHeight")}, 350);
};

var enterGame = function (ishost)
{
    $("#enternickname").hide();
    nickname = $("#nickname").val();
    lobbyname = $("#lobbyname").val();
    cardset = $("#cardset").val();
    isHost = ishost;
    ioSetRoom(lobbyname);
    lobbyRoom();
};

var endTurn = function ()
{
    $("#end-turn").hide();
    $("#play-treasures").hide();
    $("#finish-discarding").hide();
    $("#end-actions").hide();
    $("#current-player").removeClass("glow");
    $("#hand").sortable("disable");
    ajaxEndTurn();
};

var endActions = function ()
{
    $("#end-actions").hide();
    ajaxEndActions();
};

var enterNickName = function ()
{
    $('#menu').hide();
    $("footer").hide();

    $("#enternickname").show();

    $("#createlobby").on('click', function ()
    {
        enterGame(true);
    });

    $("#joinlobby").on('click', function ()
    {
        enterGame(false);
    })
};

var lobbyRoom = function ()
{
    $('#menu').hide();
    $("footer").hide();

    $("#lobbyroom").show();
    $("#lobby-name").text(lobbyname);

    if (!isHost) $("#startgame").hide();

    $("#startgame").on('click', function ()
    {
        if (!isInGame)
        {
            ajaxStartGame();
        }
    });

    if (isHost) ajaxCreateLobby();
    else ajaxJoinLobby();
};

var playGame = function ()
{
    isInGame = true;

    $('body').addClass("game");

    //Look at my pet
    //I call him Hideyoshi
    $("#lobbyroom").hide();
    $('#menu').hide();
    $("footer").hide();
    $("#hand").hide();
    $("#kingdomcards").hide();
    $("#topstates").hide();
    $("#coins").hide();
    $("#handdecor").hide();
    $("#topcoins-title").hide();
    $("#topstates-title").hide();
    $("#kingdomwrapper").hide();
    $("#cardsComeCenter").hide();
    $("#player-turninfo").hide();
    $("#end-actions").hide();
    $("#play-treasures").hide();
    $("#finish-discarding").hide();
    $("#end-turn").hide();

    $("#current-player").show();
    $("#gamewindow").show();
};

var createHand = function (cardsArray)
{
    var hand = $('#hand');
    hand.empty();

    for (var i = 0, len = cardsArray.length; i < len; i++)
    {
        var imageSource = "'images/cards/" + cardsArray[i] + ".jpg'";
        var html = '<li data-cardname="' + cardsArray[i] + '" style="background-image: url(' + imageSource + '");">';

        hand.append(html);
    }
};

var createCardsOnTable = function (cardsArray)
{
    var hand = $('#cardsComeCenter');
    hand.empty();

    for (var i = 0, len = cardsArray.length; i < len; i++)
    {
        var imageSource = "'images/cards/" + cardsArray[i] + ".jpg'";
        var html = '<li data-cardname="' + cardsArray[i] + '" style="background-image: url(' + imageSource + '");">';

        hand.append(html);
    }
};

var addFixedCards = function (cardsArray)
{
    var topStates = $("#topstates");
    var coins = $("#coins");

    topStates.empty();
    coins.empty();

    for (var i = cardsArray.length - 1; i >= 0; i--)
    {
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
    $("#topcoins-title").show();
    $("#topstates-title").show();
};

var addKingdomCards = function (cardsArray)
{
    var element = $("#kingdomcards").find('ul');

    element.empty();

    for (var i = 0, len = cardsArray.length; i < len; i++)
    {
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
    $("#kingdomwrapper").show();
};

var buyCard = function ()
{
    var cardName = $(this).attr("data-cardname");

    if (isMyTurn && phase == 2 && getCardBuyable(cardName) && getTurnInfo("buys") > 0)
    {
        ioSendChatNotice("<strong class=\"username\">" + nickname + "</strong> bought a <strong class=\"cardname\">" + cardName + "</strong>");
        ajaxBuyCard(cardName);
    }
};

var discardCard = function ()
{
    if (!allowDiscard) return;

    var cardName = $(this).attr("data-cardname");

    if (!discardMultiple)
    {
        ajaxDiscardCard(cardName);
    }
    else
    {
        $(this).remove();
        cardsToDiscard.push(cardName);
    }
};

var finishDiscarding = function ()
{
    $("#finish-discarding").hide();
    ajaxDiscardMultiple(cardsToDiscard);
    cardsToDiscard = [];
};

var showCardInfo = function (e)
{
    var cardName = $(this).parent().attr("data-cardname");

    $("#card-info").show().find("img").attr("src", "images/cards/" + cardName + ".jpg");

    e.stopPropagation();
};

var findCardElement = function (cardName)
{
    var foundCard;

    foundCard = $("#coins").find('figure[data-cardname="' + cardName + '"]');

    if (foundCard.size() == 0)
    {
        foundCard = $("#topstates").find('figure[data-cardname="' + cardName + '"]');
    }

    if (foundCard.size() == 0)
    {
        foundCard = $("#kingdomcards").find('div[data-cardname="' + cardName + '"]');
    }

    return foundCard;
};

var setCardBuyable = function (cardName, buyable)
{
    var foundCard = findCardElement(cardName);

    //States or coins
    if (foundCard.closest("#kingdomcards").size() == 0)
    {
        foundCard = foundCard.find("img");
    }
    else //Kingdom cards
    {
        foundCard = foundCard.find(".kingdomcard-top, .kingdomcard-bottom");
    }

    if (buyable) foundCard.removeClass("nobuy");
    else foundCard.addClass("nobuy");
};

var getCardBuyable = function (cardName)
{
    var isFixedCard = isInFixedCards(cardName);
    var foundCard = findCardElement(cardName);

    //States or coins
    if (isFixedCard)
    {
        foundCard = foundCard.find("img");
    }
    else //Kingdom cards
    {
        foundCard = foundCard.find(".kingdomcard-top, .kingdomcard-bottom");
    }

    return !foundCard.hasClass("nobuy");
};

var setCardAmount = function (cardName, amount)
{
    var foundCard = findCardElement(cardName);

    foundCard = foundCard.find("div.amount");

    foundCard.text(amount);
};

var highlightCardsOfType = function (type)
{
    var hand = $("#hand");

    hand.find("li").removeClass("contextaware");

    hand.find("li").each(function ()
    {
        var cardName = $(this).attr("data-cardname");

        switch (type)
        {
            case "treasure":
                var index = $.inArray(cardName, fixedCards);
                if (index >= 0 && index < 3) $(this).addClass("contextaware");
        }

    });
};

var playTreasures = function ()
{
    var treasuresToPlay = $("#hand").find("li").filter("[data-cardname=copper],[data-cardname=silver],[data-cardname=gold]");

    treasuresToPlay.each(function ()
    {
        $("#cardsComeCenter").append($(this).clone());
        $(this).remove();
    });

    $("#play-treasures").hide();
    ajaxPlayTreasures();

    hideHandIfEmpty();
};

var setTurnInfo = function (type, value)
{
    $("#player-turninfo").find("#turn-" + type).find("strong").text(value);
};

var getTurnInfo = function (type)
{
    return parseInt($("#player-turninfo").find("#turn-" + type).find("strong").text());
};

var setUpGame = function ()
{
    playGame();

    ajaxRetrieveHand();
    $("#hand").show();
    $("#handdecor").show();
    $("#cardsComeCenter").show();
    $("#player-turninfo").show();
    isMyTurn = false;
    ajaxRetrieveKingdomCards();
    ajaxCheckGameStatus();
};

var addCardSets = function (cardSets)
{
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

var clearBuyableCards = function ()
{
    for (var card in kingdomCards)
    {
        setCardBuyable(kingdomCards[card].name, false);
    }

    for (var card in fixedCards)
    {
        setCardBuyable(fixedCards[card].name, false);
    }
};

var showVictoryScreen = function (pointsList, winner)
{
    var gameOver = $("#game-over");
    gameOver.find("h1").text("You win!");
    gameOver.find("article h2").hide();
    showPointsPerPlayer(pointsList, winner);
    gameOver.show();
};

var showLossScreen = function (pointsList, winner)
{
    var gameOver = $("#game-over");
    gameOver.find("h1").text("You lose!");
    gameOver.find("article h2").text("Winner: " + winner);
    showPointsPerPlayer(pointsList, winner);
    gameOver.show();
};

var showPointsPerPlayer = function (pointsList, winner)
{
    var tbody = $("#game-over").find("tbody");
    tbody.empty();

    for (var i = 0; i < pointsList.length; i++)
    {
        var pointsOfPlayer = pointsList[i];

        var html = "<tr>";
        var isMe = (pointsOfPlayer[0] == nickname);
        var isWinner = (pointsOfPlayer[0] == winner);

        if (pointsOfPlayer[0] == winner)
        {
            html = '<tr class="winner">';
        }

        html += "<td>" + pointsOfPlayer[0] + "</td><td>" + pointsOfPlayer[1] + "</td></tr>";

        tbody.append(html);

        var newRow = tbody.find("tr").last();

        if (isMe)
        {
            newRow.addClass("me");
        }

        if (isWinner)
        {
            newRow.addClass("winner");
        }

    }
};