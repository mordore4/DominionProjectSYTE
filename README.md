# DominionProjectSYTE

## Issues
- Enkel kingdomcards beveiligd
- Duurdere kaarten kunnen gekocht worden als workshop actief is
- Amount checken voor chat 

## SQL Database
Card types reference:

1. Treasure
2. Victory
3. Action
4. Action attack
5. Action reaction
6. Gardens

## Model

This readme will cover usage of various classes in the Model project.
Model contains all core game logic, as well as persistence logic.

Not all classes are covered yet, except those that already have actual functionality.

### persistence/Database.java
This class is responsible for accessing the dominion database, as well as executing SQL statements on it.

#### Usage:
When executing a query with an instance of `Database`, it will return a DatabaseResults object for SELECT queries, and null for INSERT, UPDATE, etc..

The following code will get all cards with type 1 from the database, and return the results:

```Java

        Database db = new Database();

        DatabaseResults result = db.executeQuery("SELECT * FROM Card WHERE type = ?", "1");

        for (int i = 0; i < result.size(); i++)
        {
            DatabaseRecord currentRecord = result.getRecord(i);

            System.out.println(currentRecord.getValue("cardName"));
            System.out.println(currentRecord.getValue("cost"));
        }

```

This code will insert a new card into the database:

```Java

        Database db = new Database();

        db.executeQuery("INSERT INTO Card (cardName, type, cost) VALUES (?, ?, ?)", "dummy", "1", "4");
```

As you can see, after the first parameter, you can give any number of string arguments that will be put in place of the question marks in the first argument, in order. This is because of prepared statements.

For more information, check out the DatabaseRecord and DatabaseResults classes.

### persistence/DatabaseResults.java

The `DatabaseResults` class is no more than an ArrayList of `DatabaseRecord` objects.

You can retrieve a certain row number (starting from zero) with `getRecord(int rowNumber)`

### persistence/DatabaseRecord.java

This class represents a record in the database, and can be retrieved from a `DatabaseResults` object.

The implementation uses a hashmap so you can get column values by specifying the column name.

An example would be `currentRecord.getValue("cardName")`
