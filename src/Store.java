import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Javadoc
 *
 * @author Dhanush Biddala, Gurshaan Kang, Chloe Lin, Peter Yuk
 * @version 2022-11-14
 * <p>
 * Class that contains store methods.
 */

public class Store {
    private String name;
    // name of store
    private String productFilename;
    // items.txt

    private ArrayList<Product> productLog = new ArrayList<>();
    // convert Strings in items.txt to products
    private String salesFileName;
    // list of sales made [name]Sales.txt <Ex: walmartSales.txt>

    private ArrayList<String> saleLog = new ArrayList<>();
    //sales stored in ArrayList as String (parsed from salesFileName)


    public Store(String name, String productFilename, String salesFileName) {
        this.name = name;
        this.productFilename = productFilename;
        this.productLog = readProducts();
        this.salesFileName = salesFileName;
        this.saleLog = saleLog;
    }

    public ArrayList<Product> readProducts() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(productFilename));
            String line = "";
            ArrayList<Product> products = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(", ");
                String productName = info[0];
                String description = info[1];
                String quantity = info[2];
                String price = info[3];

                Product product = new Product(this.name, productName, description, Integer.parseInt(quantity), Double.parseDouble(price));

                products.add(product);
            }

            br.close();

            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Product> readProducts(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line = "";
            ArrayList<Product> products = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(", ");
                String productName = info[0];
                String description = info[1];
                String quantity = info[2];
                String price = info[3];

                Product product = new Product(this.name, productName, description, Integer.parseInt(quantity), Double.parseDouble(price));

                products.add(product);
            }

            br.close();

            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Product searchForProduct(String productName) {
        ArrayList<Product> products = readProducts();
        Product product = null;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equalsIgnoreCase(productName)) {
                product = products.get(i);
            }
        }
        return product;
    }

    public void writeProducts(ArrayList<Product> products) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(productFilename));
            for (int i = 0; i < products.size(); i++) {
                bw.write(products.get(i).getName() + ", ");
                bw.write(products.get(i).getDesc() + ", ");
                bw.write(products.get(i).getQuantity() + ", ");
                bw.write(products.get(i).getPrice() + "\n");
            }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeOrders(Order order) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(salesFileName, true));
            bw.write("Order: ");
            bw.write(order.getCustomerName() + ", ");
            bw.write(order.getProductName() + ", ");
            bw.write(order.getQuantityPurchased() + ", ");
            bw.write(order.getPrice() + "\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductFilename() {
        return productFilename;
    }

    public void setProductFilename(String productFilename) {
        this.productFilename = productFilename;
    }

    public ArrayList<Product> getProductLog() {
        return productLog;
    }

    public void setProductLog(ArrayList<Product> productLog) {
        this.productLog = productLog;
    }

    public String getSalesFileName() {
        return salesFileName;
    }

    public void setSalesFileName(String salesFileName) {
        this.salesFileName = salesFileName;
    }

    public ArrayList<String> getSaleLog() {
        return saleLog;
    }

    public void setSaleLog(ArrayList<String> saleLog) {
        this.saleLog = saleLog;
    }
}