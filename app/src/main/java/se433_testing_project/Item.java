package se433_testing_project;

public class Item 
{
    private final String name;
    private final double price;
    private int quantity;

    public Item(String name, double price, int quantity) 
    {
        if (name == null || name.trim().isEmpty()) 
        {
            throw new IllegalArgumentException("Item name cannot be null or empty");
        }

        if (price < 1.0) 
        {
            throw new IllegalArgumentException("Price must be at least $1.00");
        }

        if (quantity < 1) 
        {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        this.name = name;
        this.price = price;
        this.quantity = quantity;

    }

    public String getName() 
    {
        return name;
    }

    public double getPrice() 
    {
        return price;
    }

    public int getQuantity() 
    {
        return quantity;
    }

    public void setQuantity(int quantity) 
    {
        if (quantity < 1) 
        {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        this.quantity = quantity;
    }

    public double getSubtotal() 
    {
        return price * quantity;
    }
}
