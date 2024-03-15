import java.io.*;
import java.util.*;

/**
 * Javadoc
 *
 * @author Dhanush Biddala, Gurshaan Kang, Chloe Lin, Peter Yuk
 * @version 2022-11-14
 * <p>
 * Class that contains the customer methods.
 */

public class Customer extends Account {
    private ArrayList<Product> orderLog = new ArrayList<>();
    private String orderLogName;
    ArrayList<Store> stores;

    public Customer(String email, String password, String username) {
        super(email, password, username);
        // creates orderlog file if not already made
        this.orderLogName = getUsername() + "orders.csv";
        File orderLogFile = new File(orderLogName);
        try {
            orderLogFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // gathers and instantiates all the stores in userinfo
        this.stores = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("userInfo.txt"));
            String line = "";
            ArrayList<Order> orders = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.contains("Store: ")) {
                    String[] info = line.split(", ");
                    String storeName = info[0].substring(info[0].indexOf(":") + 2);
                    String fileName = info[1];
                    String salesLog = info[2];
                    Store store = new Store(storeName, fileName, salesLog);
                    stores.add(store);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // statistics dashboard for customer
    public void viewStatistics() {
        ArrayList<Store> storeList = stores;
        System.out.println("Statistics:");
        for (int i = 0; i < storeList.size(); i++) {
            int counterCustomer = 0;
            int counterTotal = 0;
            try {
                BufferedReader br = new BufferedReader(new FileReader(storeList.get(i).getSalesFileName()));
                String line = "";
                while ((line = br.readLine()) != null) {
                    //check if line contains customer name
                    if (!(line.contains(getUsername()))) {
                        counterCustomer++;
                    }
                    //updates overall total
                    counterTotal++;
                }
                br.close();

                //prints number of total sales from store and number of sales user made from store
                System.out.println(storeList.get(i).getName() + ":");
                System.out.println("    - Total sales: " + counterTotal);
                System.out.println("    - My purchases: " + counterCustomer);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //displays recent purchases
    public void viewRecentPurchases() {
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(orderLogName));
            String line = "";
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Recent Orders");
        for (int i = 0; i < lines.size(); i++) {
            System.out.println(lines.get(i));
        }
    }


    // reads orderlog file
    public ArrayList<Order> readOrders() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(orderLogName));
            String line = "";
            ArrayList<Order> orders = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(", ");
                String storeName = info[0];
                storeName = storeName.substring(storeName.indexOf(":") + 2);
                System.out.println(storeName);
                String productName = info[1];
                String quantity = info[2];
                String price = info[3];

                Order order = new Order(getUsername(), storeName, productName, Integer.parseInt(quantity), Double.parseDouble(price));

                orders.add(order);
            }
            br.close();
            return orders;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // updates orderlog file
    public void writeOrders(Order order) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(orderLogName, true));
            bw.write("Order: ");
            bw.write(order.getStoreName() + ", ");
            bw.write(order.getProductName() + ", ");
            bw.write(order.getQuantityPurchased() + ", ");
            bw.write(order.getPrice() + "\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // displays entire marketplace
    public ArrayList<Product> viewMarketPlace() {
        ArrayList<Store> stores = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("userInfo.txt"));
            String line = "";
            ArrayList<String> storeStrings = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.contains("Store: ")) {
                    storeStrings.add(line);
                    String[] info = line.split(", ");
                    String storeName = info[0].substring(info[0].indexOf(":") + 2);
                    String productLog = info[1];
                    String salesLog = info[2];
                    Store newStore = new Store(storeName, productLog, salesLog);
                    stores.add(newStore);
                }
            }

            br.close();

            ArrayList<Store> filtered = new ArrayList<>();

            for (int i = 0; i < stores.size(); i++) {
                if (!filtered.contains(stores.get(i))) {
                    filtered.add(stores.get(i));
                }
            }

            for (int i = 0; i < filtered.size(); i++) {
                viewProducts(filtered.get(i));
            }

            ArrayList<Product> products = new ArrayList<>();

            for (int i = 0; i < filtered.size(); i++) {
                for (int j = 0; j < filtered.get(i).getProductLog().size(); j++) {
                    products.add(filtered.get(i).getProductLog().get(j));
                }
            }

            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // displays product from specific store
    public ArrayList<Product> searchByStore(String storeName) {
        ArrayList<Store> stores = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("userInfo.txt"));
            String line = "";
            ArrayList<String> storeStrings = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.contains("Store: ")) {
                    storeStrings.add(line);
                    String[] info = line.split(", ");
                    String store = info[0].substring(info[0].indexOf(":") + 2);
                    String productLog = info[1];
                    String salesLog = info[2];
                    Store newStore = new Store(store, productLog, salesLog);
                    stores.add(newStore);
                }
            }

            br.close();

            ArrayList<Store> filtered = new ArrayList<>();

            for (int i = 0; i < stores.size(); i++) {
                if (stores.get(i).getName().toLowerCase().equalsIgnoreCase(storeName.toLowerCase())) {
                    filtered.add(stores.get(i));
                }
            }

            if (filtered.size() == 0) {
                System.out.println("No such store found");
                return null;
            } else {
                for (int i = 0; i < filtered.size(); i++) {
                    viewProducts(filtered.get(i));
                }
            }

            ArrayList<Product> products = new ArrayList<>();

            for (int i = 0; i < filtered.size(); i++) {
                for (int j = 0; j < filtered.get(i).getProductLog().size(); j++) {
                    products.add(filtered.get(i).getProductLog().get(j));
                }
            }

            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // displays products that contain search term
    public ArrayList<Product> searchByItem(String itemName) {
        ArrayList<Store> storeList = stores;
        ArrayList<Product> filtered = new ArrayList<>();

        for (int i = 0; i < storeList.size(); i++) {
            for (int j = 0; j < storeList.get(i).getProductLog().size(); j++) {
                if ((storeList.get(i).getProductLog().get(j).getName().toLowerCase().contains(itemName.toLowerCase()))
                        || (storeList.get(i).getProductLog().get(j).getName().equalsIgnoreCase(itemName))) {
                    filtered.add(storeList.get(i).getProductLog().get(j));
                }
            }
        }

        if (filtered.size() == 0) {
            System.out.println("No items found.");
            return null;
        } else {
            viewProducts(filtered);
            return filtered;
        }
    }

    //displays products that's descriptions contain search term
    public ArrayList<Product> searchByDesc(String desc) {
        ArrayList<Store> stores = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("userInfo.txt"));
            String line = "";
            ArrayList<String> storeStrings = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.contains("Store: ")) {
                    storeStrings.add(line);
                    String[] info = line.split(", ");
                    String store = info[0].substring(info[0].indexOf(":") + 2);
                    String productLog = info[1];
                    String salesLog = info[2];
                    Store newStore = new Store(store, productLog, salesLog);
                    stores.add(newStore);
                }
            }

            br.close();

            ArrayList<Product> filtered = new ArrayList<>();

            for (int i = 0; i < stores.size(); i++) {
                for (int j = 0; j < stores.get(i).getProductLog().size(); j++) {
                    if (stores.get(i).getProductLog().get(j).getDesc().toLowerCase().contains(desc.toLowerCase())) {
                        if (!(filtered.contains(stores.get(i).getProductLog().get(j))))
                            filtered.add(stores.get(i).getProductLog().get(j));
                    }
                }
            }

            if (filtered.size() == 0) {
                System.out.println("No items found.");
                return null;
            } else {
                viewProducts(filtered);
                return filtered;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // displays product page
    public boolean viewPage() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Type in product name to view its page or press enter to go back:");
        String product = scan.nextLine();
        if (!(product.equals(""))) {
            ArrayList<Product> products = productList();
            Product selection = null;
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getName().equalsIgnoreCase(product)) {
                    selection = products.get(i);
                }
            }
            if (selection != null) {
                System.out.printf(selection.getName() + "\n   - " + selection.getDesc() + "\n   - Stock: " +
                        selection.getQuantity() + "\n   - $%.2f\n", selection.getPrice());
                System.out.println("1. Purchase Product\n2. Go Back");
                boolean askAgain = false;
                do {
                    String purchaseChoice = scan.nextLine();
                    if (purchaseChoice.equals("1")) {
                        int quantity = 0;
                        boolean quantInt = false;
                        do {
                            System.out.println("Enter quantity you want to purchase:");
                            if (scan.hasNextInt()) {
                                quantity = scan.nextInt();
                                scan.nextLine();
                                quantInt = true;
                            } else {
                                System.out.println("Invalid input. Enter an integer.");
                                scan.next();
                            }
                        } while (!quantInt);
                        purchase(this, selection, quantity);
                        return true;
                    } else if (purchaseChoice.equals("2")) {
                        return false;
                    } else {
                        System.out.println("Invalid input. Please enter 1 or 2.");
                    }
                } while (askAgain);
            } else {
                System.out.println("No such product found.");
            }
        } else {
            return false;
        }
        return false;
    }

    // allows user to purchase item
    public void purchase(Customer customer, Product product, int quantity) {
        Store targetStore = null;
        Product targetProduct = null;

        for (int i = 0; i < stores.size(); i++) {
            for (int j = 0; j < stores.get(i).getProductLog().size(); j++) {
                if (stores.get(i).getProductLog().get(j).getName().equalsIgnoreCase(product.getName())) {
                    targetStore = stores.get(i);
                    targetProduct = stores.get(i).getProductLog().get(j);
                }
            }
        }

        if (quantity > targetProduct.getQuantity()) {
            System.out.println("Error, inadequate stock. Enter a number less than the specified stock.");
        } else {
            targetProduct.setQuantity(targetProduct.getQuantity() - quantity);
            Order order = new Order(customer.getUsername(), product, quantity);
            try {
                BufferedReader br = new BufferedReader(new FileReader(targetStore.getProductFilename()));
                ArrayList<String> lines = new ArrayList<>();
                String line = "";
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
                br.close();

                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains(targetProduct.getName())) {
                        String[] info = lines.get(i).split(", ");
                        info[2] = String.valueOf(targetProduct.getQuantity());
                        lines.set(i, info[0] + ", " + info[1] + ", "
                                + info[2] + ", " + info[3]);
                    }
                }

                ArrayList<Product> products = new ArrayList<>();
                for (int i = 0; i < lines.size(); i++) {
                    String[] info = lines.get(i).split(", ");
                    String productName = info[0];
                    String description = info[1];
                    String q = info[2];
                    String price = info[3];

                    Product prod = new Product(targetStore.getName(), productName, description, Integer.parseInt(q), Double.parseDouble(price));

                    products.add(prod);
                }

                targetStore.writeProducts(products);

                targetStore.writeOrders(order);
                customer.writeOrders(order);

                System.out.println("Item successfully purchased.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // displays list of products
    public ArrayList<Product> productList() {
        ArrayList<Store> stores = new ArrayList<>();
        ArrayList<Product> products = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("userInfo.txt"));
            String line = "";
            ArrayList<String> storeStrings = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.contains("Store: ")) {
                    storeStrings.add(line);
                    String[] info = line.split(", ");
                    String storeName = info[0].substring(info[0].indexOf(":") + 2);
                    String productLog = info[1];
                    String salesLog = info[2];
                    Store newStore = new Store(storeName, productLog, salesLog);
                    stores.add(newStore);
                }
            }

            for (int i = 0; i < stores.size(); i++) {
                for (int j = 0; j < stores.get(i).getProductLog().size(); j++) {
                    products.add(stores.get(i).getProductLog().get(j));
                }
            }

            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // displays aggregated list of products that fit criteria from every store
    public void viewProducts(ArrayList<Product> filtered) {
        ArrayList<Product> products = filtered;
        System.out.println("---------------------------------------------------------");
        for (int i = 0; i < products.size(); i++) {
            System.out.printf(products.get(i).getName() + "\n   - Sold by " + products.get(i).getStoreName() + "\n   - $%.2f \n", products.get(i).getPrice());
        }
        System.out.println("---------------------------------------------------------");
    }

    // displays aggregated list of products that fit criteria from one store
    public void viewProducts(Store store) {
        ArrayList<Product> products = store.readProducts();
        System.out.println("---------------------------------------------------------");
        for (int i = 0; i < products.size(); i++) {
            System.out.printf(products.get(i).getName() + "\n   - Sold by " + products.get(i).getStoreName() + "\n   - $%.2f \n", products.get(i).getPrice());
        }
        System.out.println("---------------------------------------------------------");
    }

    // allows to sort arraylists
    public Comparator<Product> priceLowToHigh = new Comparator<Product>() {
        public int compare(Product p1, Product p2) {
            double p1Price = p1.getPrice();
            double p2Price = p2.getPrice();
            return (int) (p1Price - p2Price);
        }
    };
    public Comparator<Product> priceHighToLow = new Comparator<Product>() {
        public int compare(Product p1, Product p2) {
            double p1Price = p1.getPrice();
            double p2Price = p2.getPrice();
            return (int) (p2Price - p1Price);
        }
    };
    public Comparator<Product> quantityLowToHigh = new Comparator<Product>() {
        public int compare(Product p1, Product p2) {
            int p1Quant = p1.getQuantity();
            int p2Quant = p2.getQuantity();
            return (p1Quant - p2Quant);
        }
    };
    public Comparator<Product> quantityHighToLow = new Comparator<Product>() {
        public int compare(Product p1, Product p2) {
            int p1Quant = p1.getQuantity();
            int p2Quant = p2.getQuantity();
            return (p2Quant - p1Quant);
        }
    };

    // allows users to sort items or view product page
    public int sortProducts(ArrayList<Product> products) {
        Scanner scan = new Scanner(System.in);
        if ((products != null)) {
            if (products.size() != 0) {
                System.out.println("1. Sort by price (Low to High)\n2. Sort by price (High to Low)\n3. Sort by stock (Low to High)" +
                        "\n4. Sort by stock (High to Low)\n5. View product page\n6. Back");
                boolean repeatSortChoice;
                do {
                    repeatSortChoice = false;
                    String sortChoice = scan.nextLine();
                    if (sortChoice.equals("1")) {
                        Collections.sort(products, priceLowToHigh);
                        viewProducts(products);
                        return 1;
                    } else if (sortChoice.equals("2")) {
                        Collections.sort(products, priceHighToLow);
                        viewProducts(products);
                        return 2;
                    } else if (sortChoice.equals("3")) {
                        Collections.sort(products, quantityLowToHigh);
                        viewProducts(products);
                        return 3;
                    } else if (sortChoice.equals("4")) {
                        Collections.sort(products, quantityHighToLow);
                        viewProducts(products);
                        return 4;
                    } else if (sortChoice.equals("5")) {
                        boolean view = viewPage();
                        if (view) {
                            return 5;
                        } else {
                            return 0;
                        }
                    } else if (sortChoice.equals("6")) {
                        return 6;
                    } else {
                        System.out.println("Invalid input. Please enter a number 1-6.");
                        repeatSortChoice = true;
                    }
                } while (repeatSortChoice);
            }
        } else {
            return -1;
        }
        return 999;
    }

    public ArrayList<Product> getOrderLog() {
        return orderLog;
    }

    public void setOrderLog(ArrayList<Product> orderLog) {
        this.orderLog = orderLog;
    }

    public String getOrderLogName() {
        return orderLogName;
    }

    public void setOrderLogName(String orderLogName) {
        this.orderLogName = orderLogName;
    }
}