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
    private boolean destroyCard;

    public RemoveCardsCondition(Player player, int cardsToRemove)
    {
        super(player);

        this.cardsToRemove = cardsToRemove;
        this.handSize = player.getHand().size();
        this.destroyCard = false;
    }

    public RemoveCardsCondition(Player player, int cardsToRemove, boolean destroyCard)
    {
        this(player, cardsToRemove);

        this.destroyCard = destroyCard;
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

        return ((handSize - playerHand.size() == cardsToRemove) || playerHand.size() == 0);
    }

    public int getType()
    {
        return type;
    }

    public boolean isDestroyCard()
    {
        return destroyCard;
    }
}
