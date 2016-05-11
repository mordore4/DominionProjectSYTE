package dominion.server;

import com.google.gson.Gson;
import dominion.*;
import dominion.exceptions.LobbyNotFoundException;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Tom Dobbelaere on 7/05/2016.
 */

public class GameManager extends javax.servlet.http.HttpServlet
{
    @Override
    public void init()
    {
        ServletContext servletContext = getServletContext();
        HashMap<String, CopyOnWriteArrayList<Card>> cardsOnTable = new HashMap<>();
        HashMap<String, Boolean> enableBuying = new HashMap<>();

        GameEngine gameEngine = null;

        try
        {
            gameEngine = new GameEngine();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        servletContext.setAttribute("gameEngine", gameEngine);
        servletContext.setAttribute("cardsOnTable", cardsOnTable);
        servletContext.setAttribute("enableBuying", enableBuying);
    }

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException
    {
        doGet(request, response);
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException
    {
        ServletContext servletContext = getServletContext();
        GameEngine gameEngine = (GameEngine) servletContext.getAttribute("gameEngine");
        HashMap<String, CopyOnWriteArrayList<Card>> cardsOnTable = (HashMap<String, CopyOnWriteArrayList<Card>>) servletContext.getAttribute("cardsOnTable");
        HashMap<String, Boolean> enableBuying = (HashMap<String, Boolean>) servletContext.getAttribute("enableBuying");

        PrintWriter writer = response.getWriter();
        Gson gson = new Gson();

        String command = request.getParameter("command");


        if (command != null)
        {
            switch (command)
            {
                case "createlobby":
                {
                    String nickname = request.getParameter("nickname");
                    String lobbyName = request.getParameter("lobbyname");
                    Account newAccount = new Account(nickname, 0);

                    gameEngine.createLobby(newAccount, lobbyName, "");

                    cardsOnTable.put(lobbyName, new CopyOnWriteArrayList<Card>());
                    enableBuying.put(lobbyName, false);
                }
                break;
                case "joinlobby":
                {
                    String nickname = request.getParameter("nickname");
                    String lobbyName = request.getParameter("lobbyname");
                    Account newAccount = new Account(nickname, 0);
                    Lobby lobby;

                    try
                    {
                        lobby = gameEngine.findLobby(lobbyName);
                        lobby.addPlayer(newAccount);

                        lobby.startGame();
                    }
                    catch (LobbyNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
                case "haslobbystarted":
                {
                    String lobbyName = request.getParameter("lobbyname");
                    Lobby lobby;

                    try
                    {
                        lobby = gameEngine.findLobby(lobbyName);

                        writer.print(lobby.isStarted());
                    }
                    catch (LobbyNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
                case "retrievehand":
                {
                    String nickname = request.getParameter("nickname");
                    String lobbyName = request.getParameter("lobbyname");
                    Lobby lobby;
                    Player thisPlayer;

                    try
                    {
                        lobby = gameEngine.findLobby(lobbyName);
                        thisPlayer = lobby.getGame().getPlayer(nickname);

                        writer.print(gson.toJson(thisPlayer.getHand().getCards()));
                    }
                    catch (LobbyNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
                case "fetchgamestatus":
                {
                    String nickname = request.getParameter("nickname");
                    String lobbyName = request.getParameter("lobbyname");
                    Lobby lobby;

                    try
                    {
                        lobby = gameEngine.findLobby(lobbyName);
                        Game game = lobby.getGame();
                        Player thisPlayer = game.getPlayer(nickname);

                        HashMap<String, Object> gameStatus = new HashMap<>();
                        gameStatus.put("isMyTurn", game.findCurrentPlayer().getAccount().getName().equals(nickname));
                        gameStatus.put("cardsOnTable", cardsOnTable.get(lobbyName));

                        if (!enableBuying.get(lobbyName))
                        {
                            gameStatus.put("phase", game.getPhase());
                        }
                        else
                        {
                            gameStatus.put("phase", 3);
                        }

                        gameStatus.put("actions", thisPlayer.getActions());
                        gameStatus.put("buys", thisPlayer.getBuys());
                        gameStatus.put("coins", thisPlayer.getCoins());

                        writer.print(gson.toJson(gameStatus));
                    }
                    catch (LobbyNotFoundException e)
                    {
                        e.printStackTrace();
                    }

                }
                break;
                case "putcardontable":
                {
                    String nickname = request.getParameter("nickname");
                    String cardName = request.getParameter("cardname");
                    String lobbyName = request.getParameter("lobbyname");

                    CopyOnWriteArrayList<Card> cards = cardsOnTable.get(lobbyName);

                    cards.add(gameEngine.findCard(cardName));

                    Lobby lobby;

                    try
                    {
                        lobby = gameEngine.findLobby(lobbyName);
                        Game game = lobby.getGame();
                        Player currentPlayer = game.findCurrentPlayer();

                        if (currentPlayer.getAccount().getName().equals(nickname))
                        {
                            game.playCard(cardName);
                            //currentPlayer.playCard(cardName);

                            if (!currentPlayer.getHand().checkHandForType(1))
                            {
                                enableBuying.put(lobbyName, true);
                            }
                        }
                    }
                    catch (LobbyNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
                case "getcardsontable":
                {
                    String lobbyName = request.getParameter("lobbyname");

                    CopyOnWriteArrayList<Card> cards = cardsOnTable.get(lobbyName);

                    writer.print(gson.toJson(cards));
                }
                break;
                case "endturn":
                {
                    String nickname = request.getParameter("nickname");
                    String lobbyName = request.getParameter("lobbyname");

                    Lobby lobby;

                    try
                    {
                        lobby = gameEngine.findLobby(lobbyName);
                        Game game = lobby.getGame();
                        Player currentPlayer = game.findCurrentPlayer();

                        if (currentPlayer.getAccount().getName().equals(nickname))
                        {
                            enableBuying.put(lobbyName, false);
                            cardsOnTable.put(lobbyName, new CopyOnWriteArrayList<Card>());
                            game.advancePhase();
                        }
                    }
                    catch (LobbyNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
                case "retrievekingdomcards":
                {
                    String lobbyName = request.getParameter("lobbyname");

                    Lobby lobby;

                    try
                    {
                        lobby = gameEngine.findLobby(lobbyName);
                        Game game = lobby.getGame();

                        HashMap<String, ArrayList<Card>> cardsArray = new HashMap<String, ArrayList<Card>>();

                        ArrayList<Card> kingdomCards = new ArrayList<>();

                        for (Card card : game.getKingdomCards())
                        {
                            kingdomCards.add(card);
                        }

                        cardsArray.put("kingdomCards", kingdomCards);

                        ArrayList<Card> fixedCards = new ArrayList<>();

                        for (Card card : game.getFixedCards())
                        {
                            fixedCards.add(card);
                        }

                        cardsArray.put("fixedCards", fixedCards);

                        writer.print(gson.toJson(cardsArray));
                    }
                    catch (LobbyNotFoundException ex)
                    {
                        //Do nothing
                    }
                }
                break;
                case "retrievebuyablecards":
                {
                    String lobbyName = request.getParameter("lobbyname");

                    Lobby lobby;

                    try
                    {
                        lobby = gameEngine.findLobby(lobbyName);
                        Game game = lobby.getGame();
                        Player currentPlayer = game.findCurrentPlayer();
                        int money = currentPlayer.getCoins();

                        ArrayList<String> buyableCards = new ArrayList<>();

                        Card[] kingdomCards = game.getKingdomCards();
                        Card[] fixedCards = game.getFixedCards();

                        for (Card kingdomCard : kingdomCards)
                        {
                            if (money >= kingdomCard.getCost())
                            {
                                buyableCards.add(kingdomCard.getName());
                            }
                        }

                        for (Card fixedCard : fixedCards)
                        {
                            if (money >= fixedCard.getCost())
                            {
                                buyableCards.add(fixedCard.getName());
                            }
                        }

                        writer.print(gson.toJson(buyableCards));
                    }
                    catch (LobbyNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }


        //TEMPORARY REMOVE LATER
        /*Account testA = new Account("testA", 0);
        Account testB = new Account("testB", 0);

        gameEngine.createLobby(testA, "test", "");
        Lobby lobby = null;
        try
        {
            lobby = gameEngine.findLobby("test");
            lobby.addPlayer(testB);
            lobby.startGame();


            response.getWriter().print(gson.toJson(lobby.getGame().getPlayer("testA").getHand().getCards()));
            //gameEngine.
        }
        catch (LobbyNotFoundException le)
        {
            //do nothing
        }*/
    }
}
