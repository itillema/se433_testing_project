package se433_testing_project.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import se433_testing_project.ShippingOption;

@Tag("unit")
class ShippingOptionTest 
{

    @Test
    void standardCostsTenAtExactlyFifty() 
    {
        assertEquals(10.0, ShippingOption.STANDARD.calculateShippingCost(50.00));
    }

    @Test
    void standardIsFreeJustOverFifty() 
    {
        assertEquals(0.0, ShippingOption.STANDARD.calculateShippingCost(50.01));
    }

    @Test
    void standardCostsTenJustUnderFifty() 
    {
        assertEquals(10.0, ShippingOption.STANDARD.calculateShippingCost(49.99));
    }

    @Test
    void nextDayCostsTwentyFiveBelowThreshold() 
    {
        assertEquals(25.0, ShippingOption.NEXT_DAY.calculateShippingCost(10.0));
    }

    @Test
    void nextDayCostsTwentyFiveAboveThreshold() 
    {
        assertEquals(25.0, ShippingOption.NEXT_DAY.calculateShippingCost(500.0));
    }

    @Test
    void getBaseCostReturnsConfiguredValue() 
    {
        assertEquals(10.0, ShippingOption.STANDARD.getBaseCost());
        assertEquals(25.0, ShippingOption.NEXT_DAY.getBaseCost());
    }

    @Test
    void fromStringParsesStandard() 
    {
        assertSame(ShippingOption.STANDARD, ShippingOption.fromString("STANDARD"));
    }

    @Test
    void fromStringParsesNextDay() 
    {
        assertSame(ShippingOption.NEXT_DAY, ShippingOption.fromString("NEXT_DAY"));
    }

    @Test
    void fromStringNormalizesSpaceToUnderscore() 
    {
        assertSame(ShippingOption.NEXT_DAY, ShippingOption.fromString("next day"));
    }

    @Test
    void fromStringNormalizesDashToUnderscore() 
    {
        assertSame(ShippingOption.NEXT_DAY, ShippingOption.fromString("next-day"));
    }

    @Test
    void fromStringIsCaseInsensitive() 
    {
        assertSame(ShippingOption.STANDARD, ShippingOption.fromString("standard"));
    }

    @Test
    void fromStringNullThrows() 
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ShippingOption.fromString(null));
        assertEquals("Shipping option cannot be null", e.getMessage());
    }

    @Test
    void fromStringInvalidThrows() 
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ShippingOption.fromString("bogus"));
        assertEquals("Invalid shipping option: bogus", e.getMessage());
    }
}
