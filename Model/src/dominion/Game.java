package dominion;

import dominion.exceptions.CardNotAvailableException;
import dominion.persistence.Database;
import dominion.persistence.DatabaseResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Digaly on 23/03/2016.
 */
public class Game
{
    //private Card[] kingdomCards;
    private Card[] cards;
    private ArrayList<Card> cardsOnTable;
    private int currentPlayerIndex;
    private Player[] players;
    private int phase;
    private boolean isOver;
    private HashMap<String, Card> cardList;
    private Database database;

    public Game(String[] playerNames, String kingdomCardSet, HashMap<String, Card> cardList)
    {
        this.cardList = cardList;
        initDatabase();
        makeCards(kingdomCardSet, playerNames.length);
        initGameState(playerNames);
    }

    public Game(String[] playerNames, String[] kingdomCards, HashMap<String, Card> cardList)
    {
        this.cardList = cardList;
        initDatabase();
        makeCards(kingdomCards, playerNames.length);
        initGameState(playerNames);
    }

    private void initGameState(String[] playerNames)
    {
        players = new Player[playerNames.length];
        cardsOnTable = new ArrayList<>();

        for (int i = 0; i < playerNames.length; i++)
        {
            Player newPlayer = new Player();

            newPlayer.setName(playerNames[i]);

            createStartingDeck(newPlayer);

            makeHand(newPlayer);

            players[i] = newPlayer;
        }

        currentPlayerIndex = pickRandomPlayer();
        phase = -1;
        isOver = false;
        advancePhase();
    }

    private void initDatabase()
    {
        try
        {
            database = new Database();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
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
        cleanup();

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

    private void makeCards(String name, int playerCount)
    {
        String[] kingdomCardsInSet = getKingdomCardsIn(name);
        makeCards(kingdomCardsInSet, playerCount);
    }

    private void makeCards(String[] kingdomCards, int playerCount)
    {
        cards = new Card[17];
        addKingdomCardsToCards(kingdomCards);
        addFixedCardsToCards(playerCount);
    }


    private String[] getKingdomCardsIn(String cardSet)
    {
        DatabaseResults results = database.executeQuery("SELECT cardName FROM CardsetCard WHERE cardset = ?", cardSet);

        String[] cardNames = new String[results.size()];

        for (int i = 0; i < results.size(); i++)
        {
            cardNames[i] = results.getRecord(i).getValue("cardName");
        }

        return cardNames;
    }

    private void addKingdomCardsToCards(String[] kingdomCardsInSet)
    {
        for (int i = 0; i < kingdomCardsInSet.length; i++)
        {
            Card card = cardList.get(kingdomCardsInSet[i]);
            cards[i] = new Card(card);
            cards[i].setAmount(10);
        }
    }

    /*private Card[] cardSet(String name)
    {
        String[] cardSetNames = null;
        Card[] cardSet = new Card[10];
        if (name.equals("default"))
        {
            cardSetNames = new String[]{"cellar", "market", "militia", "mine", "moat", "remodel", "smithy", "village", "woodcutter", "workshop",};
        }
        else if (name.equals("testWitch"))
        {
            cardSetNames = new String[]{"cellar", "market", "militia", "mine", "moat", "remodel", "witch", "village", "woodcutter", "workshop",};
        }

        for (int i = 0; i < cardSetNames.length; i++)
        {
            Card card = cardList.get(cardSetNames[i]);
            cardSet[i] = new Card(card);
            cardSet[i].setAmount(10);
        }
        return cardSet;
    }*/

    private void addFixedCardsToCards(int playerCount)
    {
        cards[10] = new Card(cardList.get("province"));
        cards[11] = new Card(cardList.get("duchy"));
        cards[12] = new Card(cardList.get("estate"));
        cards[13] = new Card(cardList.get("curse"));
        cards[14] = new Card(cardList.get("gold"));
        cards[15] = new Card(cardList.get("silver"));
        cards[16] = new Card(cardList.get("copper"));

        if (playerCount == 2)
        {
            cards[10].setAmount(8);
            cards[11].setAmount(8);
            cards[12].setAmount(8);
        }
        else
        {
            cards[10].setAmount(12);
            cards[11].setAmount(12);
            cards[12].setAmount(12);
        }

        cards[13].setAmount(playerCount * 10 - 10);
        cards[14].setAmount(30);
        cards[15].setAmount(40);
        cards[16].setAmount(60);
    }

    public Player getPlayer(String name)
    {
        Player player = null;

        for (Player p : players)
        {
            if (p.getName().equals(name))
                player = p;
        }

        return player;
    }

    public Card retrieveCard(String cardName)
    {

        Card foundCard = null;

        for (Card c : cards)
        {
            if (c.getName().equals(cardName))
                foundCard = c;
        }

        return foundCard;
    }

    public void playCard(String cardName) throws CardNotAvailableException
    {
        Player currentPlayer = findCurrentPlayer();
        Card currentCard = currentPlayer.getHand().findCard(cardName);

        if (currentCard != null && currentCard.getType() != 2)
        {
            executeCardAbilities(currentCard);

            //discardCard(currentCard);
            cardsOnTable.add(currentCard);
            currentPlayer.getHand().removeCard(currentCard);

            int cardType = currentCard.getType();

            if (cardType == 3 || cardType == 4 || cardType == 5)
                currentPlayer.setActions(currentPlayer.getActions() - 1);

            if (phase == 0)
            {
                if (!currentPlayer.getHand().containsActionCards() || currentPlayer.getActions() == 0)
                    advancePhase();
            }
        }
        else throw new CardNotAvailableException();
    }

    public void executeCardAbilities(Card currentCard) throws CardNotAvailableException
    {
        Ability[] cardAbilities = currentCard.getAbilities();

        for (Ability ability : cardAbilities)
        {
            if (ability.getId() < 6 || ability.getId() == 12)
            {
                ability.doAbility(this);
            }
            else if (ability.getId() == 6)
            {
                ability.doAbility(this, currentCard);
            }
        }
    }

    public void buyCard(String cardName) throws CardNotAvailableException
    {
        int cardCost = retrieveCard(cardName).getCost();
        Player currentPlayer = findCurrentPlayer();

        if (currentPlayer.getCoins() >= cardCost && currentPlayer.getBuys() > 0)
        {
            addCard(cardName);
            currentPlayer.setBuys(currentPlayer.getBuys() - 1);
            currentPlayer.setCoins(currentPlayer.getCoins() - cardCost);
        }
    }

    public void gainCardCostingUpTo(String cardName, int value) throws CardNotAvailableException
    {
        int cardCost = retrieveCard(cardName).getCost();

        if (value >= cardCost)
        {
            addCard(cardName);
        }
    }

    public void discardCardFromPlayer(Card card, Player player)
    {
        player.getDiscardPile().addCard(card);
        player.getHand().removeCard(card);
    }

    public void discardCard(Card card)
    {
        discardCardFromPlayer(card, findCurrentPlayer());
    }

    public void addCardToPlayer(String cardName, Player player) throws CardNotAvailableException
    {
        Card card = retrieveCard(cardName);

        if (card.getAmount() > 0)
        {
            card.setAmount(card.getAmount() - 1);

            Card newCard = new Card(card);
            newCard.setAmount(1);

            player.getDiscardPile().addCard(newCard);
        }
        else throw new CardNotAvailableException();
    }

    public void addCard(String cardName) throws CardNotAvailableException
    {
        addCardToPlayer(cardName, findCurrentPlayer());
    }

    private void createStartingDeck(Player player)
    {
        for (int i = 0; i < 7; i++)
        {
            try
            {
                addCardToPlayer("copper", player);
            }
            catch (CardNotAvailableException e)
            {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 3; i++)
        {
            try
            {
                addCardToPlayer("estate", player);
            }
            catch (CardNotAvailableException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void makeHand(Player player)
    {
        Deck deck = player.getDeck();
        Deck hand = player.getHand();
        Deck discardPile = player.getDiscardPile();
        deck.makeHand(hand, discardPile);
    }

    private void cleanup()
    {
        Player currentPlayer = findCurrentPlayer();

        for (int i = 0; i < cardsOnTable.size(); i++)
        {
            currentPlayer.getDiscardPile().addCard(cardsOnTable.get(i));
        }
        cardsOnTable = new ArrayList<>();

        ArrayList<Card> currentHand = currentPlayer.getHand().getCards();

        for (int i = 0; i < currentHand.size(); i++)
        {
            currentPlayer.getDiscardPile().addCard(currentHand.get(i));
        }

        makeHand(currentPlayer);
    }

    public ArrayList<String> findBuyableCards()
    {
        ArrayList<String> buyableCards = new ArrayList<>();

        for (Card card : cards)
        {
            if (isBuyable(card))
            {
                buyableCards.add(card.getName());
            }
        }
        return buyableCards;
    }

    public boolean isBuyable(Card card)
    {
        int money = findCurrentPlayer().getCoins();
        return money >= card.getCost() && findCurrentPlayer().getBuys() > 0 && card.getAmount() > 0;
    }

    public boolean getIsOver()
    {
        return isOver;
    }

    public Card[] getCards()
    {
        return cards;
    }

    public Card[] getKingdomCards()
    {
        return Arrays.copyOfRange(cards, 0, 10);
    }

    public Card[] getFixedCards()
    {
        return Arrays.copyOfRange(cards, 10, 17);
    }

    public void setCurrentPlayerIndex(int index)
    {
        currentPlayerIndex = index;
    }

    public ArrayList<Card> getCardsOnTable()
    {
        return cardsOnTable;
    }

    public Card findCard(String name)
    {
        return cardList.get(name);
    }

    public Player[] getPlayers()
    {
        return players;
    }
}
