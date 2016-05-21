/**
 * Created by Digaly on 18/05/2016.
 */
var kingdomCards = [{name: "militia"}, {name: "remodel"}, {name: "smithy"}, {name: "market"}, {name: "mine"},
                    {name: "cellar"}, {name: "moat"}, {name: "village"}, {name: "woodcutter"}, {name: "workshop"}];

var fixedCards = [{name: "copper"}, {name: "silver"}, {name: "gold"}, {name: "curse"}, {name: "province"}, {name: "duchy"}, {name: "estate"}];
var tempHand = ["copper", "copper", "estate", "copper", "estate"];


$(document).ready(function() {
    playGame();
    $("#kingdomcards").show();
    $("#handdecor").show();
    $("#hand").show();
    addKingdomCards(kingdomCards);
    createHand(tempHand);
    addFixedCards(fixedCards);

    for (card in kingdomCards)
    {
        setCardAmount(kingdomCards[card].name, 10);
        setCardBuyable(kingdomCards[card].name, true);
    }


    for (card in fixedCards)
    {
        setCardAmount(fixedCards[card].name, 10);
        setCardBuyable(fixedCards[card].name, true);
    }

});