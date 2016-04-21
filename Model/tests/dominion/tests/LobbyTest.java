package dominion.tests;

import dominion.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sam on 21/04/2016.
 */
public class LobbyTest
{

    @Test
    public void testAddPlayers()
    {
        Account playerOne = new Account("bob", 10);
        Account playerTwo = new Account("alice", 20);

        Lobby newLobby = new Lobby(playerOne, "mygame", "mypassword", null);

        newLobby.addPlayer(playerTwo);

        assert(newLobby.getPlayer("bob").toString().equals("bob 10"));
        assert(newLobby.getPlayer("alice").toString().equals("alice 20"));
    }
}