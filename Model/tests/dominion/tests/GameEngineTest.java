package dominion.tests;

import dominion.Card;
import dominion.GameEngine;
import dominion.Lobby;
import dominion.exceptions.LobbyNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Sam on 14/04/2016.
 */
public class GameEngineTest
{
    private GameEngine gameEngine;

    @Before
    public void setUp() throws Exception
    {
        try
        {
            gameEngine = new GameEngine();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Test
    public void testCardList()
    {
        boolean works = false;

        for (Card c : gameEngine.getCardList().values())
        {
            if (c.toString().equals("village 3 3 0 1 2 4 1 "))
            {
                works = true;
            }
        }

        assertTrue(works);
    }

    @Test
    public void testFindCard()
    {
        assert(gameEngine.findCard("smithy").toString().equals("smithy 3 4 0 4 3 "));
    }

    @Test
    public void testFindLobbyException()
    {
        try
        {
            gameEngine.findLobby("test");
        }
        catch (LobbyNotFoundException ex)
        {
            return;
        }

        fail("Finding nonexistent lobby did not throw an exception.");
    }
}