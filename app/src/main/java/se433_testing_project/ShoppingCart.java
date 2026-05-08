package se433_testing_project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCart 
{
    private final List<Item> items = new ArrayList<>();

    public int addItem(Item item) 
    {
        if (item == null) 
        {
            throw new IllegalArgumentException("Item cannot be null");
        }

        items.add(item);
        return items.size();
    }

    public boolean removeItem(String itemName) 
    {
        if (itemName == null) 
        {
            return false;
        }

        for (int i = 0; i < items.size(); i++) 
        {
            if (items.get(i).getName().equalsIgnoreCase(itemName)) 
            {
                items.remove(i);
                return true;
            }
        }

        return false;
    }

    public void editQuantity(String itemName, int newQuantity) 
    {
        for (Item item : items) 
        {
            if (item.getName().equalsIgnoreCase(itemName)) 
            {
                item.setQuantity(newQuantity);
                return;
            }
        }

        throw new IllegalArgumentException("Item not found in cart: " + itemName);
    }

    public List<Item> getItems() 
    {
        return Collections.unmodifiableList(items);
    }

    public int getItemCount() 
    {
        return items.size();
    }

    public double getRawTotal() 
    {
        double total = 0.0;
        for (Item item : items) 
        {
            total += item.getSubtotal();
        }

        return total;
    }

    public void clear() 
    {
        items.clear();
    }
}
