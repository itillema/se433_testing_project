package se433_testing_project;

public class TaxCalculator 
{

    public static final double TAX_RATE = 0.06;

    public static double calculateTax(String state, double amount) 
    {
        if (state == null) 
        {
            return 0.0;
        }

        String normalized = state.trim().toUpperCase();

        if (normalized.equals("IL") || normalized.equals("CA") || normalized.equals("NY")) 
        {
            return amount * TAX_RATE;
        }
        
        return 0.0;
    }
}
