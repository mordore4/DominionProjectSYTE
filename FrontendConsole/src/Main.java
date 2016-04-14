import dominion.*;

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
        }

        Lobby newLobby = gameEngine.createLobby(bob, "mygame", "mypassword");
        newLobby.addPlayer(alice);
        //ge.findLobby("mygame").addPlayer(alice);

        newLobby.startGame();

               

    }
}
