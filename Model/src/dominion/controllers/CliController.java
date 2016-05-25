package dominion.controllers;

import dominion.*;
import dominion.exceptions.CardNotAvailableException;
import dominion.exceptions.LobbyNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
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
    private boolean buying = false;

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
        String[] accounts = new String[amountOfPlayers];

        for (int playerNumber = 0; playerNumber < amountOfPlayers; playerNumber++)
        {
            System.out.print("Player "+(playerNumber+1)+", what is your name?\t");
            String name = scanner.nextLine();
            accounts[playerNumber] = name;
        }

        gameEngine.createLobby(accounts[0], "mygame", "big money");

        try
        {
            lobby = gameEngine.findLobby("mygame");
        }
        catch (LobbyNotFoundException ex)
        {
            //never happens in the CLI
        }

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
            System.out.println(currentPlayer.getName() + "'s turn starts.");
            switch (game.getPhase())
            {
                case 0:
                    actionPhase(currentPlayer);
                    break;
                case 1:
                    buyPhase(currentPlayer);
                    game.advancePhase();
                    break;
                case 2:
                    game.advancePlayer();
            }
        }
    }

    private void actionPhase(Player currentPlayer)
    {
        printlnincolor(31, "ACTION phase");
        printTable(currentPlayer);
        String command = "";
        Deck currentPlayerHand = currentPlayer.getHand();
        currentPlayer.addActions(10);

        Boolean hasActionCards = currentPlayerHand.containsActionCards();

        printlnincolor(34, "You can now use your action cards");
        System.out.println("Enter \"stop\" at any given time if you would like to stop using cards.");
        System.out.println();

        while (hasActionCards && currentPlayer.getActions() > 0 && !command.equals("stop"))
        {
            printActionsBuysCoins(currentPlayer);

            System.out.println("Your action cards:");
            printCardsOfType(currentPlayerHand.getCards(), 3);
            printCardsOfType(currentPlayerHand.getCards(), 4);
            printCardsOfType(currentPlayerHand.getCards(), 5);

            System.out.println("Which action card would you like to use?");

            command = scanner.nextLine().toLowerCase();
            if (!command.equals("stop"))
            {
                try
                {
                    Card card = game.findCard(command);
                    if (card.isActionCard())
                    {
                        try
                        {
                            game.playCard(command);
                        }
                        catch (CardNotAvailableException e)
                        {
                            System.out.println("you don't have that card");
                        }
                    }
                    else
                    {
                        System.out.println(command + " is not an action card.");
                    }
                }
                catch (Exception e)
                {
                    System.out.println(command + " is not a card.");
                }

            }

            hasActionCards = currentPlayerHand.containsActionCards();
        }

    }

    private void buyPhase(Player currentPlayer)
    {
        printlnincolor(31, "BUY phase");

        printTable(currentPlayer);

        useTreasureCards(currentPlayer);

        printlnincolor(31, "test6 " + game.getPhase());

        buyCards(currentPlayer);

        printlnincolor(31, "test7 " + game.getPhase());
    }

    private void printHand(Player currentPlayer)
    {
        System.out.println("Your hand:");
        System.out.println();

        for (Card c : currentPlayer.getHand().getCards())
        {
            System.out.println(c.getName());
        }
        System.out.println();
    }

    private void printCards(Player currentPlayer, String type)
    {
        Card[] cards = new Card[0]; // won't cause trouble because will never happen.
        if (type.equals("kingdom"))
        {
            System.out.println("Available kingdom cards:");
            cards = game.getKingdomCards();
        }
        if (type.equals("fixed"))
        {
            System.out.println("Available coin cards:");
            cards = game.getFixedCards();
        }


        for (int i = cards.length - 1; i >= 0; i--)
        {
            Card currentCard = cards[i];

            if (currentCard.getAmount() > 0)
            {
                if ((currentCard.getCost() <= currentPlayer.getCoins()) && buying)
                {
                    printInColor(32, String.format("%-24s",
                            currentCard.getName() + "(" + currentCard.getAmount() + "x/" + currentCard.getCost() + " c)"));
                }
                else
                {
                    System.out.print(String.format("%-24s",
                            currentCard.getName() + "(" + currentCard.getAmount() + "x/" + currentCard.getCost() + " c)"));
                }
            } else
            {
                System.out.print(String.format("%-24s", ""));
            }

            if (type.equals("fixed"))
            {
                if (i == 4)
                {
                    System.out.println("");
                    System.out.println("Available victory cards");
                }
            }
            else
            {
                if (i == 5) System.out.println();
            }
        }
        System.out.println();
        System.out.println();
    }

    private void useTreasureCards(Player currentPlayer)
    {

        String command = "";
        Deck currentPlayerHand = currentPlayer.getHand();
        Boolean hasTreasureCards = currentPlayerHand.checkHandForType(1);
        printlnincolor(34, "You can now use your treasure cards");
        System.out.println("Enter \"stop\" at any given time if you would like to stop using cards.");
        System.out.println();
        currentPlayer.setCoins(10000);
        currentPlayer.setBuys(5);
        while (!command.equals("stop") && hasTreasureCards)
        {
            printlnincolor(32, "Your coins:" + currentPlayer.getCoins());
            System.out.println("Your treasures:");
            currentPlayerHand = currentPlayer.getHand();

            printCardsOfType(currentPlayerHand.getCards(), 1);

            System.out.println("Which treasure card would you like to use?");

            command = scanner.nextLine().toLowerCase();

            if (!command.equals("stop"))
            {
                try
                {
                    game.playCard(command);
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
        buying = true;
        String command = "";
        printlnincolor(34, "You can now buy cards");
        System.out.println("Enter \"stop\" at any given time if you would like to stop buying cards.");

        while (!command.equals("stop") && currentPlayer.getBuys() > 0)
        {
            printCards(currentPlayer, "kingdom");
            printCards(currentPlayer, "fixed");
            System.out.println("Your buys:" + currentPlayer.getBuys());
            command = scanner.nextLine().toLowerCase();
            if (!command.equals("stop"))
            {
                try
                {
                    game.buyCard(command);
                }
                catch (Exception e)
                {
                    System.out.println("You can't buy a " + command + " card or no such card exists");
                }
            }
        }
        System.out.println();
        buying = false;
    }

    private void printCardsOfType(ArrayList<Card> currentCards, int type)
    {
        if (game.findCurrentPlayer().getHand().checkHandForType(type))
        {
            for (Card c : currentCards)
            {
                if (c.getType() == type)
                {
                    printlnincolor(33, c.getName());
                }
            }
            System.out.println();
        }
    }

    private void printTable(Player currentPlayer)
    {
        printHand(currentPlayer);

        printCards(currentPlayer, "kingdom");
        printCards(currentPlayer, "fixed");
    }

    private void printActionsBuysCoins(Player currentPlayer)
    {
        printlnincolor(32, "Your actions:" + currentPlayer.getActions());
        printlnincolor(32, "Your buys:" + currentPlayer.getBuys());
        printlnincolor(32, "Your coins:" + currentPlayer.getCoins());
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
        System.out.print(printColor + string + resetColor);
    }

    private void printlnincolor(int color, String string)
    {
        printInColor(color, string);
        System.out.println();
    }
}
