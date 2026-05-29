package se433_testing_project.unit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import se433_testing_project.Item;

@Tag("unit")
class ItemTest 
{

    @Test
    void constructsWithValidValues() 
    {
        Item item = new Item("Pen", 2.50, 3);
        assertEquals("Pen", item.getName());
        assertEquals(2.50, item.getPrice());
        assertEquals(3, item.getQuantity());
    }

    @Test
    void priceAtMinimumIsAccepted() 
    {
        assertDoesNotThrow(() -> new Item("Pen", 1.00, 1));
    }

    @Test
    void priceBelowMinimumThrows() 
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Item("Pen", 0.99, 1));
        assertEquals("Price must be at least $1.00", e.getMessage());
    }

    @Test
    void quantityAtMinimumIsAccepted() 
    {
        assertDoesNotThrow(() -> new Item("Pen", 2.00, 1));
    }

    @Test
    void quantityBelowMinimumThrows() 
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Item("Pen", 2.00, 0));
        assertEquals("Quantity must be at least 1", e.getMessage());
    }

    @Test
    void nullNameThrows() 
    {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Item(null, 2.00, 1));
        assertEquals("Item name cannot be null or empty", e.getMessage());
    }

    @Test
    void blankNameThrows() 
    {
        assertThrows(IllegalArgumentException.class, () -> new Item("   ", 2.00, 1));
    }

    @Test
    void getSubtotalMultipliesPriceByQuantity() 
    {
        assertEquals(7.50, new Item("Pen", 2.50, 3).getSubtotal());
    }

    @Test
    void setQuantityUpdatesValueAndSubtotal() 
    {
        Item item = new Item("Pen", 2.50, 3);
        item.setQuantity(5);
        assertEquals(5, item.getQuantity());
        assertEquals(12.50, item.getSubtotal());
    }

    @Test
    void setQuantityToExactlyOneIsAccepted() 
    {
        Item item = new Item("Pen", 2.50, 3);
        item.setQuantity(1);   // boundary: 1 is valid, kills the < / <= mutant
        assertEquals(1, item.getQuantity());
    }

    @Test
    void setQuantityBelowMinimumThrows() 
    {
        Item item = new Item("Pen", 2.50, 3);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> item.setQuantity(0));
        assertEquals("Quantity must be at least 1", e.getMessage());
    }

}
