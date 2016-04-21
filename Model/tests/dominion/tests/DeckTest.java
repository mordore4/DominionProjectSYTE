package dominion.tests;

import dominion.Card;
import dominion.Deck;
import dominion.GameEngine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Sam on 21/04/2016.
 */
public class DeckTest
{
    private GameEngine gameEngine;

    @Before
    public void setUp()
    {
        try
        {
            gameEngine = new GameEngine();
        }
        catch (Exception ex)
        {

        }
    }

    @Test
    public void testHand()
    {
        Deck deck = new Deck(true, gameEngine);

        assert(deck.getCard("copper").toString().equals("copper 1 0 1 3 1 "));
        assert(deck.getCard("estate").toString().equals("estate 2 2 1 10 1 "));
    }

    @Test
    public void testTakeTopCard()
    {
        Deck deck = new Deck(true, gameEngine);
        Deck discardPile = new Deck(false, gameEngine);

        Card topCard = deck.takeTopCard(discardPile);
        Card copper = gameEngine.findCard("copper");
        copper.setAmount(1);
        Card estate = gameEngine.findCard("estate");
        estate.setAmount(1);

        assert(topCard.equals(copper) || topCard.equals(estate));
    }

    @Test
    public void testTakeTopCardWhenDeckIsEmpty()
    {
        Deck deck = new Deck(true, gameEngine);
        Deck discardPile = new Deck(false, gameEngine);
        Card lastCard = null;

        Card smithy = gameEngine.findCard("smithy");
        discardPile.addCard(smithy);

        for (int i = 0; i < 11; i++)
        {
            lastCard = deck.takeTopCard(discardPile);
        }
        assert(lastCard.equals(smithy));

    }

}