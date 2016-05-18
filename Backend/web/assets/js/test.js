/**
 * Created by Digaly on 18/05/2016.
 */

$(document).ready(function() {
    playGame();
    $("#kingdomcards").show();
    $("#hand").show();
    addKingdomCards(kingdomCards);
    createHand(tempHand);
    fixedCardsTest = [{name: "copper"}, {name: "silver"}, {name: "gold"}, {name: "curse"}, {name: "province"}, {name: "duchy"}, {name: "estate"}];
    addFixedCards(fixedCardsTest);
});