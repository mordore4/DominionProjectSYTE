package dominion;

/**
 * Created by Sam on 23/03/2016.
 */
public class Card
{
    private String name;
    private Ability[] abilities;
    private int type;
    private int cost;
    private int amount;

    public Card(String cardName, int type, int cost, int amount, Ability[] abilities)
    {
        this.setName(cardName);
        this.setType(type);
        this.setCost(cost);
        this.setAmount(amount);
        this.setAbilities(abilities);
    }

    public Card(Card card)
    {
        this.setName(card.getName());
        this.setAbilities(card.getAbilities());
        this.setType(card.getType());
        this.setCost(card.getCost());
        this.setAmount(card.getAmount());
    }

    public Ability[] getAbilities()
    {
        return abilities;
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

    public boolean isActionCard()
    {
        return (type == 3 || type == 4 || type == 5);
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

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    private void setName(String name)
    {
        this.name = name;
    }

    private void setAbilities(Ability[] abilities)
    {
        this.abilities = abilities;
    }

    private void setType(int type)
    {
        this.type = type;
    }

    private void setCost(int cost)
    {
        this.cost = cost;
    }
}
