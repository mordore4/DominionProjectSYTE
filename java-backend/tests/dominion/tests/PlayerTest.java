package dominion.tests;

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
    private TestHelper testHelper;

    @Before
    public void setUp()
    {
        testHelper = new TestHelper();

        String[] accounts = {"bob", "alice"};
        game = new Game(accounts, testHelper.getDefaultKingdomCards(), testHelper.getTestCardList());
    }

    @Test
    public void testStartingCards()
    {
        Player player = game.getPlayer("bob");
        assert (player.getDeck().size() == 5);
    }
}