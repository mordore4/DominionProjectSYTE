/**
 * Created by Yannis on 1-12-2015.
 */

var active = false;
var kaarten = ["Adventurer", "Bureaucrat", "Cellar", "Chancellor", "Chapel", "Copper", "Council_Room", "Curse", "Duchy", "Estate"];
var huidigeAfbeelding = 1;

var toevoegenAfbeeldingen = function () {
    for (var i = 0, len = kaarten.length; i < len; i++) {

        var imageSource = "images/" + kaarten[i] + ".jpg";
        var html = '<li>';
        html += '<figure><img alt="' + kaarten[i] + '" title="' + kaarten[i] + '" src="' + imageSource + '" />';
        /*html += '<figcaption>' + kaarten[i] + '</figcaption></figure></li>';*/

        $('.carousel').append(html);
    }

};

$(document).ready(function () {

    console.log("De verbinding werkt");
    $('#play').on('click', playGame);
    toevoegenAfbeeldingen();
    $('#cards').hide();
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
    })
    $('#menu').hide();
    $('#cards').show();

    /* var goTo = this.getAttribute("href");

     window.location = goTo;


     setTimeout(function () {
     window.location = goTo;
     }, 3000);*/
};
