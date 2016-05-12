package dominion.tests;

import dominion.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sam on 21/04/2016.
 */
public class LobbyTest
{
    String playerOne;
    String playerTwo;
    Lobby lobby;

    @Before
    public void setUp()
    {
        playerOne = "bob";
        playerTwo = "alice";

        lobby = new Lobby(playerOne, "mygame", "mypassword", null);
    }

    @Test
    public void testAddPlayers()
    {

        lobby.addPlayer(playerTwo);

        assert(lobby.getPlayer("bob").toString().equals("bob"));
        assert(lobby.getPlayer("alice").toString().equals("alice"));
    }

    @Test
    public void testGetPlayer()
    {
        String username = "bob";
        assert(lobby.getPlayer(username) == playerOne);
    }

}