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

        while (!currentGame.getIsOver())
        {
            Player currentPlayer = currentGame.findCurrentPlayer();

            System.out.println(currentPlayer.getAccount().getName() + "'s turn starts.");
            //System.out.println("Your hand: ");
            //System.out.println(currentPlayer.getHand().toString());

            //System.out.println("Your discard pile: ");
            //System.out.println(currentPlayer.getDiscardPile().toString());


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
                            //System.out.print(currentCard.getName() +
                            //        "(" + currentCard.getAmount() + ")" + "\t\t");
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
                            //System.out.print(currentCard.getName() +
                            //        "(" + currentCard.getAmount() + ")" + "\t\t");
                            System.out.print(String.format("%-24s",
                                    currentCard.getName() + "(" + currentCard.getAmount() + "x/" + currentCard.getCost() + "c)"));
                        } else
                        {
                            System.out.print(String.format("%-24s", ""));
                        }

                        if (i == 4) System.out.println();
                    }
                    System.out.println();

                    String command = scanner.nextLine();
                    while (!command.equals("use") && !command.equals("buy"))
                    {
                        command = scanner.nextLine();
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

                        command = scanner.nextLine();

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
                        command = scanner.nextLine();

                        try
                        {
                            currentPlayer.buyCard(command);
                        }
                        catch (Exception e)
                        {
                            System.out.println("You don't have " + command + " or no such card exists");
                        }
                    }

                    /*try
                    {
                        currentPlayer.buyCard(command);
                    }
                    catch (CardNotAvailableException e)
                    {
                        e.printStackTrace();
                    }*/

                    break;
                case 2:
                    System.out.println("CLEANUP phase");
                    break;
            }

            currentGame.advancePhase();
        }

        System.out.println(currentGame.getPlayer(player1.getName()).getDeck().toString());
        System.out.println(currentGame.getPlayer(player2.getName()).getDeck().toString());

        System.out.println(currentGame.allCardsToString());


    }
}
