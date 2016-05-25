package dominion.util;

import dominion.Deck;
import dominion.Player;

/**
 * Created by Digaly on 19/05/2016.
 */
public class RemoveCardsCondition extends Condition
{
    private int cardsToRemove;
    private int handSize;

    public RemoveCardsCondition(Player player, int cardsToRemove)
    {
        super(player);

        this.cardsToRemove = cardsToRemove;
        this.handSize = player.getHand().size();
    }

    @Override
    public boolean isFulfilled()
    {
        Deck playerHand = getPlayer().getHand();

        return ((handSize - playerHand.size() == cardsToRemove) || playerHand.size() == 0);
    }
}
