/**
 * Created by Yannis on 1-12-2015.
 */

var active = false;
//var kaarten = ["Adventurer", "Bureaucrat", "Cellar", "Chancellor", "Chapel", "Copper", "Council_Room", "Curse", "Duchy", "Estate"];
var kaarten = ["Copper", "Copper", "Copper", "Copper", "Copper", "Copper", "Copper"];

var huidigeAfbeelding = 1;

var toevoegenAfbeeldingen = function () {
    var xoffset = 0;
    var angle = 0;

    for (var i = 0, len = kaarten.length; i < len; i++) {
        angle = i * 4;

        var imageSource = "images/" + kaarten[i] + ".jpg";
        /*var html = '<li style="left: ' + xoffset + 'px; z-index: ' + (kaarten.length - i) + '; transform: rotate(' + angle + 'deg) translate(0px , ' + (angle * 5) + 'px)">';*/
        var html = '<li style="left: ' + xoffset + 'px; z-index: ' + (kaarten.length - i) + ';">';

        html += '<figure><img alt="' + kaarten[i] + '" title="' + kaarten[i] + '" src="' + imageSource + '" />';
        /*html += '<figcaption>' + kaarten[i] + '</figcaption></figure></li>';*/

        $('.carousel').append(html);

        xoffset += 80;
    }

};

$(document).ready(function () {

    console.log("De verbinding werkt");
    $('#play').on('click', playGame);
    toevoegenAfbeeldingen();
    $('#hand').hide();
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

    $('#hand li').draggable({
        revert: true,
        zIndex: 1000,
        revertDuration: 500,
        scroll: false,
        start: function () {
            $(this).addClass("hoveredcard");
        },
        stop: function() {
            $(this).removeClass("hoveredcard");
        }
    });

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

