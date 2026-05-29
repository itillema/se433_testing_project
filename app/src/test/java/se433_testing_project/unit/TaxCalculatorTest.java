package se433_testing_project.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import se433_testing_project.TaxCalculator;

@Tag("unit")
class TaxCalculatorTest 
{

    @Test
    void utilityClassIsInstantiable() 
    {
        // calculateTax is static; cover the implicit default constructor too.
        assertNotNull(new TaxCalculator());
    }

    @Test
    void illinoisChargesSixPercent() 
    {
        assertEquals(6.0, TaxCalculator.calculateTax("IL", 100.0));
    }

    @Test
    void californiaChargesSixPercent() 
    {
        assertEquals(6.0, TaxCalculator.calculateTax("CA", 100.0));
    }

    @Test
    void newYorkChargesSixPercent() 
    {
        assertEquals(6.0, TaxCalculator.calculateTax("NY", 100.0));
    }

    @Test
    void lowercaseStateIsNormalized() 
    {
        assertEquals(6.0, TaxCalculator.calculateTax("il", 100.0));
    }

    @Test
    void surroundingWhitespaceIsNormalized() 
    {
        assertEquals(6.0, TaxCalculator.calculateTax("  ny  ", 100.0));
    }

    @Test
    void untaxedStateChargesNothing() 
    {
        assertEquals(0.0, TaxCalculator.calculateTax("TX", 100.0));
    }

    @Test
    void nullStateChargesNothing() 
    {
        assertEquals(0.0, TaxCalculator.calculateTax(null, 100.0));
    }


    
}
