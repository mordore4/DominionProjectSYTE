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
    private ArrayList<Card> cardsToDiscard;

    public Ability(int id, int amount)
    {
        this.setId(id);
        this.setAmount(amount);
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
            case 13:
                adventurerSpecialAbility(game);
                break;
            case 25:
                gainSilver(game);
                break;
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

    public void adventurerSpecialAbility(Game game)
    {
        Revealer revealer = game.findCurrentPlayer().getRevealer();
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
        game.moveCardsToDiscardPile(cardsToDiscard);
    }

    public void bureaucratSpecialAbility(game game, Revealer revealer)
    {


    }

    private void addActions(Player currentPlayer)
    {
        currentPlayer.addActions(amount);
    }

    private void addBuys(Player currentPlayer)
    {
        currentPlayer.addBuys(amount);
    }

    private void addCoins(Player currentPlayer)
    {
        currentPlayer.addCoins(amount);
    }

    private void addCards(Player currentPlayer)
    {
        currentPlayer.addCards(amount);
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
        Deck deck = game.findCurrentPlayer().getDeck();
        try
        {
            game.addCardToPileFromPlayer("silver", deck);
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

    private void setId(int id)
    {
        this.id = id;
    }

    private void setAmount(int amount)
    {
        this.amount = amount;
    }
}
