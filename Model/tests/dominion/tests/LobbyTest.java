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
    Account playerOne;
    Account playerTwo;
    Lobby lobby;

    @Before
    public void setUp()
    {
        playerOne = new Account("bob", 10);
        playerTwo = new Account("alice", 20);

        lobby = new Lobby(playerOne, "mygame", "mypassword", null);
    }

    @Test
    public void testAddPlayers()
    {

        lobby.addPlayer(playerTwo);

        assert(lobby.getPlayer("bob").toString().equals("bob 10"));
        assert(lobby.getPlayer("alice").toString().equals("alice 20"));
    }

    @Test
    public void testGetPlayer()
    {
        String username = "bob";
        assert(lobby.getPlayer(username) == playerOne);
    }

}