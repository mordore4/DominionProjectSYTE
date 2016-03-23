/**
 * Created by Yannis on 1-12-2015.
 */



$(document).ready(function () {

    console.log("De verbinding werkt");
    $('#play').on('click', playGame);

});


$body = $("body");

$(document).on({
    ajaxStart: function () {
        $body.addClass("loading");
    },
    ajaxStop: function () {
        setTimeout(stopLoading, 1500);
    }
});

var stopLoading = function() {
    $body.removeClass("loading");
};

var playGame = function() {
    $.ajax({
        url: "index.html",
        method: "GET",
        data: "",
        dataType: "html",
        success: function(data) {
            console.log(data);
        }
    })
};