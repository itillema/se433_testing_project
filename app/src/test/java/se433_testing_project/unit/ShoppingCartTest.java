package se433_testing_project.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import se433_testing_project.Item;
import se433_testing_project.ShoppingCart;

@Tag("unit")
class ShoppingCartTest 
{

    @Test
    void addItemReturnsRunningCount() 
    {
        ShoppingCart cart = new ShoppingCart();
        assertEquals(1, cart.addItem(new Item("Pen", 2.00, 1)));
        assertEquals(2, cart.addItem(new Item("Pad", 3.00, 1)));
    }

    @Test
    void addNullItemThrows() 
    {
        ShoppingCart cart = new ShoppingCart();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> cart.addItem(null));
        assertEquals("Item cannot be null", e.getMessage());
    }

    @Test
    void removeExistingItemReturnsTrue() 
    {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new Item("Pen", 2.00, 1));
        assertTrue(cart.removeItem("Pen"));
        assertEquals(0, cart.getItemCount());
    }

    @Test
    void removeMissingItemReturnsFalse() 
    {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new Item("Pen", 2.00, 1));
        assertFalse(cart.removeItem("Ghost"));
        assertEquals(1, cart.getItemCount());
    }

    @Test
    void removeNullNameReturnsFalse() 
    {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new Item("Pen", 2.00, 1));
        assertFalse(cart.removeItem(null));
    }

    @Test
    void removeIsCaseInsensitive() 
    {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new Item("Pen", 2.00, 1));
        assertTrue(cart.removeItem("pen"));
    }

    @Test
    void editQuantityUpdatesExistingItem() 
    {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new Item("Pen", 2.00, 1));
        cart.editQuantity("Pen", 4);
        assertEquals(4, cart.getItems().get(0).getQuantity());
    }

    @Test
    void editQuantityMissingItemThrows() 
    {
        ShoppingCart cart = new ShoppingCart();
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> cart.editQuantity("Ghost", 4));
        assertEquals("Item not found in cart: Ghost", e.getMessage());
    }

    @Test
    void getItemsIsUnmodifiable() 
    {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new Item("Pen", 2.00, 1));
        assertThrows(UnsupportedOperationException.class, () -> cart.getItems().add(new Item("Pad", 3.00, 1)));
    }

    @Test
    void getRawTotalSumsSubtotals() 
    {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new Item("Pen", 2.50, 2));   // 5.00
        cart.addItem(new Item("Pad", 3.00, 3));    // 9.00
        assertEquals(14.00, cart.getRawTotal());
    }

    @Test
    void getItemCountReflectsAdds() 
    {
        ShoppingCart cart = new ShoppingCart();
        assertEquals(0, cart.getItemCount());
        cart.addItem(new Item("Pen", 2.00, 1));
        assertEquals(1, cart.getItemCount());
    }

    @Test
    void clearEmptiesCart() 
    {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new Item("Pen", 2.00, 1));
        cart.clear();
        assertEquals(0, cart.getItemCount());
        assertEquals(0.0, cart.getRawTotal());
    }

    
}
