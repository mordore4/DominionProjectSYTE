package dominion.util;

import dominion.Card;
import dominion.Deck;
import dominion.Game;
import dominion.Player;

import java.util.ArrayList;

/**
 * Created by Tom Dobbelaere on 24/05/2016.
 */
public class MineCondition extends Condition
{
    private ArrayList<Card> startHand;
    private transient Game game;
    private int cardsToRemove;

    public MineCondition(Player player, Game game)
    {
        super(player);

        this.startHand = (ArrayList<Card>) player.getHand().getCards().clone();
        this.game = game;
        this.cardsToRemove = 1;
    }

    @Override
    public boolean isFulfilled()
    {
        ArrayList<Card> playerHand = getPlayer().getHand().getCards();
        Card removedCard = null;

        for (Card card : startHand)
        {
            if (!playerHand.contains(card) && !card.getName().equals("mine") && card.getType() == 1)
            {
                removedCard = card;
            }
        }

        if (removedCard != null)
        {
            GainCardToHandCondition condition = new GainCardToHandCondition(getPlayer(), game, getPlayer().getValueOfLastTrashedCard() + 3, 1);
            game.addCondition(condition);
        }

        return false;
    }
}
