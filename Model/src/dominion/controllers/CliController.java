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
        int amountOfPlayers = 2;
        Account[] accounts = new Account[amountOfPlayers];

        for (int playerNumber = 0; playerNumber < amountOfPlayers; playerNumber++)
        {
            System.out.print("Player "+playerNumber+", what is your name?\t");
            String name = scanner.nextLine();
            accounts[playerNumber] = new Account(name, 0);
        }

        /*System.out.print("Player 1, what is your name?\t");
        String name = scanner.nextLine();
        Account account1 = new Account(name, 0);

        System.out.print("Player 2, what is your name?\t");
        name = scanner.nextLine();
        Account account2 = new Account(name, 0);*/

        gameEngine.createLobby(accounts[0], "mygame", "mypassword");

        try
        {
            lobby = gameEngine.findLobby("mygame");
        }
        catch (LobbyNotFoundException ex)
        {
            //never happens in the CLI
        }

        //lobby.addPlayer(account2);

        for (int playerNumber = 1; playerNumber < amountOfPlayers; playerNumber++)
        {
            lobby.addPlayer(accounts[playerNumber]);
        }

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
                    actionPhase(currentPlayer);
                    break;
                case 1:
                    buyPhase(currentPlayer);
                    break;
            }
            game.advancePhase();
        }
    }

    private void actionPhase(Player currentPlayer)
    {
        printInColor(31, "ACTION phase");
        printTable(currentPlayer);
        String command = "";
        Hand currentPlayerHand = currentPlayer.getHand();

        Boolean hasActionCards = currentPlayerHand.containsActionCards();

        printInColor(34, "You can now use your action cards");
        System.out.println("Enter \"stop\" at any given time if you would like to stop using cards.");
        System.out.println();

        while (hasActionCards && currentPlayer.getBuys() != 0 && !command.equals("stop"))
        {
            printActionsBuysCoins(currentPlayer);

            System.out.println("Your action cards:");
            printCardsOfType(currentPlayerHand.getCards(), 3);
            printCardsOfType(currentPlayerHand.getCards(), 4);
            printCardsOfType(currentPlayerHand.getCards(), 5);

            System.out.println("Which action card would you like to use?");

            command = scanner.nextLine().toLowerCase();
            hasActionCards = currentPlayerHand.containsActionCards();
        }

    }

    private void buyPhase(Player currentPlayer)
    {
        printInColor(31, "BUY phase");

        printTable(currentPlayer);

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
        printInColor(34, "You can now use your treasure cards");
        System.out.println("Enter \"stop\" at any given time if you would like to stop using cards.");
        System.out.println();

        while (!command.equals("stop") && hasTreasureCards)
        {
            printInColor(32, "Your coins:" + currentPlayer.getCoins());
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
        printInColor(34, "You can now buy cards");
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
                printInColor(33, c.getName());
            }
        }
        System.out.println();
    }

    private void printTable(Player currentPlayer)
    {
        printHand(currentPlayer);

        printKingdomCards();

        printVicTreasCards();
    }

    private void printActionsBuysCoins(Player currentPlayer)
    {
        printInColor(32, "Your actions:" + currentPlayer.getActions());
        printInColor(32, "Your buys:" + currentPlayer.getBuys());
        printInColor(32, "Your coins:" + currentPlayer.getCoins());
    }

    private void printInColor(int color, String string)
    {
        /*
        30 black
        31 red
        32 green
        33 yellow
        34 blue
        35 magenta
        36 cyan
        37 white
         */
        String printColor = (char)27 + "[" + color + "m";
        String resetColor = (char)27 + "[0m";
        System.out.println(printColor + string + resetColor);
    }
}
