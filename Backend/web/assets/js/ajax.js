var timeOut = null;

var ajaxGetCardsets = function () {
    $.ajax
        ({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "getcardsets"
            }
        })
        .done(function (data) {
            var cardSets = JSON.parse(data);

            addCardSets(cardSets);
        });
};

var ajaxCreateLobby = function () {
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
        .done(function () {
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

var ajaxRetrieveKingdomCards = function () {
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

var ajaxRetrieveBuyableCards = function () {
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

            for (var card in buyableCards) {
                currentCardName = buyableCards[card][0];
                setCardBuyable(currentCardName, buyableCards[card][2]);
                setCardAmount(currentCardName, buyableCards[card][1])
            }
        });
};

var ajaxEndTurn = function () {
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
            ajaxCheckGameStatus();
            clearBuyableCards();
            ajaxRetrieveHand();
        });
};

var ajaxEndActions = function () {
    $.ajax({
            method: "GET",
            url: "server/gamemanager",
            data: {
                command: "endactions",
                lobbyname: lobbyname,
                nickname: nickname
            }
        })
        .done(function (data) {
            ajaxCheckGameStatus();
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
            if (!isMyTurn && (phase == 1 || phase == 3)) {
                ajaxRetrieveBuyableCards();
            }

            //This is the first time we notice it's our turn
            //So this can be seen as stuff to do on turn start
            if (!isMyTurn && status.isMyTurn) {
                $("body").addClass("myturn");
                $("#cardsComeCenter").empty();
                clearBuyableCards();
                $("#hand").sortable("enable");
            }

            isMyTurn = status.isMyTurn;

            if (isMyTurn) { //Seriously what ?
                if (phase == 1)
                {
                    $("#play-treasures").show();
                }
                else
                {
                    $("#play-treasures").hide();
                }

                if (phase == 0) {
                    $("#end-actions").show();
                }
                else {
                    $("#end-turn").show();
                    $("#end-actions").hide();
                }

                if (status.phase == 3)
                {
                    if (status.buys == 0 || status.coins == 0)
                        ajaxEndTurn();
                }
            }
            else
            {
                $("#end-actions").hide();
                $("#play-treasures").hide();
                $("#end-turn").hide();
            }

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

            if (timeOut != null) clearTimeout(timeOut);
            if (!isMyTurn) timeOut = setTimeout(ajaxCheckGameStatus, pollInterval);
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
        .done(function () {
            if (typeof callback !== 'undefined') callback();
            ajaxCheckGameStatus();
        });
};

var ajaxBuyCard = function (cardname) {
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
        .done(function () {
            ajaxRetrieveBuyableCards();
            ajaxCheckGameStatus();
        });
};