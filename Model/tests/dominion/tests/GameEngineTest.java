package dominion.tests;

import dominion.Account;
import dominion.Card;
import dominion.GameEngine;
import dominion.Lobby;
import dominion.exceptions.LobbyNotFoundException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Sam on 14/04/2016.
 */
public class GameEngineTest
{
    @Test
    public void testCardList()
    {
        boolean works = false;
        GameEngine ge = null;

        try
        {
            ge = new GameEngine();
        }
        catch (Exception ex)
        {

        }

        ArrayList<Card> cl = ge.getCardList();
        for (Card c : cl)
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
        GameEngine ge = null;

        try
        {
            ge = new GameEngine();
        }
        catch (Exception ex)
        {

        }

        assert(ge.findCard("smithy").toString().equals("smithy 3 4 0 4 3 "));
    }

    @Test
    public void testFindLobbyException()
    {
        GameEngine ge = null;

        try
        {
            ge = new GameEngine();
        }
        catch (Exception ex)
        {

        }

        try
        {
            ge.findLobby("test");
        }
        catch (LobbyNotFoundException ex)
        {
            return;
        }

        fail("Finding nonexistent lobby did not throw an exception.");
    }
}