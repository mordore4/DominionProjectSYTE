package dominion.tests;

import dominion.*;

import dominion.exceptions.CardNotAvailableException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

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
        game.advancePhase();
        assert (game.findCurrentPlayer().getName().equals(accountTwo));
    }

    @Test
    public void testIsBuyable()
    {
        Player currentPlayer = game.findCurrentPlayer();

        assert(!game.isBuyable(game.findCard("cellar")));

        currentPlayer.setCoins(2);

        assert(game.isBuyable(game.findCard("cellar")));
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

    @Test
    public void testCardsetFromDatabase()
    {
        game = new Game(accounts, "first game", testHelper.getTestCardList());

        assert(game.findCard("mine") != null);
    }
}