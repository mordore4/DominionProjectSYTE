package dominion;

import dominion.exceptions.CardNotAvailableException;
import dominion.util.*;

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
            case 9:
                gainCardCostingUpTo(game);
                break;
            case 12:
                curseOtherPlayers(game);
                break;
            case 13:
                adventurerSpecialAbility(game);
                break;
            case 14:
                bureaucratSpecialAbility(game);
                break;
            case 15:
                cellarSpecialAbility(game);
                break;
            case 19:
                militiaSpecialAbility(game);
                break;
            case 17:
                councilRoomSpecial(game);
                break;
            case 25:
                gainSilver(game);
                break;
            case 27:
                moneylenderSpecialAbility(game);
                break;
            case 28:
                remodelSpecialAbility(game);
                break;
            case 29:
                mineSpecialAbility(game);
                break;
        }
    }

    public void doAbility(Game game, Card card) throws CardNotAvailableException
    {
        switch (id)
        {
            case 6:
                game.trashCardFromPlayer(game.findCurrentPlayer(), card);
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

    public void doAbility(Game game, boolean discardDeck)
    {
        switch (id)
        {
            case 16:
                chancellorSpecialAbility(game, discardDeck);
                break;
        }
    }

    private void adventurerSpecialAbility(Game game)
    {
        Revealer revealer = game.getRevealer();
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
            revealer.addCardToReveal(currentPlayer.getName(), currentCard);
        }
        game.moveCardsToDiscardPile(cardsToDiscard);
    }

    private void bureaucratSpecialAbility(Game game)
    {
        Revealer revealer = game.getRevealer();
        Player playingPlayer = game.findCurrentPlayer();
        for (Player player : game.getPlayers())
        {
            if (player != playingPlayer)
            {
                Deck hand = player.getHand();
                if (hand.checkHandForType(2))
                {
                    boolean foundCard = false;
                    for (int i = 0; !foundCard && i < hand.getCards().size(); i++)
                    {
                        Card currentCard = hand.getCards().get(i);
                        if (currentCard.getType() == 2)
                        {
                            game.moveThisCardFromTo(currentCard,hand.getCards(),player.getDeck().getCards());
                            foundCard = true;
                            revealer.addCardToReveal(player.getName(), currentCard);
                        }
                    }
                }
                else
                {
                    for (Card currentCard : hand.getCards())
                    {
                        revealer.addCardToReveal(player.getName(), currentCard);
                    }
                }
            }
        }
    }

    public void militiaSpecialAbility(Game game)
    {
        for (Player player : game.getPlayers())
        {
            if (player != game.findCurrentPlayer() && player.getHand().findCard("moat") == null)
            {
                //Discard down to 3 cards, so we take the hand size and subtract 3
                //If our hand size is below zero, math.max will take 0 for us
                //because 0 is larger than any negative number
                RemoveCardsCondition newCondition = new RemoveCardsCondition(player, Math.max(player.getHand().size() - 3, 0));
                game.addCondition(newCondition);
            }
        }
    }

    private void chancellorSpecialAbility(Game game, boolean discardDeck)
    {
        if (discardDeck)
        {
            game.moveCardsToDiscardPile(game.findCurrentPlayer().getDeck().getCards());
        }
    }

    private void councilRoomSpecial(Game game)
    {
        Player playingPlayer = game.findCurrentPlayer();
        for (Player player : game.getPlayers())
        {
            if (player != playingPlayer)
            {
                player.getDeck().takeTopCard(player.getHand(),player.getDiscardPile());
            }
        }
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

    private void moneylenderSpecialAbility(Game game)
    {
        Deck hand = game.findCurrentPlayer().getHand();
        ArrayList<Card> handCards = hand.getCards();
        boolean destroyedCopper = false;
        for(int i = 0;!destroyedCopper && i< handCards.size();i++ )
        {
            Card currentCard = handCards.get(i);
            if (currentCard.getName().equals("copper"))
            {
                hand.removeCard(currentCard);
                destroyedCopper = true;
            }
        }
    }

    private void gainCardCostingUpTo(Game game)
    {
        GainCardCondition newCondition = new GainCardCondition(game.findCurrentPlayer(), game, amount);

        game.addCondition(newCondition);
    }

    private void cellarSpecialAbility(Game game)
    {
        RemoveCardsThenAddCondition newCondition = new RemoveCardsThenAddCondition(game.findCurrentPlayer(), game);

        game.addCondition(newCondition);
    }

    private void remodelSpecialAbility(Game game)
    {
        RemodelCondition newCondition = new RemodelCondition(game);

        game.addCondition(newCondition);
    }

    private void mineSpecialAbility(Game game)
    {
        MineCondition newCondition = new MineCondition(game.findCurrentPlayer(), game);

        game.addCondition(newCondition);
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
