package dominion.tests;

import dominion.Ability;
import dominion.Card;
import dominion.Deck;
import dominion.GameEngine;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Sam on 21/04/2016.
 */
public class DeckTest
{
    private Deck deck;
    private Deck discardPile;
    private Ability[] copperAbilities;
    private Card copper;
    private Ability[] estateAbilities;
    private Card estate;

    @Before
    public void setUp()
    {
        deck = new Deck();
        discardPile = new Deck();
        Ability copperAbility = new Ability(3, 1);
        copperAbilities = new Ability[1];
        copperAbilities[0] = copperAbility;
        copper = new Card("copper", 1, 0, 0, copperAbilities);

        Ability estateAbility = new Ability(10, 1);
        estateAbilities = new Ability[1];
        estateAbilities[0] = estateAbility;
        estate = new Card("estate", 2, 2, 0, estateAbilities);
    }

    @Test
    public void testHand()
    {
        deck.addCard(copper);
        deck.addCard(estate);

        assert(deck.findCard("copper").toString().equals("copper 1 0 0 3 1 "));
        assert(deck.findCard("estate").toString().equals("estate 2 2 0 10 1 "));
    }

    /*@Test
    public void testTakeTopCard()
    {
        Deck deck = new Deck();
        Deck discardPile = new Deck();

        for (int i = 0; i < 7; i ++)
            deck.addCard(new Card(gameEngine.findCard("copper")));
        for (int i = 0; i < 3; i ++)
            deck.addCard(new Card(gameEngine.findCard("estate")));

        deck.shuffle();

        Card topCard = deck.takeTopCard(discardPile);
        Card copper = gameEngine.findCard("copper");
        Card estate = gameEngine.findCard("estate");

        assert(topCard.equals(copper) || topCard.equals(estate));
    }*/

    /*@Test
    public void testTakeTopCardWhenDeckIsEmpty()
    {
        Deck deck = new Deck();
        Deck discardPile = new Deck();
        Card lastCard = null;

        for (int i = 0; i < 7; i ++)
            deck.addCard(new Card(gameEngine.findCard("copper")));

        for (int i = 0; i < 3; i ++)
            deck.addCard(new Card(gameEngine.findCard("estate")));

        Card smithy = new Card(gameEngine.findCard("smithy"));
        discardPile.addCard(smithy);

        for (int i = 0; i < 11; i++)
        {
            lastCard = deck.takeTopCard(discardPile);
        }
        assert(lastCard.equals(smithy));

    }*/

}