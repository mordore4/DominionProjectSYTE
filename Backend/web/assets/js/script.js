/**
 * Created by Yannis on 1-12-2015.
 */

var active = false;
//var kaarten = ["Adventurer", "Bureaucrat", "Cellar", "Chancellor", "Chapel", "Copper", "Council_Room", "Curse", "Duchy", "Estate"];
var kaarten = ["Adventurer", "Bureaucrat", "Cellar", "Chancellor", "Chapel", "Copper", "Council_Room"];
var coinsTop = ["Coppertop", "Silvertop", "Goldtop", "Cursetop"];
var StatesTop = ["provinceTop", "dutchyTop", "estateTop"];
var kingdomCards = ["Militia", "Remodel", "Smithy", "Market", "Mine", "Cellar", "Moat", "Village", "Woodcutter", "Workshop"];

var huidigeAfbeelding = 1;
var huidigeAfbeeldingTop = 1;

var toevoegenAfbeeldingen = function () {
    var xoffset = 0;
    var angle = 0;

    for (var i = 0, len = kaarten.length; i < len; i++) {
        angle = i * 4;

        var imageSource = "images/" + kaarten[i] + ".jpg";
        /*var html = '<li style="left: ' + xoffset + 'px; z-index: ' + (kaarten.length - i) + '; transform: rotate(' + angle + 'deg) translate(0px , ' + (angle * 5) + 'px)">';*/
        /*var html = '<li style="left: ' + xoffset + 'px; z-index: ' + (kaarten.length - i) + ';">';*/
        var html = '<li style="background-image: url(' + imageSource + ');">';


        //html += '<figure><img alt="' + kaarten[i] + '" title="' + kaarten[i] + '" src="' + imageSource + '" />';
        /*html += '<figcaption>' + kaarten[i] + '</figcaption></figure></li>';*/

        $('#hand').append(html);

        xoffset += 80;
    }

};


var toevoegenAfbeeldingenTop = function () {
    for (var i = 0, len = coinsTop.length; i < len; i++) {

        var imgsrc = "images/" + coinsTop[i] + ".png";

        var html = '<li>';
        html += '<figure><img alt="' + coinsTop[i] + '" title="' + coinsTop[i] + '" src="' + imgsrc + '" />';
        /*html += '<figcaption>' + coinsTop[i] + '</figcaption></figure></li>'*/

        $('#coins').append(html);
    }
    $('#coins li:first').show();
};

var toevoegenStatesTop = function () {
    for (var i = 0, len = StatesTop.length; i < len; i++) {

        var imgsrc = "images/" + StatesTop[i] + ".png";

        var html = '<li>';
        html += '<figure><img alt="' + StatesTop[i] + '" title="' + StatesTop[i] + '" src="' + imgsrc + '" />';
        /*html += '<figcaption>' + coinsTop[i] + '</figcaption></figure></li>'*/

        $('#topstates').append(html);
    }
    $('#topstates li:first').show();
};

var toevoegenKingdomCards = function () {

    for (var i = 0, len = kingdomCards.length; i < len; i++) {
        var cardName = kingdomCards[i];

        var html = '<li>';
        html += '<div class="kingdomcard">';
        html += '<div class="kingdomcard-top" style="background-image: url(images/' + cardName + '.jpg);"></div>';
        html += '<div class="kingdomcard-bottom" style="background-image: url(images/' + cardName + '.jpg);"></div>';
        html += '</div>';
        html += '</li>';

        $("#kingdomcards ul").append(html);
    }


};

$(document).ready(function () {

    console.log("De verbinding werkt");
    $('#play').on('click', playGame);
    toevoegenAfbeeldingen();
    $('#hand').hide();
    $('#coins').hide();
    $('#topstates').hide();
    $('#cardsComeCenter').hide();
    $("#gamewindow").hide();
    $('#hand li').on('click', function () {
        $(this).find('img').css('top', '100px');
        //$(this).css('height', '180');

    });

    /*$('#hand li').on('mouseenter', function () {
     $(this).addClass("hoveredcard");
     });

     $('#hand li').on('mouseleave', function () {
     $(this).removeClass("hoveredcard");
     });*/

    $("#hand").sortable({
        revert: true,
        connectWith: "#cardsComeCenter"
        //axis: "x"
    });

    $('#cardsComeCenter').sortable({
        handle: "none",
        placeholder: false,
        items: "li:not('#cardsComeCenter li')"
        /*placeholder: "placeholder"*/
    });

    /*$('#hand li').draggable({
     revert: true,
     zIndex: 1000,
     revertDuration: 500,
     connectToSortable: "#sortable",
     scroll: false,
     start: function () {
     $(this).addClass("hoveredcard");
     },
     stop: function() {
     $(this).removeClass("hoveredcard");
     }
     });*/
    toevoegenAfbeeldingenTop();
    toevoegenStatesTop();
    toevoegenKingdomCards();

    /*objectDragen();*/
});


$body = $("body");

$(document).on({
    ajaxStart: function () {
        if (active) $body.addClass("loading");
    },
    ajaxStop: function () {
        if (active) setTimeout(stopLoading, 2000);
        active = false;
    }
});

var stopLoading = function () {
    $body.removeClass("loading");
};

var playGame = function (e) {
    $('#coins').show();
    $('#topstates').show();
    $("#gamewindow").show();
    $('#cardsComeCenter').show();
    //$('body').css({background : 'url(images/daszeker.jpg) no-repeat '});
    e.preventDefault();

    /*active = true;*/

    $.ajax({
        url: "index.html",
        method: "GET",
        data: "",
        dataType: "html",
        success: function (data) {
            console.log(data);
        }
    });

    $('#menu').hide();
    $('#hand').show();
    $("div.mastfoot").hide();

    /* var goTo = this.getAttribute("href");

     window.location = goTo;


     setTimeout(function () {
     window.location = goTo;
     }, 3000);*/
};

/*var objectDragen = function(){
 $("#cards li").draggable();

 }
 */

