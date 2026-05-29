package se433_testing_project.integration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import se433_testing_project.Item;
import se433_testing_project.OrderProcessor;
import se433_testing_project.ShippingOption;
import se433_testing_project.ShoppingCart;


// Integration tests forfull pricing pipeline, OrderProcessor > ShoppingCart > Item, TaxCalculator, ShippingOption
@Tag("integration")
class OrderPipelineTest 
{

    private OrderProcessor processor(String state, ShippingOption ship, Item... items) 
    {
        ShoppingCart cart = new ShoppingCart();
        for (Item i : items) 
        {
            cart.addItem(i);
        }

        return new OrderProcessor(cart, "Alice", state, ship);
    }

    @Test
    void taxedStateWithFreeStandardShippingOverFifty() 
    {
        // 60 in IL: 60 + 6% (3.60) + free standard shipping = 63.60
        OrderProcessor p = processor("IL", ShippingOption.STANDARD, new Item("Widget", 30.0, 2));
        assertEquals(63.60, p.calculateTotal());
    }

    @Test
    void untaxedStateWithChargedStandardShippingAtThreshold() 
    {
        // 50 in TX: no tax + $10 standard (50 is not "over" 50) = 60.00
        OrderProcessor p = processor("TX", ShippingOption.STANDARD, new Item("Mug", 25.0, 2));
        assertEquals(60.00, p.calculateTotal());
    }

    @Test
    void nextDayShippingIsAlwaysCharged() 
    {
        //  60 in NY: 60 + 6% (3.60) + $25 next-day = 88.60
        OrderProcessor p = processor("NY", ShippingOption.NEXT_DAY, new Item("Widget", 30.0, 2));
        assertEquals(88.60, p.calculateTotal());
    }

    @Test
    void minimumPurchaseExactlyOneDollarPasses() 
    {
        OrderProcessor p = processor("TX", ShippingOption.STANDARD, new Item("Tiny", 1.0, 1));
        assertDoesNotThrow(p::calculateTotal);
    }

    @Test
    void belowMinimumPurchaseThrowsWithExactMessage() 
    {
        OrderProcessor p = processor("TX", ShippingOption.STANDARD); // empty cart, raw 0.0
        IllegalStateException e = assertThrows(IllegalStateException.class, p::calculateTotal);
        assertEquals("Purchase amount must be at least $1.0 (current: $0.0)", e.getMessage());
    }

    @Test
    void maximumPurchaseExactlyAtLimitPasses() 
    {
        OrderProcessor p = processor("TX", ShippingOption.STANDARD, new Item("Big", 99999.99, 1));
        assertEquals(99999.99, p.calculateTotal());
    }

    @Test
    void aboveMaximumPurchaseThrowsWithExactMessage() 
    {
        OrderProcessor p = processor("TX", ShippingOption.STANDARD, new Item("Huge", 50000.0, 2));
        IllegalStateException e = assertThrows(IllegalStateException.class, p::calculateTotal);
        assertEquals("Purchase amount cannot exceed $99999.99 (current: $100000.0)", e.getMessage());
    }

    @Test
    void checkoutReturnsTransactionCompletedForValidCart() 
    {
        OrderProcessor p = processor("IL", ShippingOption.STANDARD, new Item("Widget", 30.0, 2));
        assertEquals("transaction completed", p.checkout());
    }

    @Test
    void checkoutValidatesBeforeCompleting() 
    {
        OrderProcessor p = processor("TX", ShippingOption.STANDARD); // empty cart
        assertThrows(IllegalStateException.class, p::checkout);
    }
}
