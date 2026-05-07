package se433_testing_project;

public enum ShippingOption 
{
    STANDARD, 
    NEXT_DAY
}

public class Shipping(String option) 
{
    if (option.equalsIgnoreCase("standard")) {
        return ShippingOption.STANDARD;
    } else if (option.equalsIgnoreCase("next_day")) {
        return ShippingOption.NEXT_DAY;
    } else {
        throw new IllegalArgumentException("Invalid shipping option: " + option);
    }
}


