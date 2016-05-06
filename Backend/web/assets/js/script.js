var tempHand = ["Adventurer", "Bureaucrat", "Cellar", "Chancellor", "Chapel", "Copper", "Council_Room"];
var kingdomCards = ["Militia", "Remodel", "Smithy", "Market", "Mine", "Cellar", "Moat", "Village", "Woodcutter", "Workshop"];
var fixedCards = ["copper", "silver", "gold", "curse", "province", "duchy", "estate"];

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
        items: "li:not('#cardsComeCenter li')"
    });

    $("label[for=state_id]").parent().load("assets/fragments/states.html");
    $('#play').on('click', playGame);
});

var playGame = function (e) {
    e.preventDefault();

    addFixedCards();
    addKingdomCards(kingdomCards);
    createHand(tempHand);

    $('body').addClass("game");

    $('#menu').hide();
    $("div.mastfoot").hide();

    $("#gamewindow").show();
    $('#cardsComeCenter').show();
};

var createHand = function (cardsArray) {
    for (var i = 0, len = cardsArray.length; i < len; i++) {
        var imageSource = "images/cards/" + cardsArray[i] + ".jpg";
        var html = '<li style="background-image: url(' + imageSource + ');">';

        $('#hand').append(html);
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