package dominion;

import dominion.exceptions.CardNotAvailableException;
import dominion.persistence.Database;
import dominion.persistence.DatabaseResults;
import dominion.util.Condition;
import dominion.util.ConditionList;
import dominion.util.GainCardCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Digaly on 23/03/2016.
 */
public class Game
{
    private Card[] cards;
    private CopyOnWriteArrayList<Card> cardsOnTable;
    private int currentPlayerIndex;
    private Player[] players;
    private int phase;
    private boolean isOver;
    private HashMap<String, Card> cardList;
    private ConditionList conditionsList;
    private Revealer revealer;

    public Game(String[] playerNames, String[] kingdomCards, HashMap<String, Card> cardList)
    {
        this.setCardList(cardList);
        makeCards(kingdomCards, playerNames.length);
        initGameState(playerNames);
        revealer = new Revealer();
        conditionsList = new ConditionList();
    }

    private void initGameState(String[] playerNames)
    {
        addPlayers(playerNames);

        currentPlayerIndex = pickRandomPlayer();
        phase = -1;
        isOver = false;
        advancePhase();
    }

    private void addPlayers(String[] playerNames)
    {
        players = new Player[playerNames.length];
        cardsOnTable = new CopyOnWriteArrayList<>();

        for (int i = 0; i < playerNames.length; i++)
        {
            Player newPlayer = new Player();

            newPlayer.setName(playerNames[i]);

            setUpPlayerCards(newPlayer);

            players[i] = newPlayer;
        }
    }

    private void setUpPlayerCards(Player newPlayer)
    {
        createStartingDeck(newPlayer);

        makeHand(newPlayer);
    }

    private int pickRandomPlayer()
    {
        return (int) (Math.random() * players.length);
    }

    public void advancePhase()
    {
        phase++;

        if (phase == 0 && !findCurrentPlayer().hasActionCards())
        {
            phase++;
        }

        /*if (phase == 2 && findBuyableCards().size() == 0)
        {
            phase++;
        }*/

        if (phase >= 3)
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

    private void makeCards(String[] kingdomCards, int playerCount)
    {
        cards = new Card[17];
        addKingdomCardsToCards(kingdomCards);
        addFixedCardsToCards(playerCount);
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

        cards[13].setAmount((playerCount - 1) * 10);
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

            if (currentCard.isActionCard())
                currentPlayer.setActions(currentPlayer.getActions() - 1);

            if (phase == 0)
            {

                if ((!currentPlayer.getHand().containsActionCards() || currentPlayer.getActions() == 0) && conditionsList.size() == 0)
                    advancePhase();
            }
            else if (phase == 1)
            {
                if (!currentPlayer.getHand().checkHandForType(1))
                    advancePhase();
            }
        }
        else throw new CardNotAvailableException();
    }

    public void trashCardFromPlayer(Player player, Card card)
    {
        player.setValueOfLastTrashedCard(card.getCost());
        player.getHand().removeCard(card);
    }

    public void playTreasures()
    {
        Player currentPlayer = findCurrentPlayer();

        ArrayList<Card> handClone = (ArrayList<Card>) currentPlayer.getHand().getCards().clone();

        for (Card card : handClone)
        {
            if (card.getType() == 1)
            {
                try
                {
                    playCard(card.getName());
                }
                catch (CardNotAvailableException e)
                {
                    //Do nothing
                }
            }
        }
    }

    public void executeCardAbilities(Card currentCard) throws CardNotAvailableException
    {
        Ability[] cardAbilities = currentCard.getAbilities();

        for (Ability ability : cardAbilities)
        {
            if (ability.getId() < 6 || ability.getId() == 12 || ability.getId() == 15 || ability.getId() == 9 || ability.getId() >= 25)
            {
                ability.doAbility(this);
            }
            else if (ability.getId() == 6)
            {
                ability.doAbility(this, currentCard);
            }
            else if (ability.getId() == 13 || ability.getId() == 14 || ability.getId() == 19)
            {
                ability.doAbility(this);
            }
        }
    }

    public void buyCard(String cardName) throws CardNotAvailableException
    {
        Card thisCard = retrieveCard(cardName);
        int cardCost = thisCard.getCost();
        Player currentPlayer = findCurrentPlayer();

        boolean hasGainCardCondition = getConditionsList().hasConditionOfType(GainCardCondition.class);


        if ((currentPlayer.getCoins() >= cardCost && currentPlayer.getBuys() > 0) || hasGainCardCondition)
        {
            addCard(cardName);

            if (!hasGainCardCondition)
            {
                currentPlayer.setBuys(currentPlayer.getBuys() - 1);
                currentPlayer.setCoins(currentPlayer.getCoins() - cardCost);
            }
            else
            {
                getConditionsList().removeCompleteConditions();
            }
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

    public void addCardToPlayer(String cardName, Player player) throws CardNotAvailableException
    {
        addCardToPileFromPlayer(cardName, player.getDiscardPile());
        Card card = retrieveCard(cardName);
        if (card.getType() == 2)
        {
            int amountToAdd = card.getAbilities()[0].getAmount();
            player.addVictoryPoints(amountToAdd);
        }
    }

    public void addCardToPileFromPlayer(String cardName, Deck pile) throws CardNotAvailableException
    {
        Card card = retrieveCard(cardName);

        if (card.getAmount() > 0)
        {
            card.setAmount(card.getAmount() - 1);

            Card newCard = new Card(card);
            newCard.setAmount(1);

            pile.addCard(newCard);
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
        cardsOnTable = new CopyOnWriteArrayList<>();

        ArrayList<Card> currentHand = currentPlayer.getHand().getCards();

        for (int i = 0; i < currentHand.size(); i++)
        {
            currentPlayer.getDiscardPile().addCard(currentHand.get(i));
        }

        makeHand(currentPlayer);
        checkIfGameIsOver();
    }

    public void moveCardsToDiscardPile(ArrayList<Card> fromPile)
    {
        moveCardsFromTo(fromPile, findCurrentPlayer().getDiscardPile().getCards());
    }

    public void moveCardsFromTo(ArrayList<Card> fromPile, ArrayList<Card> toPile)
    {
        for (int i = fromPile.size() - 1; i >= 0; i--)
        {
            Card cardToMove = fromPile.get(i);
            moveThisCardFromTo(cardToMove, fromPile, toPile);
        }
    }


    public void moveThisCardFromTo(Card thisCard, ArrayList<Card> fromPile, ArrayList<Card> toPile)
    {
        toPile.add(thisCard);
        fromPile.remove(thisCard);
    }

    public void discardCardFromPlayer(Card card, Player player)
    {
        moveThisCardFromTo(card, player.getHand().getCards(), player.getDiscardPile().getCards());
    }

    public void discardCard(Card card)
    {
        discardCardFromPlayer(card, findCurrentPlayer());
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
        boolean hasEnoughMoney = money >= card.getCost();

        boolean hasGainCardCondition = getConditionsList().hasConditionOfType(GainCardCondition.class);

        if (hasGainCardCondition)
        {
            GainCardCondition condition = (GainCardCondition) getConditionsList().get(findCurrentPlayer());

            hasEnoughMoney = card.getCost() <= condition.getCost();
        }

        return hasEnoughMoney && findCurrentPlayer().getBuys() > 0 && card.getAmount() > 0;
    }

    public void checkIfGameIsOver()
    {
        int emptyStacks = 0;
        Card province = cards[10];
        boolean provinceStackIsEmpty = province.getAmount() == 0;
        for (Card card : cards)
        {
            if (card.getAmount() == 0)
            {
                emptyStacks += 1;
            }
        }
        boolean threeStacksAreEmpty = (emptyStacks >= 3);
        if (provinceStackIsEmpty || threeStacksAreEmpty)
        {
            isOver = true;
        }
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

    public CopyOnWriteArrayList<Card> getCardsOnTable()
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

    private void setCardList(HashMap<String, Card> cardList)
    {
        this.cardList = cardList;
    }

    public Revealer getRevealer()
    {
        return revealer;
    }

    public ConditionList getConditionsList()
    {
        if (conditionsList.hasChanged() && conditionsList.size() == 0)
        {
            if (findCurrentPlayer().hasActionCards() && findCurrentPlayer().getActions() > 0)
            {
                setPhase(0);
            }
            else
            {
                setPhase(1);
            }
        }

        return conditionsList;
    }

    public void addCondition(Condition condition)
    {
        conditionsList.add(condition);

        boolean isGainCardCondition = getConditionsList().hasConditionOfType(GainCardCondition.class);

        if (isGainCardCondition)
        {
            phase = 2;
        }
    }

    public void setPhase(int phase)
    {
        this.phase = phase;

        if (this.phase == 0 && !findCurrentPlayer().hasActionCards())
        {
            this.phase++;
        }
    }
}
