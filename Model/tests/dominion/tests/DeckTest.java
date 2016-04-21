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

        deck.addCard(new Card(gameEngine.findCard("copper")));
        deck.addCard(new Card(gameEngine.findCard("estate")));

        assert(deck.getCard("copper").toString().equals("copper 1 0 0 3 1 "));
        assert(deck.getCard("estate").toString().equals("estate 2 2 0 10 1 "));
    }

    @Test
    public void testTakeTopCard()
    {
        Deck deck = new Deck(true, gameEngine);
        Deck discardPile = new Deck(false, gameEngine);

        for (int i = 0; i < 7; i ++)
            deck.addCard(new Card(gameEngine.findCard("copper")));
        for (int i = 0; i < 3; i ++)
            deck.addCard(new Card(gameEngine.findCard("estate")));

        deck.shuffle();

        Card topCard = deck.takeTopCard(discardPile);
        Card copper = gameEngine.findCard("copper");
        Card estate = gameEngine.findCard("estate");

        assert(topCard.equals(copper) || topCard.equals(estate));
    }

    @Test
    public void testTakeTopCardWhenDeckIsEmpty()
    {
        Deck deck = new Deck(true, gameEngine);
        Deck discardPile = new Deck(false, gameEngine);
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

    }

}