package se433_testing_project.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import se433_testing_project.OrderProcessor;
import se433_testing_project.ShippingOption;
import se433_testing_project.ShoppingCart;

@Tag("unit")
class OrderProcessorUnitTest 
{

    private ShoppingCart cart() 
    {
        return new ShoppingCart();
    }

    @Test
    void nullCartThrows() 
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new OrderProcessor(null, "Alice", "IL", ShippingOption.STANDARD));
        assertEquals("Cart cannot be null", e.getMessage());
    }

    @Test
    void nullNameThrows() 
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new OrderProcessor(cart(), null, "IL", ShippingOption.STANDARD));
        assertEquals("Customer name cannot be null or empty", e.getMessage());
    }

    @Test
    void blankNameThrows() 
    {
        assertThrows(IllegalArgumentException.class, () -> new OrderProcessor(cart(), "   ", "IL", ShippingOption.STANDARD));
    }

    @Test
    void nullStateThrows() 
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new OrderProcessor(cart(), "Alice", null, ShippingOption.STANDARD));
        assertEquals("State cannot be null or empty", e.getMessage());
    }

    @Test
    void blankStateThrows() 
    {
        assertThrows(IllegalArgumentException.class, () -> new OrderProcessor(cart(), "Alice", "  ", ShippingOption.STANDARD));
    }

    @Test
    void nullShippingThrows() 
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new OrderProcessor(cart(), "Alice", "IL", null));
        assertEquals("Shipping option cannot be null", e.getMessage());
    }

    @Test
    void gettersReturnConstructorValues() 
    {
        ShoppingCart c = cart();
        OrderProcessor p = new OrderProcessor(c, "Alice", "IL", ShippingOption.STANDARD);
        assertSame(c, p.getCart());
        assertEquals("Alice", p.getCustomerName());
        assertEquals("IL", p.getState());
        assertSame(ShippingOption.STANDARD, p.getShippingOption());
    }

    @Test
    void setShippingOptionUpdatesValue() 
    {
        OrderProcessor p = new OrderProcessor(cart(), "Alice", "IL", ShippingOption.STANDARD);
        p.setShippingOption(ShippingOption.NEXT_DAY);
        assertSame(ShippingOption.NEXT_DAY, p.getShippingOption());
    }

    @Test
    void setShippingOptionNullThrows() 
    {
        OrderProcessor p = new OrderProcessor(cart(), "Alice", "IL", ShippingOption.STANDARD);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> p.setShippingOption(null));
        assertEquals("Shipping option cannot be null", e.getMessage());
    }
}
