package se433_testing_project;

import java.io.PrintStream;
import java.util.List;
import java.util.Locale;


// controls console output for user facing console messages
public class ConsoleOutput 
{
    private final PrintStream out;

    public ConsoleOutput(PrintStream out) 
    {
        this.out = out;
    }

    public void welcome() 
    {
        out.println("=== Welcome to the Shopping Application ===");
    }

    public void menu() 
    {
        out.println();
        out.println("--- Menu ---");
        out.println("1. Add item to cart");
        out.println("2. View cart contents");
        out.println("3. Edit item quantity");
        out.println("4. Remove item from cart");
        out.println("5. Get current total");
        out.println("6. Checkout");
        out.println("7. Quit without checking out");
        out.print("Choose an option: ");
    }

    // Print prompt text without trailing newline
    public void prompt(String text) 
    {
        out.print(text);
    }

    //print an informational line
    public void message(String text) 
    {
        out.println(text);
    }

    public void itemAdded(int count) 
    {
        out.println("Item added. Cart now contains " + count + " item(s).");
    }

    public void cartContents(List<Item> items, double rawTotal) 
    {
        if (items.isEmpty()) 
        {
            out.println("Your cart is empty.");

            return;
        }

        out.println("--- Cart Contents ---");

        for (Item item : items) 
        {
            out.printf(Locale.US, "  %s | price=$%.2f | qty=%d | subtotal=$%.2f%n", item.getName(), item.getPrice(), item.getQuantity(), item.getSubtotal());
        }

        out.printf(Locale.US, "Raw total: $%.2f%n", rawTotal);
    }

    public void quantityUpdated() 
    {
        out.println("Quantity updated.");
    }

    public void itemRemoved(boolean removed) 
    {
        if (removed) 
        {
            out.println("Item removed.");
        } 

        else 
            {
            out.println("Item not found in cart.");
        }
    }

    public void currentTotal(double total) 
    {
        out.printf(Locale.US, "Current total (including tax and shipping): $%.2f%n", total);
    }

    public void finalTotal(double total) 
    {
        out.printf(Locale.US, "Final total: $%.2f%n", total);
    }

    public void checkoutResult(String msg) 
    {
        out.println(msg);
    }

    public void error(String message) 
    {
        out.println("Error: " + message);
    }

    public void invalidChoice() 
    {
        out.println("Invalid choice. Please enter a number from 1 to 7.");
    }

    public void goodbye() 
    {
        out.println("Goodbye!");
    }
}
