package se433_testing_project;

public class TaxCalculator 
{
    public static double calculateTax(String state, double amount)
    {
        if (state.toUpperCase().equals("IL") || state.toUpperCase().equals("CA") || state.toUpperCase().equals("NY"))
        {
            return amount * 0.06;
        }
        else
        {
            return amount * 0.00;
        }
    }


}
