package dominion.controllers;

import dominion.*;
import dominion.exceptions.LobbyNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;
/**
 * Created by Sam on 28/04/2016.
 */
public class CliController
{
    private GameEngine gameEngine;
    private Scanner scanner;
    private Lobby lobby;
    private Game game;

    public CliController()
    {
        try
        {
            gameEngine = new GameEngine();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return;
        }
        scanner = new Scanner(System.in);
    }

    public void printTitle()
    {
       System.out.println("\\~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~/");
        System.out.println("|                                                     |");
        System.out.println("|  DDD    OOO   M   M  III  N   N  III   OOO   N   N  |");
        System.out.println("|  D  D  O   O  MM MM   I   NN  N   I   O   O  NN  N  |");
        System.out.println("|  D  D  O   O  M M M   I   N N N   I   O   O  N N N  |");
        System.out.println("|  D  D  O   O  M   M   I   N  NN   I   O   O  N  NN  |");
        System.out.println("|  DDD    OOO   M   M  III  N   N  III   OOO   N   N  |");
        System.out.println("|                                                     |");
        System.out.println("/~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~\\");
    }

    public void setUpLobby()
    {
        lobby = null;

        System.out.println("Player 1, what is your name?");
        String name = scanner.nextLine();
        Account player1 = new Account(name, 0);

        System.out.println("Player 2, what is your name?");
        name = scanner.nextLine();
        Account player2 = new Account(name, 0);

        gameEngine.createLobby(player1, "mygame", "mypassword");

        try
        {
            lobby = gameEngine.findLobby("mygame");
        }
        catch (LobbyNotFoundException ex)
        {
            //never happens in the CLI
        }

        lobby.addPlayer(player2);

    }

    public void startGame()
    {
        lobby.startGame();
        game = lobby.getGame();
    }

    public void gamePlay()
    {
        while (!game.getIsOver())
        {
            Player currentPlayer = game.findCurrentPlayer();
            System.out.println(currentPlayer.getAccount().getName() + "'s turn starts.");
            switch (game.getPhase())
            {
                case 0:
                    actionPhase();
                    break;
                case 1:
                    buyPhase(currentPlayer);
                    break;
            }
            game.advancePhase();
        }
    }

    private void actionPhase()
    {
        System.out.println("ACTION phase");
    }

    private void buyPhase(Player currentPlayer)
    {
        System.out.println("BUY phase");

        printHand(currentPlayer);

        printKingdomCards();

        printVicTreasCards();

        useTreasureCards(currentPlayer);

        buyCards(currentPlayer);
    }

    private void printHand(Player currentPlayer)
    {
        System.out.println();
        System.out.println("Your hand:");
        System.out.println();

        for (Card c : currentPlayer.getHand().getCards())
        {
            System.out.println(c.getName());
        }
    }

    private void printKingdomCards()
    {
        System.out.println();
        System.out.println("Available kingdom cards:");

        for (int i = 0; i < game.getKingdomCards().length; i++)
        {
            Card currentCard = game.getKingdomCards()[i];

            if (currentCard.getAmount() > 0)
            {
                System.out.print(String.format("%-24s",
                        currentCard.getName() + "(" + currentCard.getAmount() + "x/" + currentCard.getCost() + " c)"));
            } else
            {
                System.out.print(String.format("%-24s", ""));
            }

            if (i == 4) System.out.println();
        }
    }

    private void printVicTreasCards()
    {
        System.out.println();
        System.out.println();
        System.out.println("Available victory/treasure cards:");

        for (int i = game.getFixedCards().length - 1; i >= 0; i--)
        {
            Card currentCard = game.getFixedCards()[i];
            Card prevCard;
            try
            {
                prevCard = game.getFixedCards()[i-1];
            }
            catch (Exception e)
            {
                prevCard = null;
            }

            if (currentCard.getAmount() > 0)
            {
                System.out.print(String.format("%-24s",
                        currentCard.getName() + "(" + currentCard.getAmount() + "x/" + currentCard.getCost() + " c)"));
            } else
            {
                System.out.print(String.format("%-24s", ""));
            }

            if (prevCard != null && prevCard.getType() != currentCard.getType()) System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    private void useTreasureCards(Player currentPlayer)
    {

        String command = "";
        Hand currentPlayerHand = currentPlayer.getHand();
        Boolean hasTreasureCards = currentPlayerHand.checkHandForType(1);
        printInColor(31, "You can now use your treasure cards");
        System.out.println("Enter \"stop\" at any given time if you would like to stop using cards.");
        System.out.println();

        while (!command.equals("stop") && hasTreasureCards)
        {
            System.out.println("Your coins:" + currentPlayer.getCoins());
            System.out.println("Your treasures:");
            currentPlayerHand = currentPlayer.getHand();

            printCardsOfType(currentPlayerHand.getCards(), 1);

            System.out.println("Which treasure card would you like to use?");

            command = scanner.nextLine().toLowerCase();

            if (!command.equals("stop"))
            {
                try
                {
                    currentPlayer.playCard(command);
                }
                catch (Exception e)
                {
                    System.out.println("You don't have a " + command + " card or no such card exists");
                }
            }
            hasTreasureCards = currentPlayerHand.checkHandForType(1);
        }

    }

    private void buyCards(Player currentPlayer)
    {
        String command = "";
        System.out.println("You can now buy cards");
        System.out.println("Enter \"stop\" at any given time if you would like to stop buying cards.");
        while (!command.equals("stop") && currentPlayer.getBuys() > 0)
        {
            printKingdomCards();
            printVicTreasCards();
            System.out.println("Your buys:" + currentPlayer.getBuys());
            command = scanner.nextLine().toLowerCase();

            try
            {
                currentPlayer.buyCard(command);
            }
            catch (Exception e)
            {
                System.out.println("You don't have " + command + " or no such card exists");
            }
        }
    }

    private void printCardsOfType(ArrayList<Card> currentCards, int type)
    {
        for (Card c : currentCards)
        {
            if (c.getType() == type)
            {
                System.out.println(c.getName());
            }
        }
        System.out.println();
    }

    private void printInColor(int color, String string)
    {
        String printColor = (char)27 + "[" + color + "m";
        String resetColor = (char)27 + "[0m";
        System.out.println(printColor + string + resetColor);
    }
}
