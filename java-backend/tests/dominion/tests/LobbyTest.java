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
    TestHelper testHelper;

    @Before
    public void setUp()
    {
        playerOne = "bob";
        playerTwo = "alice";
        testHelper = new TestHelper();

        lobby = new Lobby(playerOne, "mygame", testHelper.getDefaultKingdomCards(),  null);
    }

    @Test
    public void testAddPlayers()
    {

        lobby.addPlayer(playerTwo);

        assert(lobby.getPlayer("bob").equals("bob"));
        assert(lobby.getPlayer("alice").equals("alice"));
    }

    @Test
    public void testGetPlayer()
    {
        String username = "bob";
        assert(lobby.getPlayer(username) == playerOne);
    }

}