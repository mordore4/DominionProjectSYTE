/**
 * Created by Yannis on 1-12-2015.
 */

var active = false;


$(document).ready(function () {

    console.log("De verbinding werkt");
    $('#play').on('click', playGame);

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

   /* var goTo = this.getAttribute("href");

    window.location = goTo;


    setTimeout(function () {
        window.location = goTo;
    }, 3000);*/
};
