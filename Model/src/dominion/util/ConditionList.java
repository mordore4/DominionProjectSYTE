package dominion.util;

import dominion.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Digaly on 19/05/2016.
 */
public class ConditionList
{
    private Map<Player, Condition> conditions;

    public ConditionList()
    {
        conditions = new HashMap<>();
    }

    public void add(Condition condition)
    {
        conditions.put(condition.getPlayer(), condition);
    }

    private void removeCompleteConditions()
    {
        ArrayList<Player> keysToRemove = new ArrayList<>();

        for (Condition condition : conditions.values())
        {
            if (condition.isFulfilled())
            {
                if (!keysToRemove.contains(condition.getPlayer()))
                {
                    keysToRemove.add(condition.getPlayer());
                }
            }
        }

        for (Player player : keysToRemove)
        {
            conditions.remove(player);
        }
    }

    public Condition get(Player player)
    {
        removeCompleteConditions();

        return conditions.get(player);
    }

    public int size()
    {
        return conditions.size();
    }
}
