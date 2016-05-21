var timeOut = null;

var ajaxGetCardsets = function ()
{
    $.ajax
        ({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "getcardsets"
            }
        })
        .done(function (data)
        {
            var cardSets = JSON.parse(data);

            addCardSets(cardSets);
        });
};

var ajaxCreateLobby = function ()
{
    $.ajax
        ({
            method: "GET",
            url: "server/gamemanager",
            data: {
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

var ajaxCheckLobbyReady = function ()
{
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "haslobbystarted",
                lobbyname: lobbyname
            }
        })
        .done(function (data)
        {
            var status = JSON.parse(data);

            var players = status.players;

            $("#player-list").empty();
            for (var i = 0; i < players.length; i++)
            {
                $("#player-list").append(players[i] + "<br/>");
            }

            if (status.started)
            {
                setUpGame();
            }
            else
            {
                setTimeout(ajaxCheckLobbyReady, pollInterval);
            }
        });
};

var ajaxJoinLobby = function ()
{
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "joinlobby",
                nickname: nickname,
                lobbyname: lobbyname
            }
        })
        .done(function (data)
        {
            ajaxCheckLobbyReady();
        });
};

var ajaxStartGame = function ()
{
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "startgame",
                lobbyname: lobbyname
            }
        })
        .done(function (data)
        {

        });
};

var ajaxRetrieveKingdomCards = function ()
{
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "retrievekingdomcards",
                lobbyname: lobbyname
            }
        })
        .done(function (data)
        {
            var cards = JSON.parse(data);

            kingdomCards = cards.kingdomCards;
            fixedCards = cards.fixedCards;

            addKingdomCards(cards.kingdomCards);
            addFixedCards(cards.fixedCards);

            ajaxRetrieveBuyableCards();
        });
};

var ajaxRetrieveBuyableCards = function ()
{
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "retrievebuyablecards",
                lobbyname: lobbyname
            }
        })
        .done(function (data)
        {
            var buyableCards = JSON.parse(data);

            for (var card in buyableCards)
            {
                currentCardName = buyableCards[card][0];
                setCardBuyable(currentCardName, buyableCards[card][2]);
                setCardAmount(currentCardName, buyableCards[card][1])
            }
        });
};

var ajaxEndTurn = function ()
{
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "endturn",
                lobbyname: lobbyname,
                nickname: nickname
            }
        })
        .done(function (data)
        {
            ajaxCheckGameStatus();
            clearBuyableCards();
            ajaxRetrieveHand();
        });
};

var ajaxEndActions = function ()
{
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "endactions",
                lobbyname: lobbyname,
                nickname: nickname
            }
        })
        .done(function (data)
        {
            ajaxCheckGameStatus();
        });
};

var ajaxCheckGameStatus = function ()
{
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "fetchgamestatus",
                nickname: nickname,
                lobbyname: lobbyname
            }
        })
        .done(function (data)
        {
            var status = JSON.parse(data);
            phase = status.phase;

            //This information is shared for all players
            //(Everyone sees the AB and C of the current player)
            setTurnInfo("actions", status.actions);
            setTurnInfo("buys", status.buys);
            setTurnInfo("coins", status.coins);

            $("#current-player").find("span.playername").text(status.currentPlayer);

            //This is the first time we notice it's our turn
            //So this can be seen as stuff to do on turn start
            if (!isMyTurn && status.isMyTurn)
            {
                allowDiscard = false;
                clearBuyableCards();
                ajaxRetrieveBuyableCards();
                $("#current-player").addClass("glow");
                $("#cardsComeCenter").empty();
                $("#handdecor").show();
            }

            isMyTurn = status.isMyTurn;

            var bEndActions = $("#end-actions");
            var bEndTurn = $("#end-turn");
            var bPlayTreasures = $("#play-treasures");

            if (isMyTurn)
            {
                //Why not just hide everything and just show
                //what is necessary? Because it would cause flickering
                //And there is no real way to know that a phase has
                //ended except through the game status

                //Don't show anything and hide everything if a condition is set
                //essentially stopping game flow

                if (status.conditionsActive)
                {
                    $("#hand").sortable("disable");

                    bEndActions.hide();
                    bEndTurn.hide();
                    bPlayTreasures.hide();
                }
                else
                {
                    $("#hand").sortable("enable");

                    switch (phase)
                    {
                        case 0:
                            bEndActions.show();
                            bEndTurn.hide();
                            bPlayTreasures.hide();
                            break;
                        case 1:
                            bEndActions.hide();
                            bEndTurn.show();
                            bPlayTreasures.show();
                            break;
                        case 3:
                            bEndActions.hide();
                            bEndTurn.show();
                            bPlayTreasures.hide();
                            if (status.buys == 0 || status.coins == 0)
                            {
                                endTurn();
                            }
                            break;
                    }
                }
            }
            else
            {
                console.log(status.myCondition);

                if (phase == 1 || phase == 3)
                {
                    ajaxRetrieveBuyableCards()
                }

                //Get the cards that are on the table
                //Not needed if it is actually our turn
                var cardNames = [];
                status.cardsOnTable.forEach(function (item)
                {
                    cardNames.push(item);
                });
                createCardsOnTable(cardNames);
            }

            if (status.myCondition != null)
            {
                switch (status.myCondition.name)
                {
                    case "RemoveCardsCondition":
                        $("#handdecor").find("div.title").text("Remove " + status.myCondition.condition.cardsToRemove + " cards");
                        allowDiscard = true;
                        break;
                    case "GainCardCondition":
                        break;
                }
            }
            else
            {
                allowDiscard = false;
                $("#handdecor").find("div.title").text("Your hand");
            }

            //Keep calling this ajax unless it's our turn
            if (timeOut != null) clearTimeout(timeOut);
            if (!isMyTurn || status.conditionsActive) timeOut = setTimeout(ajaxCheckGameStatus, pollInterval);
        });
};


var ajaxRetrieveHand = function ()
{
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "retrievehand",
                nickname: nickname,
                lobbyname: lobbyname
            }
        })
        .done(function (data)
        {
            var cardArray = JSON.parse(data);
            var cardNames = [];

            cardArray.forEach(function (item)
            {
                cardNames.push(item.name);
            });

            $("#hand").show();
            $("#handdecor").show();

            createHand(cardNames);
        });
};

var ajaxPutCardOnTable = function (cardname, callback)
{
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
        .done(function ()
        {
            if (typeof callback !== 'undefined') callback();
            ajaxCheckGameStatus();
        });
};

var ajaxPlayTreasures = function ()
{
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "playtreasures",
                lobbyname: lobbyname,
                nickname: nickname
            }
        })
        .done(function ()
        {
            ajaxRetrieveBuyableCards();
            ajaxCheckGameStatus();
        });
};

var ajaxBuyCard = function (cardname)
{
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
        .done(function ()
        {
            ajaxRetrieveBuyableCards();
            ajaxCheckGameStatus();
        });
};

var ajaxDiscardCard = function (cardname)
{
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "discardcard",
                cardname: cardname,
                lobbyname: lobbyname,
                nickname: nickname
            }
        })
        .done(function ()
        {
            ajaxRetrieveHand();
            ajaxCheckGameStatus();
        });
};