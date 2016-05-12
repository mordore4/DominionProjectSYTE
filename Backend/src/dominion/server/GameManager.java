package dominion.server;

import com.google.gson.Gson;
import dominion.*;
import dominion.exceptions.CardNotAvailableException;
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
        HashMap<String, Boolean> enableBuying = (HashMap<String, Boolean>) servletContext.getAttribute("enableBuying");

        PrintWriter writer = response.getWriter();
        Gson gson = new Gson();

        String command = request.getParameter("command");

        if (command != null)
        {
            String nickname = request.getParameter("nickname");
            String lobbyName = request.getParameter("lobbyname");
            String cardName = request.getParameter("cardname");
            Lobby lobby = null;
            Game game = null;
            Player thisPlayer = null;
            Player currentPlayer = null;
            boolean isMyTurn = false;
            try
            {
                lobby = gameEngine.findLobby(lobbyName);
                if (lobby.isStarted())
                {
                    game = lobby.getGame();
                    thisPlayer = game.getPlayer(nickname);
                    currentPlayer = game.findCurrentPlayer();
                    //isMyTurn = currentPlayer.isMyTurn(nickname);
                    if (thisPlayer != null)
                    {
                        isMyTurn = thisPlayer.equals(currentPlayer);
                    }
                }
            }
            catch (LobbyNotFoundException e)
            {
                e.printStackTrace();
            }

            switch (command)
            {
                case "createlobby":
                {
                    Account newAccount = new Account(nickname, 0);

                    gameEngine.createLobby(newAccount, lobbyName, "");

                    enableBuying.put(lobbyName, false);
                }
                break;
                case "joinlobby":
                {
                    Account newAccount = new Account(nickname, 0);

                    lobby.addPlayer(newAccount);
                    lobby.startGame();
                }
                break;
                case "haslobbystarted":
                {
                    writer.print(lobby.isStarted());
                }
                break;
                case "retrievehand":
                {
                    writer.print(gson.toJson(thisPlayer.getHand().getCards()));
                }
                break;
                case "fetchgamestatus":
                {
                    HashMap<String, Object> gameStatus = new HashMap<>();
                    gameStatus.put("isMyTurn", isMyTurn);
                    gameStatus.put("cardsOnTable", game.getCardsOnTable());

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
                break;
                case "putcardontable":
                {

                    if (isMyTurn)
                    {
                        game.playCard(cardName);
                        //currentPlayer.playCard(cardName);

                        if (!currentPlayer.getHand().checkHandForType(1))
                        {
                            enableBuying.put(lobbyName, true);
                        }
                    }
                }
                break;
                case "endturn":
                {
                    if (isMyTurn)
                    {
                        enableBuying.put(lobbyName, false);
                        game.advancePlayer();
                    }
                }
                break;
                case "retrievekingdomcards":
                {
                    HashMap<String, Card[]> cardsArray = new HashMap<>();

                    cardsArray.put("kingdomCards", game.getKingdomCards());

                    cardsArray.put("fixedCards", game.getFixedCards());

                    writer.print(gson.toJson(cardsArray));
                }
                break;
                case "retrievebuyablecards":
                {
                    ArrayList<Object[]> cards = new ArrayList<>();

                    for (Card card : game.getKingdomCards())
                    {
                        Object[] cardInfo = {card.getName(), card.getAmount(), game.isBuyable(card)};
                        cards.add(cardInfo);
                    }

                    for (Card card : game.getFixedCards())
                    {
                        Object[] cardInfo = {card.getName(), card.getAmount(), game.isBuyable(card)};
                        cards.add(cardInfo);
                    }

                    writer.print(gson.toJson(cards));

                }
                case "buycard":
                {
                    if (isMyTurn)
                    {
                        try
                        {
                            currentPlayer.buyCard(cardName);
                        }
                        catch (CardNotAvailableException ex)
                        {
                            ex.printStackTrace();
                            //Do nothing
                        }
                    }
                }
                break;
                /*case "retrievebuyablecards":
                {
                    ArrayList<String> buyableCards = game.findBuyableCards();

                    writer.print(gson.toJson(buyableCards));
                }*/
            }
        }
    }
}
