package dominion.controllers;

import dominion.*;
import dominion.exceptions.LobbyNotFoundException;

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

    public CliController(GameEngine gameEngine)
    {
        this.gameEngine = gameEngine;
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

        System.out.println("Player 1, what is your name?");
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
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
            game.advancePhase();
        }
    }

}
