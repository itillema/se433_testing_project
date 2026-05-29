package se433_testing_project.integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import se433_testing_project.ConsoleInput;
import se433_testing_project.ConsoleOutput;
import se433_testing_project.Item;
import se433_testing_project.MenuController;
import se433_testing_project.OrderProcessor;
import se433_testing_project.ShippingOption;
import se433_testing_project.ShoppingCart;


// Integration tests for MenuController wired to ConsoleInput and ConsoleOutput

@Tag("integration")
class MenuControllerTest 
{

    private ByteArrayOutputStream buf;

    private MenuController controller(String script) 
    {
        buf = new ByteArrayOutputStream();
        ConsoleOutput out = new ConsoleOutput(new PrintStream(buf, true, StandardCharsets.UTF_8));
        ConsoleInput in = new ConsoleInput(new Scanner(script), out);
        return new MenuController(in, out);
    }

    private String captured() 
    {
        return buf.toString(StandardCharsets.UTF_8);
    }

    private ShoppingCart cartWith(Item... items) 
    {
        ShoppingCart cart = new ShoppingCart();
        for (Item i : items) 
        {
            cart.addItem(i);
        }

        return cart;
    }

    private OrderProcessor processorFor(ShoppingCart cart, String state, ShippingOption ship) 
    {
        return new OrderProcessor(cart, "Alice", state, ship);
    }

    // handlers

    @Test
    void addItemAddsToCartAndReportsCount() 
    {
        ShoppingCart cart = new ShoppingCart();
        controller("Pen\n1.50\n2\n").addItem(cart);
        assertEquals(1, cart.getItemCount());
        assertTrue(captured().contains("Item added. Cart now contains 1 item(s)."));
    }

    @Test
    void addItemWithInvalidPriceShowsErrorAndLeavesCartEmpty() 
    {
        ShoppingCart cart = new ShoppingCart();
        controller("Cheap\n0.50\n1\n").addItem(cart);
        assertEquals(0, cart.getItemCount());
        assertTrue(captured().contains("Error: Price must be at least $1.00"));
    }

    @Test
    void viewCartShowsEmptyMessage() 
    {
        controller("").viewCart(new ShoppingCart());
        assertTrue(captured().contains("Your cart is empty."));
    }

    @Test
    void viewCartShowsItemsAndTotal() 
    {
        controller("").viewCart(cartWith(new Item("Pen", 2.50, 2)));
        assertTrue(captured().contains("--- Cart Contents ---"));
        assertTrue(captured().contains("Raw total: $5.00"));
    }

    @Test
    void editQuantityUpdatesItem() 
    {
        ShoppingCart cart = cartWith(new Item("Apple", 2.00, 1));
        controller("Apple\n5\n").editQuantity(cart);
        assertEquals(5, cart.getItems().get(0).getQuantity());
        assertTrue(captured().contains("Quantity updated."));
    }

    @Test
    void editQuantityMissingItemShowsError() 
    {
        ShoppingCart cart = cartWith(new Item("Apple", 2.00, 1));
        controller("Ghost\n5\n").editQuantity(cart);
        assertTrue(captured().contains("Error: Item not found in cart: Ghost"));
    }

    @Test
    void editQuantityBelowOneShowsError() 
    {
        ShoppingCart cart = cartWith(new Item("Apple", 2.00, 1));
        controller("Apple\n0\n").editQuantity(cart);
        assertTrue(captured().contains("Error: Quantity must be at least 1"));
    }

    @Test
    void removeItemPresentConfirmsRemoval() 
    {
        ShoppingCart cart = cartWith(new Item("Apple", 2.00, 1));
        controller("Apple\n").removeItem(cart);
        assertEquals(0, cart.getItemCount());
        assertTrue(captured().contains("Item removed."));
    }

    @Test
    void removeItemAbsentReportsNotFound() 
    {
        controller("Ghost\n").removeItem(new ShoppingCart());
        assertTrue(captured().contains("Item not found in cart."));
    }

    @Test
    void getTotalShowsComputedTotal() 
    {
        ShoppingCart cart = cartWith(new Item("Widget", 30.0, 2));
        controller("").getTotal(processorFor(cart, "IL", ShippingOption.STANDARD));
        assertTrue(captured().contains("Current total (including tax and shipping): $63.60"));
    }

    @Test
    void getTotalOnEmptyCartShowsError() 
    {
        ShoppingCart cart = new ShoppingCart();
        controller("").getTotal(processorFor(cart, "IL", ShippingOption.STANDARD));
        assertTrue(captured().contains("Error: Purchase amount must be at least $1.0 (current: $0.0)"));
    }

    @Test
    void checkoutShowsFinalTotalAndCompletes() 
    {
        ShoppingCart cart = cartWith(new Item("Widget", 30.0, 2));
        controller("").checkout(processorFor(cart, "IL", ShippingOption.STANDARD));
        assertTrue(captured().contains("Final total: $63.60"));
        assertTrue(captured().contains("transaction completed"));
    }

    @Test
    void checkoutOverMaximumShowsError() 
    {
        ShoppingCart cart = cartWith(new Item("Huge", 50000.0, 2));
        controller("").checkout(processorFor(cart, "TX", ShippingOption.STANDARD));
        assertTrue(captured().contains("Error: Purchase amount cannot exceed $99999.99 (current: $100000.0)"));
    }


    // dispatching, checks return vals and actions of each case

    @Test
    void dispatchAddItemReturnsTrueAndAddsItem() 
    {
        ShoppingCart cart = new ShoppingCart();
        OrderProcessor p = processorFor(cart, "IL", ShippingOption.STANDARD);
        assertTrue(controller("Pen\n1.50\n2\n").dispatch("1", cart, p));
        assertEquals(1, cart.getItemCount());
    }

    @Test
    void dispatchViewCartReturnsTrueAndPrints() 
    {
        ShoppingCart cart = cartWith(new Item("Pen", 2.50, 2));
        OrderProcessor p = processorFor(cart, "IL", ShippingOption.STANDARD);
        assertTrue(controller("").dispatch("2", cart, p));
        assertTrue(captured().contains("--- Cart Contents ---"));
    }

    @Test
    void dispatchEditQuantityReturnsTrueAndUpdates() 
    {
        ShoppingCart cart = cartWith(new Item("Apple", 2.00, 1));
        OrderProcessor p = processorFor(cart, "IL", ShippingOption.STANDARD);
        assertTrue(controller("Apple\n4\n").dispatch("3", cart, p));
        assertEquals(4, cart.getItems().get(0).getQuantity());
    }

    @Test
    void dispatchRemoveItemReturnsTrueAndRemoves() 
    {
        ShoppingCart cart = cartWith(new Item("Apple", 2.00, 1));
        OrderProcessor p = processorFor(cart, "IL", ShippingOption.STANDARD);
        assertTrue(controller("Apple\n").dispatch("4", cart, p));
        assertEquals(0, cart.getItemCount());
    }

    @Test
    void dispatchGetTotalReturnsTrueAndPrints() 
    {
        ShoppingCart cart = cartWith(new Item("Widget", 30.0, 2));
        OrderProcessor p = processorFor(cart, "IL", ShippingOption.STANDARD);
        assertTrue(controller("").dispatch("5", cart, p));
        assertTrue(captured().contains("Current total (including tax and shipping): $63.60"));
    }

    @Test
    void dispatchCheckoutReturnsFalseAndCompletes() 
    {
        ShoppingCart cart = cartWith(new Item("Widget", 30.0, 2));
        OrderProcessor p = processorFor(cart, "IL", ShippingOption.STANDARD);
        assertFalse(controller("").dispatch("6", cart, p));
        assertTrue(captured().contains("transaction completed"));
    }

    @Test
    void dispatchQuitReturnsFalseAndSaysGoodbye() 
    {
        ShoppingCart cart = new ShoppingCart();
        OrderProcessor p = processorFor(cart, "IL", ShippingOption.STANDARD);
        assertFalse(controller("").dispatch("7", cart, p));
        assertTrue(captured().contains("Goodbye!"));
    }

    @Test
    void dispatchEofSentinelReturnsFalse() 
    {
        ShoppingCart cart = new ShoppingCart();
        OrderProcessor p = processorFor(cart, "IL", ShippingOption.STANDARD);
        assertFalse(controller("").dispatch(ConsoleInput.NO_INPUT, cart, p));
    }

    @Test
    void dispatchUnknownChoiceReturnsTrueAndWarns() 
    {
        ShoppingCart cart = new ShoppingCart();
        OrderProcessor p = processorFor(cart, "IL", ShippingOption.STANDARD);
        assertTrue(controller("").dispatch("99", cart, p));
        assertTrue(captured().contains("Invalid choice. Please enter a number from 1 to 7."));
    }


}
