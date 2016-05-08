package dominion.server;

import com.google.gson.Gson;
import dominion.*;
import dominion.exceptions.LobbyNotFoundException;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tom Dobbelaere on 7/05/2016.
 */

public class GameManager extends javax.servlet.http.HttpServlet
{
    @Override
    public void init()
    {
        ServletContext servletContext = getServletContext();
        HashMap<String, ArrayList<Card>> cardsOnTable = new HashMap<>();

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
        HashMap<String, ArrayList<Card>> cardsOnTable = (HashMap<String, ArrayList<Card>>) servletContext.getAttribute("cardsOnTable");

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

                    cardsOnTable.put(lobbyName, new ArrayList<Card>());
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
                case "fetchlobbystatus":
                {
                    String nickname = request.getParameter("nickname");
                    String lobbyName = request.getParameter("lobbyname");
                    Lobby lobby;

                    try
                    {
                        lobby = gameEngine.findLobby(lobbyName);
                        Game game = lobby.getGame();

                        HashMap<String, Object> gameStatus = new HashMap<>();
                        gameStatus.put("isMyTurn", game.findCurrentPlayer().getAccount().getName().equals(nickname));
                        gameStatus.put("cardsOnTable", cardsOnTable.get(lobbyName));

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
                    String cardName = request.getParameter("cardname");
                    String lobbyName = request.getParameter("lobbyname");

                    ArrayList<Card> cards = cardsOnTable.get(lobbyName);

                    cards.add(gameEngine.findCard(cardName));
                }
                break;
                case "getcardsontable":
                {
                    String lobbyName = request.getParameter("lobbyname");

                    ArrayList<Card> cards = cardsOnTable.get(lobbyName);

                    writer.print(gson.toJson(cards));
                }
                break;
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
