package dominion.tests;

import dominion.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Sam on 12/05/2016.
 */
public class GameTest
{
    private GameEngine gameEngine;
    private Account accountOne;
    private Account accountTwo;
    private Account[] accounts;

    private Game game;

    @Before
    public void setUp()
    {
        try
        {
            gameEngine = new GameEngine();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        accountOne = new Account("bob", 10);
        accountTwo = new Account("alice", 20);
        accounts = new Account[2];
        accounts[0] = accountOne;
        accounts[1] = accountTwo;

        game = new Game(accounts, "default", gameEngine);
    }

    @Test
    public void testAdvancePlayer()
    {
        game.setCurrentPlayerIndex(0);
        game.advancePlayer();
        assert (game.findCurrentPlayer().getAccount().equals(accountTwo));
    }
}