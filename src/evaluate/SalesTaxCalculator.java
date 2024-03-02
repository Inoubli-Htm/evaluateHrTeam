package evaluate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

class Item {
    private String name;
    private BigDecimal price;
    private boolean isImported;
    private boolean isExempt;

    public Item(String name, BigDecimal price, boolean isImported, boolean isExempt) {
        this.name = name;
        this.price = price;
        this.isImported = isImported;
        this.isExempt = isExempt;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isImported() {
        return isImported;
    }

    public boolean isExempt() {
        return isExempt;
    }
}

class ShoppingBasket {
    private List<Item> items;

    public ShoppingBasket() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }
}

public class SalesTaxCalculator {
    private static final BigDecimal BASIC_TAX_RATE = new BigDecimal("0.10");
    private static final BigDecimal IMPORT_TAX_RATE = new BigDecimal("0.05");
    private static final BigDecimal ROUNDING_FACTOR = new BigDecimal("0.05");

    public static void main(String[] args) {
        // Input
        ShoppingBasket basket1 = new ShoppingBasket();
        basket1.addItem(new Item("book", new BigDecimal("12.49"), false, true));
        basket1.addItem(new Item("music CD", new BigDecimal("14.99"), false, false));
        basket1.addItem(new Item("chocolate bar", new BigDecimal("0.85"), false, true));

        ShoppingBasket basket2 = new ShoppingBasket();
        basket2.addItem(new Item("imported box of chocolates", new BigDecimal("10.00"), true, true));
        basket2.addItem(new Item("imported bottle of perfume", new BigDecimal("47.50"), true, false));

        ShoppingBasket basket3 = new ShoppingBasket();
        basket3.addItem(new Item("imported bottle of perfume", new BigDecimal("27.99"), true, false));
        basket3.addItem(new Item("bottle of perfume", new BigDecimal("18.99"), false, false));
        basket3.addItem(new Item("packet of headache pills", new BigDecimal("9.75"), false, true));
        basket3.addItem(new Item("box of imported chocolates", new BigDecimal("11.25"), true, true));

        // Output
        generateReceipt(basket1);
        generateReceipt(basket2);
        generateReceipt(basket3);
    }

    private static void generateReceipt(ShoppingBasket basket) {
        BigDecimal totalSalesTax = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;

        System.out.println("Output:");
        for (Item item : basket.getItems()) {
            BigDecimal salesTax = calculateSalesTax(item);
            totalSalesTax = totalSalesTax.add(salesTax);
            BigDecimal taxedPrice = item.getPrice().add(salesTax);
            totalPrice = totalPrice.add(taxedPrice);

            System.out.println("1 " + (item.isImported() ? "imported " : "") + item.getName() + ": " + taxedPrice);
        }

        System.out.println("Sales Taxes: " + roundSalesTax(totalSalesTax) + " Total: " + roundTotalPrice(totalPrice));
    }

    private static BigDecimal calculateSalesTax(Item item) {
        BigDecimal salesTax = BigDecimal.ZERO;
        if (!item.isExempt()) {
            salesTax = salesTax.add(item.getPrice().multiply(BASIC_TAX_RATE));
        }
        if (item.isImported()) {
            salesTax = salesTax.add(item.getPrice().multiply(IMPORT_TAX_RATE));
        }
        return roundSalesTax(salesTax);
    }

    private static BigDecimal roundSalesTax(BigDecimal salesTax) {
        return salesTax.divide(ROUNDING_FACTOR, 0, RoundingMode.UP).multiply(ROUNDING_FACTOR);
    }

    private static BigDecimal roundTotalPrice(BigDecimal totalPrice) {
        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }
}

