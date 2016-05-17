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
    private Deck hand;
    private Ability[] abilities;
    private Card testCard1;
    private Card testCard2;
    private Card testCard3;

    @Before
    public void setUp()
    {
        deck = new Deck();
        discardPile = new Deck();
        hand = new Deck();

        Ability copperAbility = new Ability(3, 1);
        abilities = new Ability[1];
        abilities[0] = copperAbility;

        testCard1 = new Card("testCard1", 1, 1, 1, abilities);
        testCard2 = new Card("testCard2", 2, 2, 2, abilities);
        testCard3 = new Card("testCard3", 3, 3, 3, abilities);
    }

    @Test
    public void testDeck()
    {
        deck.addCard(testCard1);
        deck.addCard(testCard2);

        assert(deck.findCard("testCard1").toString().equals("testCard1 1 1 1 3 1 "));
        assert(deck.findCard("testCard2").toString().equals("testCard2 2 2 2 3 1 "));
    }

    @Test
    public void testTakeTopCard()
    {
        Deck deck = new Deck();
        Deck discardPile = new Deck();

        for (int i = 0; i < 7; i ++)
            deck.addCard(new Card(testCard1));
        for (int i = 0; i < 3; i ++)
            deck.addCard(new Card(testCard2));

        deck.shuffle();

        deck.takeTopCard(hand, discardPile);

        assert(hand.getTopCard().equals(testCard1) || hand.getTopCard().equals(testCard2));
    }

    @Test
    public void testTakeTopCardWhenDeckIsEmpty()
    {
        Deck deck = new Deck();
        Deck discardPile = new Deck();
        Card lastCard = null;

        for (int i = 0; i < 7; i ++)
            deck.addCard(new Card(testCard1));

        for (int i = 0; i < 3; i ++)
            deck.addCard(new Card(testCard2));

        discardPile.addCard(new Card(testCard3));

        for (int i = 0; i < 11; i++)
        {
            deck.takeTopCard(hand,discardPile);
        }

        lastCard = hand.getTopCard();
        assert(lastCard.equals(testCard3));
    }

}
