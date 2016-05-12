package dominion;

import java.util.ArrayList;

/**
 * Created by Digaly on 23/03/2016.
 */
public class Game
{
    private Card[] fixedCards;
    private String kingdomCardSet;
    private Card[] kingdomCards;
    private int currentPlayerIndex;
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
            Player newPlayer = new Player(this);

            newPlayer.setAccount(accounts[i]);

            players[i] = newPlayer;
        }

        currentPlayerIndex = pickRandomPlayer();
        phase = -1;
        isOver = false;
        advancePhase();
    }

    private int pickRandomPlayer()
    {
        return (int) (Math.random() * players.length);
    }

    public void advancePhase()
    {
        phase++;

        if (phase == 0 && !findCurrentPlayer().hasActionCards()) phase++;

        if (phase >= 2)
        {
            advancePlayer();
        }
    }

    public void advancePlayer()
    {
        findCurrentPlayer().cleanup();

        currentPlayerIndex++;

        if (currentPlayerIndex > players.length - 1)
        {
            currentPlayerIndex = 0;
        }

        findCurrentPlayer().setBuys(1);
        findCurrentPlayer().setActions(1);
        findCurrentPlayer().setCoins(0);

        phase = -1;
        advancePhase();
    }

    public int getPhase()
    {
        return phase;
    }

    public Player findCurrentPlayer()
    {
        return players[currentPlayerIndex];
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
            cardSet[i] = new Card(card);
            cardSet[i].setAmount(10);
        }
        return cardSet;
    }

    private Card[] makeFixedCards(int playerCount)
    {
        Card[] fixedCards = new Card[7];

        fixedCards[0] = new Card(gameEngine.findCard("province"));
        fixedCards[1] = new Card(gameEngine.findCard("duchy"));
        fixedCards[2] = new Card(gameEngine.findCard("estate"));
        fixedCards[3] = new Card(gameEngine.findCard("curse"));
        fixedCards[4] = new Card(gameEngine.findCard("gold"));
        fixedCards[5] = new Card(gameEngine.findCard("silver"));
        fixedCards[6] = new Card(gameEngine.findCard("copper"));


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

    public Card retrieveCard(String cardName)
    {
        Card foundCard = null;

        for (Card c : fixedCards)
        {
            if (c.getName().equals(cardName))
                foundCard = c;
        }

        if (foundCard == null)
        {
            for (Card c : kingdomCards)
            {
                if (c.getName().equals(cardName))
                    foundCard = c;
            }
        }

        return foundCard;
    }

    public void playCard(String cardName)
    {
        Player currentPlayer = findCurrentPlayer();
        Card currentCard = currentPlayer.getHand().findCard(cardName);
        Ability[] cardAbilities = currentCard.getAbilities();

        for (Ability ability : cardAbilities)
        {
            ability.doAbility(this);
        }

        currentPlayer.getDiscardPile().addCard(currentCard);
        currentPlayer.getHand().removeCard(currentCard);
        int cardType = currentCard.getType();

        if (cardType == 3 || cardType == 4 || cardType == 5)
            currentPlayer.setActions(currentPlayer.getActions() - 1);
    }

    /*public Card retrieveCard(String cardName, Boolean isKingdomCard)
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
    }*/

    public String allCardsToString()
    {
        String out = "";

        for (int i = 0; i < kingdomCards.length; i++)
        {
            out += kingdomCards[i].toString() + "\n";
        }

        for (int i = 0; i < fixedCards.length; i++)
        {
            out += fixedCards[i].toString() + "\n";
        }

        return out;
    }

    public boolean getIsOver()
    {
        return isOver;
    }

    public Card[] getKingdomCards()
    {
        return kingdomCards;
    }

    public Card[] getFixedCards()
    {
        return fixedCards;
    }

    public void setCurrentPlayerIndex(int index) {currentPlayerIndex = index;}

    public Player getCurrentPlayer() {return players[currentPlayerIndex];}
}
