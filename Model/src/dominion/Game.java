package dominion;

import java.util.ArrayList;

/**
 * Created by Digaly on 23/03/2016.
 */
public class Game
{
    private String cardSetName;
    private Card[] cardSet;
    private Player currentPlayer;
    private Player[] players;
    private int phase;
    private boolean isOver;
    private GameEngine gameEngine;

    public Game(Account[] accounts, String cardSetName, GameEngine gameEngine)
    {
        this.cardSetName = cardSetName;
        this.gameEngine = gameEngine;
        this.cardSet = cardSet(cardSetName);
        players = new Player[accounts.length];

        for (int i = 0; i < accounts.length; i++)
        {
            Player newPlayer = new Player(gameEngine);

            newPlayer.setAccount(accounts[i]);

            players[i] = newPlayer;
        }
    }

    private Card[] cardSet(String name)
    {
        String[] cardSetNames = null;
        Card[] cardSet = new Card[10];
        if (name.equals("default"))
        {
            cardSetNames = new String[] {"cellar", "market", "militia", "mine", "moat", "remodel", "smithy", "village", "woodcutter", "workshop", };
        }

        for (int i = 0; i < cardSetNames.length; i++)
        {
            Card card = gameEngine.findCard(cardSetNames[i]);
            cardSet[i] = new Card(card);
            cardSet[i].setAmount(10);
        }
        return cardSet;
    }
}
