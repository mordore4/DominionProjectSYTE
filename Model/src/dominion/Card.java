package dominion;

/**
 * Created by Sam on 23/03/2016.
 */
public class Card
{
    private String name;
    private Ability[] abilities; //possibly wrong, Abilities might become class
    private int type;
    private int cost;
    private int amount;

    public Card(String cardName, int type, int cost, int amount, Ability[] abilities)
    {
        name = cardName;
        this.type = type;
        this.cost = cost;
        this.amount = amount;
        this.abilities = abilities;
    }

    public Card(Card card)
    {
        name = card.getName();
        abilities = card.getAbilities();
        type = card.getType();
        cost = card.getCost();
        amount = card.getAmount();
    }

    public String getName()
    {
        return name;
    }

    public int getType()
    {
        return type;
    }

    public int getCost()
    {
        return cost;
    }

    public int getAmount()
    {
        return amount;
    }

    public Ability[] getAbilities()
    {
        return abilities;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public void setCost(int cost)
    {
        this.cost = cost;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public String toString()
    {
        String tmp = name + " " + type + " " + cost + " " + amount + " ";

        for (int i = 0; i < abilities.length; i++)
        {
            tmp += abilities[i].toString();
        }

        return tmp;
    }

    public Boolean equals(Card card)
    {
        Boolean nameCorrect = name.equals(card.getName());
        Boolean abilitiesCorrect = abilities == card.abilities;
        Boolean typeCorrect = type == card.type;
        Boolean costCorrect = cost == card.cost;
        Boolean amountCorrect = amount == card.amount;

        return(nameCorrect && abilitiesCorrect && typeCorrect && costCorrect && amountCorrect);
    }

}
