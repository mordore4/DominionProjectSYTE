package dominion.tests;

import dominion.*;

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

    private Game game;

    @Before
    public void setUp()
    {
        accountOne = "bob";
        accountTwo = "alice";
        accounts = new String[2];
        accounts[0] = accountOne;
        accounts[1] = accountTwo;

        game = new Game(accounts, "default", TestHelper.getTestCardList());
    }

    @Test
    public void testAdvancePlayer()
    {
        game.setCurrentPlayerIndex(0);
        game.advancePlayer();
        assert (game.findCurrentPlayer().getName().equals(accountTwo));
    }

    @Test
    public void testIsBuyable()
    {
        game.setCurrentPlayerIndex(0);

        Player currentPlayer = game.findCurrentPlayer();

        assert(!game.isBuyable(game.findCard("cellar")));

        currentPlayer.setCoins(2);

        assert(game.isBuyable(game.findCard("cellar")));
    }

    @Test
    public void testFindBuyableCards()
    {
        game.setCurrentPlayerIndex(0);

        Player currentPlayer = game.findCurrentPlayer();
        currentPlayer.setCoins(2);

        String[] buyables = {"cellar", "moat", "estate", "curse", "copper"};

        assert(Arrays.equals(game.findBuyableCards().toArray(), buyables));
    }

    @Test
    public void testAddCard()
    {
        game.setCurrentPlayerIndex(0);

        Player currentPlayer = game.findCurrentPlayer();

        try
        {
            game.addCard("cellar");
        }
        catch (Exception ex)
        {

        }

        assert(currentPlayer.getDiscardPile().findCard("cellar") != null);
    }

    @Test
    public void testBuyCard()
    {
        game.setCurrentPlayerIndex(0);

        Player currentPlayer = game.findCurrentPlayer();
        currentPlayer.setCoins(2);

        try
        {
            game.buyCard("cellar");
        }
        catch (Exception ex)
        {

        }

        assert(currentPlayer.getCoins() == 0);
        assert(currentPlayer.getBuys() == 0);
        assert(currentPlayer.getDiscardPile().findCard("cellar") != null);
    }
}