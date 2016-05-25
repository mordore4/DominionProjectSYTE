package dominion.tests;

import dominion.Ability;
import dominion.Card;
import dominion.GameEngine;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Digaly on 12/05/2016.
 */
public class TestHelper
{
    private HashMap<String, Card> testCardList;
    private String[] defaultKingdomCards;

    public TestHelper()
    {
        fillTestCardList();
        defaultKingdomCards = new String[] {"cellar", "market", "militia", "mine", "moat", "remodel", "smithy", "village", "woodcutter", "workshop"};
    }

    public String[] getDefaultKingdomCards()
    {
        return defaultKingdomCards;
    }

    public HashMap<String, Card> getTestCardList()
    {
        return testCardList;
    }

    public void fillTestCardList()
    {
        testCardList = new HashMap<>();

        testCardList.put("adventurer", new Card("adventurer", 3, 6, 0, new Ability[] {}));
        testCardList.put("bureaucrat", new Card("bureaucrat", 4, 4, 0, new Ability[] {}));
        testCardList.put("cellar", new Card("cellar", 3, 2, 0, new Ability[] {new Ability(1, 1), new Ability(15, -1)}));
        testCardList.put("chancellor", new Card("chancellor", 3, 3, 0, new Ability[] {}));
        testCardList.put("chapel", new Card("chapel", 3, 2, 0, new Ability[] {new Ability(5, 4)}));
        testCardList.put("copper", new Card("copper", 1, 0, 0, new Ability[] {new Ability(3, 1)}));
        testCardList.put("council room", new Card("council room", 3, 5, 0, new Ability[] {}));
        testCardList.put("curse", new Card("curse", 2, 0, 0, new Ability[] {new Ability(10, -1)}));
        testCardList.put("duchy", new Card("duchy", 2, 5, 0, new Ability[] {new Ability(10, 3)}));
        testCardList.put("estate", new Card("estate", 2, 2, 0, new Ability[] {new Ability(10, 1)}));
        testCardList.put("feast", new Card("feast", 3, 4, 0, new Ability[] {}));
        testCardList.put("festival", new Card("festival", 3, 5, 0, new Ability[] {new Ability(1, 2), new Ability(2, 1), new Ability(3, 2)}));
        testCardList.put("gardens", new Card("gardens", 6, 4, 0, new Ability[] {}));
        testCardList.put("gold", new Card("gold", 1, 6, 0, new Ability[] {new Ability(3, 3)}));
        testCardList.put("laboratory", new Card("laboratory", 3, 5, 0, new Ability[] {}));
        testCardList.put("library", new Card("library", 3, 5, 0, new Ability[] {}));
        testCardList.put("market", new Card("market", 3, 5, 0, new Ability[] {}));
        testCardList.put("militia", new Card("militia", 4, 4, 0, new Ability[] {}));
        testCardList.put("mine", new Card("mine", 3, 5, 0, new Ability[] {}));
        testCardList.put("moat", new Card("moat", 5, 2, 0, new Ability[] {}));
        testCardList.put("moneylender", new Card("moneylender", 3, 4, 0, new Ability[] {}));
        testCardList.put("province", new Card("province", 2, 8, 0, new Ability[] {new Ability(10, 6)}));
        testCardList.put("remodel", new Card("remodel", 3, 4, 0, new Ability[] {}));
        testCardList.put("silver", new Card("silver", 1, 3, 0, new Ability[] {new Ability(3, 2)}));
        testCardList.put("smithy", new Card("smithy", 3, 4, 0, new Ability[] {new Ability(4, 3)}));
        testCardList.put("spy", new Card("spy", 4, 4, 0, new Ability[] {}));
        testCardList.put("thief", new Card("thief", 4, 4, 0, new Ability[] {}));
        testCardList.put("throne room", new Card("throne room", 3, 4, 0, new Ability[] {}));
        testCardList.put("village", new Card("village", 3, 3, 0, new Ability[] {new Ability(1, 2), new Ability(4, 1)}));
        testCardList.put("witch", new Card("witch", 4, 4, 0, new Ability[] {new Ability(4, 2), new Ability(9, 0)}));
        testCardList.put("woodcutter", new Card("woodcutter", 3, 3, 0, new Ability[] {new Ability(2, 1), new Ability(3, 2)}));
        testCardList.put("workshop", new Card("workshop", 3, 3, 0, new Ability[] {new Ability(9, 4)}));
    }

    public void printArrayStatements()
    {
        GameEngine gameEngine = null;

        try
        {
            gameEngine = new GameEngine();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        HashMap<String, Card> cardList = gameEngine.getCardList();

        for (Card card : cardList.values())
        {
            System.out.print(String.format("testCardList.put(\"%s\", new Card(\"%s\", %s, %s, 0, new Ability[] {", card.getName(), card.getName(), card.getType(), card.getCost()));

            boolean isFirst = true;

            for (Ability ability : card.getAbilities())
            {
                if (!isFirst)
                {
                    System.out.print(", ");
                }
                System.out.print(String.format("new Ability(%s, %s)",ability.getId(), ability.getAmount()));

                isFirst = false;
            }
            System.out.println("}));");
        }
    }

    public void main(String[] args)
    {
        printArrayStatements();
    }
}
