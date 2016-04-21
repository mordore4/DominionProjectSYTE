package dominion.tests;

import dominion.Account;
import dominion.Game;
import dominion.GameEngine;
import dominion.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Tom Dobbelaere on 21/04/2016.
 */
public class PlayerTest
{
    private Game game;
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

        Account[] accounts = {new Account("bob", 0), new Account("alice", 0)};
        game = new Game(accounts, "default", gameEngine);
    }

    @Test
    public void testStartingCards()
    {
        Player player = game.getPlayer("bob");
        assert (player.getDeck().size() == 10);
    }
}