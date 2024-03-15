import java.io.*;
import java.util.ArrayList;
/**
 * Javadoc
 *
 * @author Dhanush Biddala, Gurshaan Kang, Chloe Lin, Peter Yuk
 * @version 2022-11-14
 * <p>
 * Class that contains order information.
 */
public class Order {
    private String productName;
    private double price;
    private String customerName;
    private int quantityPurchased;
    private String storeName;

    public Order(String customerName, Product product, int quantityPurchased) {
        this.customerName = customerName;
        this.productName = product.getName();
        this.quantityPurchased = quantityPurchased;
        this.price = product.getPrice() * quantityPurchased;
        this.storeName = product.getStoreName();
    }

    public Order(String customerName, String storeName, String productName, int quantityPurchased, double price) {
        this.customerName = customerName;
        this.productName = productName;
        this.quantityPurchased = quantityPurchased;
        this.price = price;
        this.storeName = storeName;
    }

    public static void main(String[] args) {
        Product product1 = new Product("ToysRus", "Lego set", "StarWars lego set", 5, 3.99);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getQuantityPurchased() {
        return quantityPurchased;
    }

    public void setQuantityPurchased(int quantityPurchased) {
        this.quantityPurchased = quantityPurchased;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}