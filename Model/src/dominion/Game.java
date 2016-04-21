package dominion;

/**
 * Created by Digaly on 23/03/2016.
 */
public class Game
{
    private Card[] fixedCards;
    private String kingdomCardSet;
    private Card[] kingdomCards;
    private Player currentPlayer;
    private Player[] players;
    private int phase;
    private boolean isOver;
    private GameEngine gameEngine;

    public Game(Account[] accounts, String kingdomCardSet, GameEngine gameEngine)
    {
        this.kingdomCardSet = kingdomCardSet;
        this.gameEngine = gameEngine;
        this.kingdomCards = cardSet(kingdomCardSet);
        fixedCards = makeFixedCards(accounts.length);
        players = new Player[accounts.length];

        for (int i = 0; i < accounts.length; i++)
        {
            Player newPlayer = new Player(gameEngine, this);

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
            cardSetNames = new String[]{"cellar", "market", "militia", "mine", "moat", "remodel", "smithy", "village", "woodcutter", "workshop",};
        }

        for (int i = 0; i < cardSetNames.length; i++)
        {
            Card card = gameEngine.findCard(cardSetNames[i]);
            cardSet[i] = new Card(card, this);
            cardSet[i].setAmount(10);
        }
        return cardSet;
    }

    private Card[] makeFixedCards(int playerCount)
    {
        Card[] fixedCards = new Card[7];

        fixedCards[0] = new Card(gameEngine.findCard("province"), this);
        fixedCards[1] = new Card(gameEngine.findCard("duchy"), this);
        fixedCards[2] = new Card(gameEngine.findCard("estate"), this);
        fixedCards[3] = new Card(gameEngine.findCard("curse"), this);
        fixedCards[4] = new Card(gameEngine.findCard("gold"), this);
        fixedCards[5] = new Card(gameEngine.findCard("silver"), this);
        fixedCards[6] = new Card(gameEngine.findCard("copper"), this);


        if (playerCount == 2)
        {
            fixedCards[0].setAmount(8);
            fixedCards[1].setAmount(8);
            fixedCards[2].setAmount(8);
        }
        else
        {
            fixedCards[0].setAmount(12);
            fixedCards[1].setAmount(12);
            fixedCards[2].setAmount(12);
        }

        fixedCards[3].setAmount(playerCount * 10 - 10);
        fixedCards[4].setAmount(30);
        fixedCards[5].setAmount(40);
        fixedCards[6].setAmount(60);
        //fixedCards[6].setAmount(60 - players.length * 7);

        return fixedCards;
    }

    public Player getPlayer(String name)
    {
        Player player = null;

        for (Player p : players)
        {
            if (p.getAccount().getName().equals(name))
                player = p;
        }

        return player;
    }

    public Card retrieveCard(String cardName, Boolean isKingdomCard)
    {
        Card foundCard = null;

        Card[] searchArray = fixedCards;
        if (isKingdomCard) searchArray = kingdomCards;

        for (Card c : searchArray)
        {
            if (c.getName().equals(cardName))
                foundCard = c;
        }

        return foundCard;
    }
}
