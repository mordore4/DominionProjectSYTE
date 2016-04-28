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

        Lobby lobby = cliController.setUpLobby();
        Game currentGame = cliController.startGame(lobby);

        while (!currentGame.getIsOver())
        {
            Player currentPlayer = currentGame.findCurrentPlayer();

            System.out.println(currentPlayer.getAccount().getName() + "'s turn starts.");


            switch (currentGame.getPhase())
            {
                case 0:
                    System.out.println("ACTION phase");
                    break;
                case 1:
                    System.out.println("\tBUY phase");
                    System.out.println();
                    System.out.println("Your hand:");
                    for (Card c : currentPlayer.getHand().getCards())
                    {
                        System.out.println(c.getName());
                    }
                    System.out.println();
                    System.out.println("Available kingdom cards:");
                    for (int i = 0; i < currentGame.getKingdomCards().length; i++)
                    {
                        Card currentCard = currentGame.getKingdomCards()[i];

                        if (currentCard.getAmount() > 0)
                        {
                            System.out.print(String.format("%-24s",
                                    currentCard.getName() + "(" + currentCard.getAmount() + "x/" + currentCard.getCost() + "c)"));
                        } else
                        {
                            System.out.print(String.format("%-24s", ""));
                        }

                        if (i == 4) System.out.println();
                    }
                    System.out.println();
                    System.out.println();
                    System.out.println("Available victory/treasure cards:");
                    for (int i = currentGame.getFixedCards().length - 1; i >= 0; i--)
                    {
                        Card currentCard = currentGame.getFixedCards()[i];

                        if (currentCard.getAmount() > 0)
                        {
                            System.out.print(String.format("%-24s",
                                    currentCard.getName() + "(" + currentCard.getAmount() + "x/" + currentCard.getCost() + "c)"));
                        } else
                        {
                            System.out.print(String.format("%-24s", ""));
                        }

                        if (i == 4) System.out.println();
                    }
                    System.out.println();

                    String command = cliController.getScanner().nextLine();
                    while (!command.equals("use") && !command.equals("buy"))
                    {
                        command = cliController.getScanner().nextLine();
                    }

                    while (!command.equals("buy"))
                    {
                        System.out.println("Your coins:" + currentPlayer.getCoins());
                        System.out.println("Your treasures:");
                        for (Card c : currentPlayer.getHand().getCards())
                        {
                            if (c.getType() == 1)
                                System.out.println(c.getName());
                        }

                        command = cliController.getScanner().nextLine();

                        if (!command.equals("buy"))
                        {
                            try
                            {
                                currentPlayer.playCard(command);
                            }
                            catch (Exception e)
                            {
                                System.out.println("You don't have " + command + " or no such card exists");
                            }
                        }
                    }

                    while (!command.equals("stop") && currentPlayer.getBuys() > 0)
                    {
                        System.out.println("Your buys:" + currentPlayer.getBuys());
                        command = cliController.getScanner().nextLine();

                        try
                        {
                            currentPlayer.buyCard(command);
                        }
                        catch (Exception e)
                        {
                            System.out.println("You don't have " + command + " or no such card exists");
                        }
                    }
                    break;
                case 2:
                    System.out.println("CLEANUP phase");
                    break;
            }

            currentGame.advancePhase();
        }
    }
}
