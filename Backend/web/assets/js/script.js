/**
 * Created by Yannis on 1-12-2015.
 */

var active = false;
//var kaarten = ["Adventurer", "Bureaucrat", "Cellar", "Chancellor", "Chapel", "Copper", "Council_Room", "Curse", "Duchy", "Estate"];
var kaarten = ["Adventurer", "Copper", "Copper", "Copper", "Copper", "Copper", "Copper"];
var coinsTop = ["Coppertop","Silvertop","Goldtop","Cursetop"];
var StatesTop = ["provinceTop","dutchyTop","estateTop"];

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
    for (var i= 0, len = coinsTop.length; i < len; i++) {

        var imgsrc = "images/" + coinsTop[i] + ".png";

        var html = '<li>'
        html += '<figure><img alt="' + coinsTop[i] + '" title="' + coinsTop[i] + '" src="' + imgsrc  + '" />';
        /*html += '<figcaption>' + coinsTop[i] + '</figcaption></figure></li>'*/

        $('#coins').append(html);
    }
    $('#coins li:first').show();
};

var toevoegenStatesTop = function () {
    for (var i= 0, len = StatesTop.length; i < len; i++) {

        var imgsrc = "images/" + StatesTop[i] + ".png";

        var html = '<li>'
        html += '<figure><img alt="' + StatesTop[i] + '" title="' + StatesTop[i] + '" src="' + imgsrc  + '" />';
        /*html += '<figcaption>' + coinsTop[i] + '</figcaption></figure></li>'*/

        $('#topstates').append(html);
    }
    $('#topstates li:first').show();
};


$(document).ready(function () {

    console.log("De verbinding werkt");
    $('#play').on('click', playGame);
    toevoegenAfbeeldingen();
    $('#hand').hide();
    $('#coins').hide();
    $('#topstates').hide();
    $("#gamewindow").hide();
    $('#hand li').on('click', function () {
        console.log("FUCKER");
        $(this).find('img').css('top', '100px');
        //$(this).css('height', '180');

    });

    /*$('#hand li').on('mouseenter', function () {
     $(this).addClass("hoveredcard");
     });

     $('#hand li').on('mouseleave', function () {
     $(this).removeClass("hoveredcard");
     });*/

    $("#hand").sortable();

    $('#hand li').draggable({
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
    });
    toevoegenAfbeeldingenTop();
    toevoegenStatesTop();
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

