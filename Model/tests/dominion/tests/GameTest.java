package dominion.tests;

import dominion.*;

import dominion.exceptions.CardNotAvailableException;
import dominion.util.GainCardCondition;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Sam on 12/05/2016.
 */
public class GameTest
{
    private String accountOne;
    private String accountTwo;
    private String[] accounts;
    private TestHelper testHelper;

    private Game game;

    @Before
    public void setUp()
    {
        accountOne = "bob";
        accountTwo = "alice";
        accounts = new String[2];
        accounts[0] = accountOne;
        accounts[1] = accountTwo;
        testHelper = new TestHelper();

        game = new Game(accounts, testHelper.getDefaultKingdomCards(), testHelper.getTestCardList());
        game.setCurrentPlayerIndex(0);
    }

    @Test
    public void testAdvancePlayer()
    {
        game.advancePlayer();
        assert (game.findCurrentPlayer().getName().equals(accountTwo));
    }

    @Test
    public void testAdvancePhase()
    {
        assert(game.getPhase() == 1);

        game.playTreasures();

        assert(game.getPhase() == 2);

        try
        {
            game.buyCard("copper");
        }
        catch (CardNotAvailableException ex)
        {

        }

        game.advancePhase();

        assert (game.findCurrentPlayer().getName().equals(accountTwo));
    }

    @Test
    public void testIsBuyable()
    {
        Player currentPlayer = game.findCurrentPlayer();

        assert(!game.isBuyable(game.retrieveCard("cellar")));

        currentPlayer.setCoins(2);

        assert(game.isBuyable(game.retrieveCard("cellar")));
    }

    @Test
    public void testFindBuyableCards()
    {
        Player currentPlayer = game.findCurrentPlayer();
        currentPlayer.setCoins(2);

        String[] buyables = {"cellar", "moat", "estate", "curse", "copper"};

        assert(Arrays.equals(game.findBuyableCards().toArray(), buyables));
    }

    @Test
    public void testAddCard()
    {
        Player currentPlayer = game.findCurrentPlayer();

        try
        {
            game.addCard("cellar");
        }
        catch (CardNotAvailableException ex)
        {
            ex.printStackTrace();
        }

        assert(currentPlayer.getDiscardPile().findCard("cellar") != null);
    }

    @Test
    public void testBuyCard()
    {
        Player currentPlayer = game.findCurrentPlayer();
        currentPlayer.setCoins(2);

        try
        {
            game.buyCard("cellar");
        }
        catch (CardNotAvailableException ex)
        {
            ex.printStackTrace();
        }

        assert(currentPlayer.getCoins() == 0);
        assert(currentPlayer.getBuys() == 0);
        assert(currentPlayer.getDiscardPile().findCard("cellar") != null);
    }

    @Test
    public void testGainCardCostingUpTo()
    {
        Player currentPlayer = game.findCurrentPlayer();

        try
        {
            game.gainCardCostingUpTo("moat", 2);
        }
        catch (CardNotAvailableException ex)
        {
            ex.printStackTrace();
        }

        assert(currentPlayer.getDiscardPile().findCard("moat") != null);
    }

    @Test
    public void testDiscardCard()
    {
        Player currentPlayer = game.findCurrentPlayer();

        currentPlayer.getHand().addCard(game.retrieveCard("moat"));

        assert(currentPlayer.getHand().findCard("moat") != null);

        game.discardCard(currentPlayer.getHand().findCard("moat"));

        assert(currentPlayer.getHand().findCard("moat") == null);
    }

    /*@Test
    public void testCardsetFromDatabase()
    {
        game = new Game(accounts, "first game", testHelper.getDefaultKingdomCards(), testHelper.getTestCardList());

        assert(game.retrieveCard("mine") != null);
    }*/

    @Test
    public void testMoveThisCardFromTo()
    {
        Card testCard = new Card("testCard", 0, 0, 0, null);
        Deck discardPile = game.findCurrentPlayer().getDiscardPile();
        ArrayList<Card> randomList = new ArrayList<>();
        randomList.add(testCard);
        boolean cardIsInRandomListFirst = randomList.contains(testCard);
        boolean cardDidNotStartInDiscardPile = !discardPile.getCards().contains(testCard);
        game.moveThisCardFromTo(testCard, randomList, discardPile.getCards());
        boolean cardLeftRandomList = !randomList.contains(testCard);
        boolean cardWentToDiscard = discardPile.getCards().contains(testCard);

        assert (cardIsInRandomListFirst && cardDidNotStartInDiscardPile && cardLeftRandomList && cardWentToDiscard);
    }

    @Test
    public void testGainCardCondition()
    {
        try
        {
            game.addCard("workshop");
        }
        catch (CardNotAvailableException e)
        {
            e.printStackTrace();
        }
        game.setPhase(0);

        game.findCurrentPlayer().setCoins(20);

        GainCardCondition newCondition = new GainCardCondition(game.findCurrentPlayer(), game, 2);
        Card testCard = new Card("testCard", 3, 8, 1, null);

        assertTrue(game.isBuyable(testCard));

        game.addCondition(newCondition);

        assertFalse(game.isBuyable(testCard));
        assert(game.getPhase() == 2);
        assert(game.isBuyable(game.retrieveCard("moat")));

        try
        {
            game.buyCard("moat");
        }
        catch (CardNotAvailableException e)
        {
            e.printStackTrace();
        }

        assert(game.findCurrentPlayer().getDiscardPile().findCard("moat") != null);
        assert(game.getConditionsList().size() == 0);
        assert(game.getPhase() == 1);
    }

    @Test
    public void TestWorkshop()
    {
        game.findCurrentPlayer().getHand().addCard(game.retrieveCard("workshop"));
        game.findCurrentPlayer().setCoins(20);
        game.setPhase(0);

        try
        {
            game.playCard("workshop");
        }
        catch (CardNotAvailableException e)
        {
            e.printStackTrace();
        }

        assertTrue(game.getPhase() == 2);
        assertFalse(game.isBuyable(game.retrieveCard("market")));
        assertTrue(game.isBuyable(game.retrieveCard("smithy")));

        try
        {
            game.buyCard("smithy");
        }
        catch (CardNotAvailableException e)
        {
            e.printStackTrace();
        }

        assertTrue(game.getPhase() == 1);
    }
}