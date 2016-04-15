import dominion.*;
import dominion.Exceptions.*;

import java.util.ArrayList;

/**
 * Created by Digaly on 14/04/2016.
 */
public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Dominion SYTE Console frontend");
        System.out.println("------------------------------");

        Account bob = new Account("bob", 0);
        Account alice = new Account("alice", 0);

        GameEngine gameEngine = null;

        try
        {
            gameEngine = new GameEngine();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return;
        }

        gameEngine.createLobby(bob, "mygame", "mypassword");

        Lobby lobby = null;

        try
        {
            lobby = gameEngine.findLobby("mygame");
        }
        catch (LobbyNotFoundException ex)
        {
            System.out.println("That lobby wasn't found.");
            return;
        }

        lobby.addPlayer(alice);
        //ge.findLobby("mygame").addPlayer(alice);

        lobby.startGame();

        Game currentGame = lobby.getGame();


    }
}
