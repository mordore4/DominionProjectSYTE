import dominion.*;
import dominion.exceptions.*;
import java.util.Scanner;

/**
 * Created by Digaly on 14/04/2016.
 */
public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Dominion SYTE Console frontend");
        System.out.println("------------------------------");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Player 1, what is your name?");
        String name = scanner.nextLine();

        Account player1 = new Account(name, 0);

        System.out.println("Player 2, what is your name?");
        name = scanner.nextLine();

        Account player2 = new Account(name, 0);

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



        gameEngine.createLobby(player1, "mygame", "mypassword");

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

        lobby.addPlayer(player2);
        //ge.findLobby("mygame").addPlayer(player2);

        lobby.startGame();

        Game currentGame = lobby.getGame();





    }
}
