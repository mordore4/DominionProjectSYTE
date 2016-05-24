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
    private int lastSize;

    public ConditionList()
    {
        lastSize = 0;
        conditions = new HashMap<>();
    }

    public void add(Condition condition)
    {
        conditions.put(condition.getPlayer(), condition);
    }

    public void removeCompleteConditions()
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

    public boolean hasConditionOfType(Class<?> type)
    {
        removeCompleteConditions();

        for (Condition condition : conditions.values())
        {
            //if (condition.getClass().isInstance(type))
            if (type.isInstance(condition))
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasChanged()
    {
        removeCompleteConditions();

        if (lastSize != conditions.size())
        {
            lastSize = conditions.size();
            return true;
        }
        else
        {
            return false;
        }
    }

    public int size()
    {
        return conditions.size();
    }
}
