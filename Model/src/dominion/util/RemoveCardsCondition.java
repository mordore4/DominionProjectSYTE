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
    private int type;

    public RemoveCardsCondition(Player player, int cardsToRemove)
    {
        super(player);

        this.cardsToRemove = cardsToRemove;
        this.handSize = player.getHand().size();
    }

    public RemoveCardsCondition(Player player, int cardsToRemove, int type)
    {
        this(player, cardsToRemove);
        this.type = type;
    }

    @Override
    public boolean isFulfilled()
    {
        Deck playerHand = getPlayer().getHand();

        return handSize - playerHand.size() == cardsToRemove || playerHand.size() == 0;
    }

    public int getType()
    {
        return type;
    }
}
