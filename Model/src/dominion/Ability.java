package dominion;

import dominion.exceptions.CardNotAvailableException;

import java.util.ArrayList;

/**
 * Created by Sam on 31/03/2016.
 */
public class Ability
{
    private int id;
    private int amount;
    ArrayList<Card> cardsToDiscard;

    public Ability(int id, int amount)
    {
        this.id = id;
        this.amount = amount;
        cardsToDiscard = new ArrayList<>();
    }

    public void doAbility(Game game)
    {
        switch (id)
        {
            case 1:
                addActions(game.findCurrentPlayer());
                break;
            case 2:
                addBuys(game.findCurrentPlayer());
                break;
            case 3:
                addCoins(game.findCurrentPlayer());
                break;
            case 4:
                addCards(game.findCurrentPlayer());
                break;
            case 5:
                break;
            case 12:
                curseOtherPlayers(game);
                break;

            case 25:
                gainSilver(game);
                break;
        }
    }

    public void doAbility(Game game, Revealer revealer)
    {
        switch (id)
        {
            case 13:
                adventurerSpecialAbility(game, revealer);
        }
    }

    public void doAbility(Game game, Card card) throws CardNotAvailableException
    {
        switch (id)
        {
            case 6:
                game.findCurrentPlayer().getHand().removeCard(card);
                break;
            case 9:
                gainCardCostingUpTo(game, card);
                break;
            case 11:
                playCardTwice(game, card);
                break;
        }
    }

    public void doAbility(Game game, ArrayList<Card> cards)
    {
        switch (id)
        {
            case 7:
                trashCards(game.findCurrentPlayer(), cards);
                break;
        }
    }

    public void adventurerSpecialAbility(Game game, Revealer revealer)
    {
        int treasuresFound = 0;
        Player currentPlayer = game.findCurrentPlayer();
        Deck deck = currentPlayer.getDeck();
        Deck discardPile = currentPlayer.getDiscardPile();
        Deck hand = currentPlayer.getHand();

        for (int i = 0; treasuresFound < 2 && i < (deck.size() + discardPile.size()); i++)
        {
            Card currentCard = deck.getTopCard(discardPile);
            if (currentCard.getType() == 1)
            {
                treasuresFound += 1;
                deck.takeTopCard(hand, discardPile);
            }
            else
            {
                deck.putTopCardIn(cardsToDiscard, discardPile);
            }
            revealer.addCardToReveal(currentCard);
        }
        discardCardsToDiscard(game);
    }

    public void discardCardsToDiscard(Game game)
    {
        Deck discardPile = game.findCurrentPlayer().getDiscardPile();
        for (int i = cardsToDiscard.size() - 1; i >= 0; i--)
        {
            discardPile.addCard(cardsToDiscard.get(i));
            cardsToDiscard.remove(cardsToDiscard.get(i));
        }
    }


    private void addActions(Player currentPlayer)
    {
        int currentPlayerActions = currentPlayer.getActions();
        currentPlayer.setActions(currentPlayerActions + amount);
    }

    private void addBuys(Player currentPlayer)
    {
        int currentPlayerBuys = currentPlayer.getBuys();
        currentPlayer.setBuys(currentPlayerBuys + amount);
    }

    private void addCoins(Player currentPlayer)
    {
        int currentPlayerCoins = currentPlayer.getCoins();
        currentPlayer.setCoins(currentPlayerCoins + amount);
    }

    private void addCards(Player currentPlayer)
    {
        Deck discardPile = currentPlayer.getDiscardPile();
        Deck deck = currentPlayer.getDeck();
        for (int i = 0; i < amount; i++)
        {
            if (discardPile.size() + deck.size() != 0)
            {
                deck.takeTopCard(currentPlayer.getHand(), discardPile);
            }
        }
    }

    private void trashCards(Player currentPlayer, ArrayList<Card> cards)
    {
        Deck hand = currentPlayer.getHand();
        for (Card card : cards)
        {
            hand.removeCard(card);
        }
    }

    private void gainCardCostingUpTo(Game game, Card card) throws CardNotAvailableException
    {
        game.gainCardCostingUpTo(card.getName(), amount);
    }

    private void playCardTwice(Game game, Card card) throws CardNotAvailableException
    {
        for (int i = 0; i < 2; i++)
        {
            game.executeCardAbilities(card);
        }
        game.discardCard(card);
    }

    private void curseOtherPlayers(Game game)
    {
        Player playingPlayer = game.findCurrentPlayer();
        for (Player player : game.getPlayers())
        {
            if (player != playingPlayer)
            {
                try
                {
                    game.addCardToPlayer("curse", player);
                }
                catch (CardNotAvailableException e)
                {
                    //do nothing
                }
            }
        }
    }

    private void gainSilver(Game game)
    {
        try
        {
            game.addCard("silver");
        }
        catch (CardNotAvailableException e)
        {
            //do nothing
        }
    }



    public int getId()
    {
        return id;
    }

    public int getAmount()
    {
        return amount;
    }

    public String toString()
    {
        return id + " " + amount + " ";
    }

    public ArrayList<Card> getCardsToDiscard()
    {
        return cardsToDiscard;
    }
}
