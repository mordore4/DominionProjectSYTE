import dominion.*;
import dominion.controllers.CliController;
import dominion.exceptions.LobbyNotFoundException;

/**
 * Created by Digaly on 14/04/2016.
 */
public class Main2
{
    public static void main(String[] args)
    {
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

        CliController cliController = new CliController(gameEngine);
        cliController.printTitle();

        cliController.setUpLobby();
        cliController.startGame();


    }
}
