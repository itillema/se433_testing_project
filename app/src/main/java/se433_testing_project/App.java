package se433_testing_project;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class App 
{
    private final Scanner scanner;
    private final PrintStream out;

    public App(Scanner scanner, PrintStream out) 
    {
        this.scanner = scanner;
        this.out = out;
    }

    public void run() 
    {
        out.println("=== Welcome to the Shopping Application ===");

        String name = promptNonEmpty("Enter your name: ");
        String state = promptNonEmpty("Enter your state of residence (e.g., IL, CA, NY, TX): ");
        ShippingOption shipping = promptShipping();

        ShoppingCart cart = new ShoppingCart();
        OrderProcessor processor = new OrderProcessor(cart, name, state, shipping);

        boolean running = true;
        while (running) 
        {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) 
            {
                case "1":
                    handleAddItem(cart);
                    break;

                case "2":
                    handleViewCart(cart);
                    break;

                case "3":
                    handleEditQuantity(cart);
                    break;

                case "4":
                    handleRemoveItem(cart);
                    break;

                case "5":
                    handleGetTotal(processor);
                    break;

                case "6":
                    handleCheckout(processor);
                    running = false;
                    break;

                case "7":
                    out.println("Goodbye!");
                    running = false;
                    break;

                default:
                    out.println("Invalid choice. Please enter a number from 1 to 7.");
            }
        }
    }

    private void printMenu() {
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

    private void handleAddItem(ShoppingCart cart) {
        try 
        {
            String itemName = promptNonEmpty("Item name: ");
            double price = promptDouble("Item price: $");
            int quantity = promptInt("Quantity: ");
            Item item = new Item(itemName, price, quantity);
            int count = cart.addItem(item);
            out.println("Item added. Cart now contains " + count + " item(s).");
        } 

        catch (IllegalArgumentException e) 
        {
            out.println("Error: " + e.getMessage());
        }
    }

    private void handleViewCart(ShoppingCart cart) 
    {
        List<Item> items = cart.getItems();
        if (items.isEmpty()) 
        {
            out.println("Your cart is empty.");
            return;
        }

        out.println("--- Cart Contents ---");

        for (Item item : items) 
        {
            out.printf("  %s | price=$%.2f | qty=%d | subtotal=$%.2f%n",
                item.getName(), item.getPrice(), item.getQuantity(), item.getSubtotal());
        }
        out.printf("Raw total: $%.2f%n", cart.getRawTotal());


    }

    private void handleEditQuantity(ShoppingCart cart) 
    {
        try 
        {
            String itemName = promptNonEmpty("Name of item to edit: ");
            int newQuantity = promptInt("New quantity: ");
            cart.editQuantity(itemName, newQuantity);
            out.println("Quantity updated.");
        } 
        
        catch (IllegalArgumentException e) 
        {
            out.println("Error: " + e.getMessage());
        }
    }

    private void handleRemoveItem(ShoppingCart cart) 
    {
        String itemName = promptNonEmpty("Name of item to remove: ");
        boolean removed = cart.removeItem(itemName);
        if (removed) 
        {
            out.println("Item removed.");
        } 
        
        else 
        {
            out.println("Item not found in cart.");
        }
    }

    private void handleGetTotal(OrderProcessor processor) 
    {
        try 
        {
            double total = processor.calculateTotal();
            out.printf("Current total (including tax and shipping): $%.2f%n", total);
        } 
        
        catch (IllegalStateException e) 
        {
            out.println("Error: " + e.getMessage());
        }
    }

    private void handleCheckout(OrderProcessor processor) 
    {
        try 
        {
            double total = processor.calculateTotal();
            out.printf("Final total: $%.2f%n", total);
            String result = processor.checkout();
            out.println(result);
        } 
        
        catch (IllegalStateException e) 
        {
            out.println("Error: " + e.getMessage());
        }
    }

    private String promptNonEmpty(String prompt) 
    {
        while (true) 
            {
            out.print(prompt);
            String input = scanner.nextLine();

            if (input != null && !input.trim().isEmpty()) 
            {
                return input.trim();
            }

            out.println("Input cannot be empty.");
        }
    }

    private int promptInt(String prompt) 
    {
        while (true) 
        {
            out.print(prompt);
            String input = scanner.nextLine().trim();
            try 
            {
                return Integer.parseInt(input);
            } 
            
            catch (NumberFormatException e) 
            {
                out.println("Please enter a valid integer.");
            }
        }
    }

    private double promptDouble(String prompt) 
    {
        while (true) 
            {
            out.print(prompt);
            String input = scanner.nextLine().trim();
            try 
            {
                return Double.parseDouble(input);
            } 
            catch (NumberFormatException e) 
            {
                out.println("Please enter a valid number.");
            }
        }
    }

    private ShippingOption promptShipping() 
    {
        while (true) 
            {
            out.print("Shipping option (STANDARD or NEXT_DAY): ");
            String input = scanner.nextLine();
            try 
            {
                return ShippingOption.fromString(input);
            } 

            catch (IllegalArgumentException e) 
            {
                out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) 
    {
        try (Scanner scanner = new Scanner(System.in)) 
        {
            new App(scanner, System.out).run();
        }
    }
}
