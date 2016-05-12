package dominion.tests;

import dominion.*;

import org.junit.Before;
import org.junit.Test;

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
}