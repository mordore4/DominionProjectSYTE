/**
 * Created by Yannis on 1-12-2015.
 */

var i = 0;

$( document ).ready(function() {

     $( "ul.carousel li:first-child" ).show();



$('ul.sequence li:last-of-type a').on ("click",function(e){

    e.preventDefault();

    i+=1;

    if (i > 7) {
        i = 0;
    }
    console.log(i);

    $('ul.carousel li:nth-of-type(' + (i - 1) + ')').fadeOut(200);
    $('ul.carousel li:nth-of-type(' + (i) + ')').fadeIn(600);
});
    $('ul.sequence li:first-of-type a').on ("click",function(e){
        e.preventDefault();
        console.log("werkt dit?");
        i-=1;

        if (i < 0) {
            i = 7;
        }
        console.log(i);

        $('ul.carousel li:nth-of-type(' + (i + 1) + ')').fadeOut(200);
        $('ul.carousel li:nth-of-type(' + (i) + ')').fadeIn(600);

});
});

