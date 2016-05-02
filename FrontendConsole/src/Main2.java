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

        CliController cliController = new CliController();
        cliController.printTitle();

        cliController.setUpLobby();
        cliController.startGame();

        cliController.gamePlay();
    }
}
