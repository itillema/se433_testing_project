package se433_testing_project;

public enum ShippingOption 
{
    STANDARD(10.0),
    NEXT_DAY(25.0);

    private final double baseCost;

    ShippingOption(double baseCost) 
    {
        this.baseCost = baseCost;
    }

    public double getBaseCost() 
    {
        return baseCost;
    }

    public double calculateShippingCost(double rawPrice) 
    {
        if (this == STANDARD && rawPrice > 50.0) 
        {
            return 0.0;
        }

        return baseCost;

    }

    public static ShippingOption fromString(String option) 
    {
        if (option == null) 
        {
            throw new IllegalArgumentException("Shipping option cannot be null");
        }

        String normalized = option.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        
        if (normalized.equals("STANDARD")) 
        {
            return STANDARD;
        } 
        
        else if (normalized.equals("NEXT_DAY")) 
        {
            return NEXT_DAY;
        } 
        
        else 
        {
            throw new IllegalArgumentException("Invalid shipping option: " + option);
        }
    }
}
