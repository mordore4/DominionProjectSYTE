package dominion.util;

import dominion.Player;

import java.util.ArrayList;

/**
 * Created by Digaly on 19/05/2016.
 */
public class ConditionsList
{
    private ArrayList<Condition> conditions;

    public ConditionsList()
    {
        conditions = new ArrayList<>();
    }

    public void add(Condition condition)
    {
        conditions.add(condition);
    }

    private void removeCompleteConditions()
    {
        ArrayList<Condition> toRemove = new ArrayList<>();

        for (int i = 0; i < conditions.size(); i++)
        {
            Condition currentCondition = conditions.get(i);

            if (currentCondition.isFulfilled())
            {
                toRemove.add(currentCondition);
            }
        }

        for (Condition condition : toRemove)
        {
            conditions.remove(condition);
        }
    }

    public ArrayList<Condition> conditionsOfPlayer(Player player)
    {
        removeCompleteConditions();

        ArrayList<Condition> results = new ArrayList<>();

        for (int i = 0; i < conditions.size(); i++)
        {
            Condition currentCondition = conditions.get(i);

            if (currentCondition.getPlayer() == player)
            {
                    results.add(currentCondition);
            }
        }

        return results;
    }
}
