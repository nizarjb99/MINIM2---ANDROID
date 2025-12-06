package dsa.upc.edu.listapp.github;

public class Item {

    public final int id;
    public final String name;
    public final int durability;
    public final int price;
    public final String emoji;
    public final String description;
    int quantity;


    public Item(int id, String name, int durability, int price, String emoji, String description, int quantity)
    {
        this.id = id;
        this.name = name;
        this.durability = durability;
        this.price = price;
        this.emoji = emoji;
        this.description = description;
        this.quantity = quantity;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDurability() {
        return durability;
    }

    public int getPrice() {
        return price;
    }

    public String getEmoji() {
        return emoji;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
