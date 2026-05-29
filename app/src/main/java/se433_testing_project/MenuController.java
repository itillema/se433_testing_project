package se433_testing_project;


// Controls the menu and action handlers
public class MenuController 
{
    private final ConsoleInput in;
    private final ConsoleOutput out;

    public MenuController(ConsoleInput in, ConsoleOutput out) 
    {
        this.in = in;
        this.out = out;
    }

    public void run() 
    {
        out.welcome();

        String name = in.promptNonEmpty("Enter your name: ");
        String state = in.promptNonEmpty("Enter your state of residence (e.g., IL, CA, NY, TX): ");
        ShippingOption shipping = in.promptShipping();

        ShoppingCart cart = new ShoppingCart();
        OrderProcessor processor = new OrderProcessor(cart, name, state, shipping);

        boolean running = true;
        while (running) 
        {
            out.menu();
            running = dispatch(in.readMenuChoice(), cart, processor);
        }
    }

    // menu action loop handler
    boolean dispatch(String choice, ShoppingCart cart, OrderProcessor processor) 
    {
        switch (choice) 
        {
            case "1":
                addItem(cart);
                return true;
            case "2":
                viewCart(cart);
                return true;
            case "3":
                editQuantity(cart);
                return true;
            case "4":
                removeItem(cart);
                return true;
            case "5":
                getTotal(processor);
                return true;
            case "6":
                checkout(processor);
                return false;
            case "7":
                out.goodbye();
                return false;
            case ConsoleInput.NO_INPUT:
                return false;
            default:
                out.invalidChoice();
                return true;
        }
    }

    void addItem(ShoppingCart cart) 
    {
        try 
        {
            String name = in.promptNonEmpty("Item name: ");
            double price = in.promptDouble("Item price: $");
            int quantity = in.promptInt("Quantity: ");
            int count = cart.addItem(new Item(name, price, quantity));
            out.itemAdded(count);
        } 
        
        catch (IllegalArgumentException e) 
        {
            out.error(e.getMessage());
        }
    }

    void viewCart(ShoppingCart cart) 
    {
        out.cartContents(cart.getItems(), cart.getRawTotal());
    }

    void editQuantity(ShoppingCart cart) 
    {
        try 
        {
            String name = in.promptNonEmpty("Name of item to edit: ");
            int newQuantity = in.promptInt("New quantity: ");
            cart.editQuantity(name, newQuantity);
            out.quantityUpdated();

        } 
        
        catch (IllegalArgumentException e) 
        {
            out.error(e.getMessage());
        }
    }

    void removeItem(ShoppingCart cart) 
    {
        String name = in.promptNonEmpty("Name of item to remove: ");
        out.itemRemoved(cart.removeItem(name));
    }

    void getTotal(OrderProcessor processor) 
    {
        try 
        {
            out.currentTotal(processor.calculateTotal());
        } 
        
        catch (IllegalStateException e) 
        {
            out.error(e.getMessage());
        }
    }

    void checkout(OrderProcessor processor) 
    {
        try 
        {
            out.finalTotal(processor.calculateTotal());
            out.checkoutResult(processor.checkout());
        } 
        
        catch (IllegalStateException e) 
        {
            out.error(e.getMessage());
        }
        
    }
}
