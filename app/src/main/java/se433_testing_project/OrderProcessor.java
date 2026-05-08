package se433_testing_project;

public class OrderProcessor 
{
    public static final double MIN_PURCHASE = 1.0;
    public static final double MAX_PURCHASE = 99999.99;

    private final ShoppingCart cart;
    private String customerName;
    private String state;
    private ShippingOption shippingOption;

    public OrderProcessor(ShoppingCart cart, String customerName, String state, ShippingOption shippingOption) 
    {
        if (cart == null) 
        {
            throw new IllegalArgumentException("Cart cannot be null");
        }

        if (customerName == null || customerName.trim().isEmpty()) 
        {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }

        if (state == null || state.trim().isEmpty()) 
        {
            throw new IllegalArgumentException("State cannot be null or empty");
        }

        if (shippingOption == null) 
        {
            throw new IllegalArgumentException("Shipping option cannot be null");
        }

        this.cart = cart;
        this.customerName = customerName;
        this.state = state;
        this.shippingOption = shippingOption;
    }

    public ShoppingCart getCart() 
    {
        return cart;
    }

    public String getCustomerName() 
    {
        return customerName;
    }

    public String getState() 
    {
        return state;
    }

    public ShippingOption getShippingOption() 
    {
        return shippingOption;
    }

    public void setShippingOption(ShippingOption shippingOption) 
    {
        if (shippingOption == null) 
        {
            throw new IllegalArgumentException("Shipping option cannot be null");
        }

        this.shippingOption = shippingOption;
    }

    public double calculateTotal() 
    {
        double rawTotal = cart.getRawTotal();
        if (rawTotal < MIN_PURCHASE) 
        {
            throw new IllegalStateException("Purchase amount must be at least $" + MIN_PURCHASE + " (current: $" + rawTotal + ")");
        }

        if (rawTotal > MAX_PURCHASE) 
        {
            throw new IllegalStateException("Purchase amount cannot exceed $" + MAX_PURCHASE + " (current: $" + rawTotal + ")");
        }

        double tax = TaxCalculator.calculateTax(state, rawTotal);
        double shipping = shippingOption.calculateShippingCost(rawTotal);
        return rawTotal + tax + shipping;
    }

    public String checkout() 
    {
        calculateTotal();
        return "transaction completed";
    }
}
