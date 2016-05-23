package dominion.server;

import dominion.*;
import dominion.exceptions.CardNotAvailableException;
import dominion.exceptions.LobbyNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;
import dominion.util.Condition;
import dominion.util.RemoveCardsCondition;
import dominion.util.RemoveCardsThenAddCondition;

/**
 * Created by Digaly on 19/05/2016.
 */
public class HTMLController
{
    GameEngine gameEngine;
    Map<String, Command> methodMap;
    private Gson gson;


    public HTMLController()
    {
        try
        {
            gameEngine = new GameEngine();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        gson = new Gson();
        methodMap = new HashMap<>();

        fillMethodMap();
    }

    public void fillMethodMap()
    {
        methodMap.put("createlobby", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                createLobby(request.getParameter("nickname"), request.getParameter("lobbyname"),
                        request.getParameter("cardset"));
            }
        });

        methodMap.put("joinlobby", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                joinLobby(request.getParameter("nickname"), request.getParameter("lobbyname"));
            }
        });

        methodMap.put("startgame", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                startGame(request.getParameter("lobbyname"));
            }
        });

        methodMap.put("haslobbystarted", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                hasLobbyStarted(request.getParameter("lobbyname"), writer);
            }
        });

        methodMap.put("getcardsets", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                getCardSets(writer);
            }
        });

        methodMap.put("retrievehand", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                retrieveHand(request.getParameter("nickname"), request.getParameter("lobbyname"), writer);
            }
        });

        methodMap.put("retrievekingdomcards", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                retrieveKingdomCards(request.getParameter("lobbyname"), writer);
            }
        });

        methodMap.put("fetchgamestatus", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                fetchGameStatus(request.getParameter("nickname"), request.getParameter("lobbyname"), writer);
            }
        });

        methodMap.put("retrievebuyablecards", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                retrieveBuyableCards(request.getParameter("lobbyname"), writer);
            }
        });

        methodMap.put("buycard", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                buyCard(request.getParameter("nickname"), request.getParameter("lobbyname"),
                        request.getParameter("cardname"));
            }
        });

        methodMap.put("discardcard", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                discardCard(request.getParameter("nickname"), request.getParameter("lobbyname"),
                        request.getParameter("cardname"));
            }
        });

        methodMap.put("discardmultiple", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                discardMultiple(request.getParameter("nickname"), request.getParameter("lobbyname"),
                        request.getParameter("cardlist"));
            }
        });

        methodMap.put("endactions", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                endActions(request.getParameter("lobbyname"));
            }
        });

        methodMap.put("endturn", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                endTurn(request.getParameter("lobbyname"));
            }
        });

        methodMap.put("playtreasures", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                playTreasures(request.getParameter("lobbyname"));
            }
        });

        methodMap.put("putcardontable", new Command()
        {
            @Override
            public void runCommand(HttpServletRequest request, PrintWriter writer)
            {
                putCardOnTable(request.getParameter("lobbyname"), request.getParameter("cardname"));
            }
        });
    }

    public void executeCommand(HttpServletRequest request, PrintWriter writer)
    {
        String command = request.getParameter("command");

        methodMap.get(command).runCommand(request, writer);
    }

    public void createLobby(String nickname, String lobbyName, String cardSet)
    {
        gameEngine.createLobby(nickname, lobbyName, cardSet);
    }

    public void joinLobby(String nickname, String lobbyName)
    {
        try
        {
            gameEngine.findLobby(lobbyName).addPlayer(nickname);
        }
        catch (LobbyNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void startGame(String lobbyName)
    {
        try
        {
            gameEngine.findLobby(lobbyName).startGame();
        }
        catch (LobbyNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void hasLobbyStarted(String lobbyName, PrintWriter writer)
    {
        Lobby lobby = null;

        try
        {
            lobby = gameEngine.findLobby(lobbyName);
        }
        catch (LobbyNotFoundException e)
        {
            e.printStackTrace();
        }


        HashMap<String, Object> lobbyStatus = new HashMap<>();
        lobbyStatus.put("started", lobby.isStarted());
        lobbyStatus.put("players", lobby.getPlayersInLobby());

        writer.print(gson.toJson(lobbyStatus));
    }

    public void getCardSets(PrintWriter writer)
    {
        String[] cardSets = gameEngine.retrieveCardSets();

        writer.print(gson.toJson(cardSets));
    }

    public void retrieveHand(String nickname, String lobbyName, PrintWriter writer)
    {
        Player thisPlayer = null;

        try
        {
            thisPlayer = gameEngine.findLobby(lobbyName).getGame().getPlayer(nickname);
        }
        catch (LobbyNotFoundException e)
        {
            e.printStackTrace();
        }

        writer.print(gson.toJson(thisPlayer.getHand().getCards()));
    }

    public void retrieveKingdomCards(String lobbyName, PrintWriter writer)
    {
        Game game = retrieveGameOfLobby(lobbyName);

        HashMap<String, Card[]> cardsArray = new HashMap<>();

        cardsArray.put("kingdomCards", game.getKingdomCards());

        cardsArray.put("fixedCards", game.getFixedCards());

        writer.print(gson.toJson(cardsArray));
    }

    public void fetchGameStatus(String nickname, String lobbyName, PrintWriter writer)
    {
        Game game = retrieveGameOfLobby(lobbyName);
        Player thisPlayer = game.getPlayer(nickname);

        boolean isMyTurn = game.findCurrentPlayer().getName().equals(nickname);

        HashMap<String, Object> gameStatus = new HashMap<>();
        gameStatus.put("isMyTurn", isMyTurn);

        ArrayList<String> cardsOnTable = new ArrayList<>();

        for (Card card : game.getCardsOnTable())
        {
            cardsOnTable.add(card.getName());
        }


        Condition myCondition = game.getConditionsList().get(thisPlayer);
        ConditionWrapper myConditionWrapper = null;

        if (myCondition != null)
        {
            myConditionWrapper = new ConditionWrapper(myCondition);
        }

        gameStatus.put("cardsOnTable", cardsOnTable);
        gameStatus.put("conditionsActive", game.getConditionsList().size() > 0);
        gameStatus.put("myCondition", myConditionWrapper);
        gameStatus.put("currentPlayer", game.findCurrentPlayer().getName());
        gameStatus.put("phase", game.getPhase());
        gameStatus.put("actions", game.findCurrentPlayer().getActions());
        gameStatus.put("buys", game.findCurrentPlayer().getBuys());
        gameStatus.put("coins", game.findCurrentPlayer().getCoins());

        writer.print(gson.toJson(gameStatus));
    }

    public void retrieveBuyableCards(String lobbyName, PrintWriter writer)
    {
        Game game = retrieveGameOfLobby(lobbyName);

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

    public void buyCard(String nickname, String lobbyName, String cardName)
    {
        Game game = retrieveGameOfLobby(lobbyName);

        try
        {
            if (game.findCurrentPlayer().getName().equals(nickname) && game.getPhase() == 2)
            {
                game.buyCard(cardName);
            }
        }
        catch (CardNotAvailableException ex)
        {
            ex.printStackTrace();
            //Do nothing
        }
    }

    public void discardCard(String nickname, String lobbyName, String cardName)
    {
        Game game = retrieveGameOfLobby(lobbyName);

        Player thisPlayer = game.getPlayer(nickname);
        Condition thisPlayersCondition = game.getConditionsList().get(thisPlayer);
        boolean conditionHasType = false;
        int cardType = 0;
        if (thisPlayersCondition instanceof RemoveCardsCondition)
        {
            RemoveCardsCondition thisPlayerCondition = (RemoveCardsCondition) thisPlayersCondition;
            cardType = thisPlayerCondition.getType();
            conditionHasType = (cardType != 0);
        }
        Card card = thisPlayer.getHand().findCard(cardName);

        if (card != null)
        {
            if (conditionHasType)
            {
                if (cardType == card.getType())
                {
                    thisPlayer.getHand().removeCard(card);
                }
            }
            else
            {
                thisPlayer.getHand().removeCard(card);
            }
        }
    }

    public void discardMultiple(String nickname, String lobbyName, String cardlist)
    {
        Game game = retrieveGameOfLobby(lobbyName);

        if (!game.findCurrentPlayer().getName().equals(nickname)) return;

        Player currentPlayer = game.findCurrentPlayer();

        String[] cards = cardlist.split(",");

        for (String card : cards)
        {
            game.discardCard(currentPlayer.getHand().findCard(card));
        }

        RemoveCardsThenAddCondition condition = (RemoveCardsThenAddCondition) game.getConditionsList().get(currentPlayer);
        condition.finish();
    }

    public void endActions(String lobbyName)
    {
        Game game = retrieveGameOfLobby(lobbyName);

        if (game.getPhase() == 0)
        {
            game.advancePhase();
        }
    }

    public void endTurn(String lobbyName)
    {
        Game game = retrieveGameOfLobby(lobbyName);

        game.advancePlayer();
    }

    public void playTreasures(String lobbyName)
    {
        Game game = retrieveGameOfLobby(lobbyName);

        game.playTreasures();
    }

    public void putCardOnTable(String lobbyName, String cardName)
    {
        Game game = retrieveGameOfLobby(lobbyName);

        try
        {
            game.playCard(cardName);
        }
        catch (CardNotAvailableException ex)
        {
            ex.printStackTrace();
        }
    }

    private Game retrieveGameOfLobby(String lobbyName)
    {
        Game game = null;

        try
        {
            game = gameEngine.findLobby(lobbyName).getGame();
        }
        catch (LobbyNotFoundException e)
        {
            e.printStackTrace();
        }

        return game;
    }
}
