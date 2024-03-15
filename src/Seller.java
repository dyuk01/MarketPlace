import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Javadoc
 *
 * @author Dhanush Biddala, Gurshaan Kang, Chloe Lin, Peter Yuk
 * @version 2022-11-14
 * <p>
 * Class that contains the seller methods.
 */

public class Seller extends Account {
    public ArrayList<Store> stores;

    // new seller
    public Seller(String email, String password, String username) {
        super(email, password, username);
        stores = new ArrayList<>();
    }

    // seller with existing stores
    public Seller(String email, String password, String username, ArrayList<Store> stores) {
        super(email, password, username);
        this.stores = setStores();
    }

    // reads external file and uploads products to store
    public void importProducts(String fileName, Store store) {
        File file = new File(fileName);
        try {
            file.createNewFile();
            ArrayList<Product> products = store.readProducts(fileName);
            store.writeProducts(products);
            store.setProductLog(products);
        } catch (IOException e) {
            System.out.println("File does not exist.");
        }
    }

    // sets products for each store
    public ArrayList<Store> setStores() {
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<Store> storeList = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader("userInfo.txt"));
            String line = "";
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals(getEmail())) {
                if (lines.get(i + 3).equalsIgnoreCase("No stores created")) {
                    storeList = new ArrayList<>();
                } else {
                    ArrayList<String> stores = new ArrayList<>();
                    storeList = new ArrayList<>();
                    int counter = i + 3;
                    boolean storeBoolean = true;
                    do {
                        if (lines.get(counter).contains("Store:")) {
                            stores.add(lines.get(counter));
                        }
                        counter++;
                        if (counter < lines.size()) {
                            if ((lines.get(counter).contains("@"))) {
                                storeBoolean = false;
                            }
                        } else {
                            storeBoolean = false;
                        }
                    } while (storeBoolean);
                    for (int x = 0; x < stores.size(); x++) {
                        String[] info = stores.get(x).split(", ");
                        String storeName = info[0].substring(info[0].indexOf(":") + 2);
                        String productLog = info[1];
                        String salesLog = info[2];
                        Store newStore = new Store(storeName, productLog, salesLog);
                        storeList.add(newStore);
                    }
                }
            }
        }
        return storeList;
    }

    // displays stores
    public void listStores(boolean num) {
        if (num) {
            ArrayList<Store> stores = setStores();
            if (stores.size() == 0) {
                System.out.println("No current stores.");
            } else {
                int counter = 1;
                for (int i = 0; i < stores.size(); i++) {
                    System.out.println(counter + ": " + stores.get(i).getName());
                }
            }
        } else {
            System.out.println(getUsername() + "'s stores:");
            ArrayList<Store> stores = setStores();
            if (stores.size() == 0) {
                System.out.println("No current stores.\n");
            } else {
                for (int i = 0; i < stores.size(); i++) {
                    System.out.println(stores.get(i).getName());
                }
                System.out.println(" ");
            }
        }
    }

    // allows user to add product
    public void addProduct(String storeName) {
        Store storeToAdd = null;
        for (int i = 0; i < stores.size(); i++) {
            if (stores.get(i).getName().toLowerCase().equals(storeName.toLowerCase())) {
                storeToAdd = stores.get(i);

            }
        }
        Scanner scan = new Scanner(System.in);
        ArrayList<Product> products = storeToAdd.getProductLog();
        System.out.println("Enter product name:");
        String name = scan.nextLine();
        System.out.println("Enter product description:");
        String desc = scan.nextLine();
        int quantity = 0;
        boolean quantInt = false;
        do {
            System.out.println("Enter product quantity:");
            if (scan.hasNextInt()) {
                quantity = scan.nextInt();
                scan.nextLine();
                quantInt = true;
            } else {
                System.out.println("Invalid input. Enter an integer.");
                scan.next();
            }
        } while (!quantInt);
        double price = 0;
        boolean priceDouble = false;
        do {
            System.out.println("Enter product price:");
            if (scan.hasNextDouble()) {
                price = scan.nextDouble();
                scan.nextLine();
                priceDouble = true;
            } else {
                System.out.println("Invalid input. Enter a double.");
                scan.next();
            }
        } while (!priceDouble);

        Product product = new Product(storeToAdd.getName(), name, desc, quantity, price);
        products.add(product);

        storeToAdd.writeProducts(products);
    }

    // allows user to remove product from store
    public void removeProduct(String storeName) {
        Store storeToRemove = null;
        for (int i = 0; i < stores.size(); i++) {
            if (stores.get(i).getName().equals(storeName)) {
                storeToRemove = stores.get(i);
            }
        }
        Scanner scan = new Scanner(System.in);
        ArrayList<Product> products = storeToRemove.getProductLog();
        if (products.size() == 0) {
            System.out.println("No products to remove.");
        } else {
            boolean repeat = true;
            int productRemove;
            do {
                int counter = 0;
                System.out.println("Select product you want to remove:");
                for (int i = 0; i < products.size(); i++) {
                    counter++;
                    System.out.println(counter + ": " + products.get(i).getName());
                }
                productRemove = scan.nextInt();
                scan.nextLine();

                if (productRemove > counter) {
                    System.out.println("Invalid input, please enter the number that corresponds " +
                            "with the product you want to remove.\n");
                } else {
                    repeat = false;
                }
            } while (repeat);

            products.remove(productRemove - 1);
            storeToRemove.writeProducts(products);
        }
    }

    // allows user to edit products
    public void editProduct(String storeName) {
        Scanner scan = new Scanner(System.in);
        Store storeToEdit = null;
        for (int i = 0; i < stores.size(); i++) {
            if (stores.get(i).getName().equals(storeName)) {
                storeToEdit = stores.get(i);
            }
        }

        ArrayList<Product> products = storeToEdit.getProductLog();

        int counter = 0;
        System.out.println("Select Product to edit:");
        for (int i = 0; i < products.size(); i++) {
            counter++;
            System.out.println(counter + ": " + products.get(i).getName());
        }

        int productEdit = scan.nextInt();
        scan.nextLine();

        Product productToEdit = products.get(productEdit - 1);
        System.out.println("Enter new product name or press enter to keep the previous name:");
        String newName = scan.nextLine();
        if (!(newName.equals(""))) {
            productToEdit.setName(newName);
        }
        System.out.println("Enter new product description or press enter to keep the previous description:");
        String newDesc = scan.nextLine();
        if (!(newDesc.equals(""))) {
            productToEdit.setDesc(newDesc);
        }
        boolean quantInt = false;
        int newQuantity;
        do {
            System.out.println("Enter new product quantity or enter 0 to keep the previous quantity:");
            if (scan.hasNextInt()) {
                newQuantity = scan.nextInt();
                scan.nextLine();
                if (!(newQuantity == 0)) {
                    productToEdit.setQuantity(newQuantity);
                }
                quantInt = true;
            } else {
                System.out.println("Invalid input. Enter an integer.");
                scan.next();
            }
        } while (!quantInt);
        double newPrice;
        boolean priceDouble = false;
        do {
            System.out.println("Enter new product price or enter 0 to keep the previous price:");
            if (scan.hasNextDouble()) {
                newPrice = scan.nextDouble();
                scan.nextLine();
                if (!(newPrice == 0)) {
                    productToEdit.setPrice(newPrice);
                }
                priceDouble = true;
            } else {
                System.out.println("Invalid input. Enter a double.");
                scan.next();
            }
        } while (!priceDouble);

        products.set(productEdit - 1, productToEdit);
        storeToEdit.writeProducts(products);
    }

    // allows user to create store
    public void createStore() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter name of store:");
        String storeName = scan.nextLine();
        String productFilename = storeName.replaceAll("\\s", "");
        productFilename = productFilename.toLowerCase();
        productFilename = productFilename + "items.csv";
        File productFile = new File(productFilename);
        try {
            productFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String salesFilename = storeName.replaceAll("\\s", "");
        salesFilename = salesFilename.toLowerCase();
        salesFilename = salesFilename + "sales.csv";
        File salesFile = new File(salesFilename);
        try {
            salesFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Store newStore = new Store(storeName, productFilename, salesFilename);
        stores.add(newStore);

        updateUserInfo();
    }

    // shows amount of purchases each customer made by store, and amount of items sold by store
    public void viewStatistics(Store store) {
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> customers = new ArrayList<>();
        ArrayList<String> items = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(store.getSalesFileName()));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] info = line.split(", ");
                String customerName = info[0].substring(info[0].indexOf(":") + 2);
                String item = info[1];
                lines.add(line);
                if (!(customers.contains(customerName))) {
                    customers.add(customerName);
                }
                if (!(items.contains(item))) {
                    items.add(item);
                }
            }
            br.close();

            System.out.println("Store Statistics:");
            System.out.println("Customers - ");
            for (int i = 0; i < customers.size(); i++) {
                int counter = 0;
                for (int j = 0; j < lines.size(); j++) {
                    if (lines.get(j).contains(customers.get(i))) {
                        counter++;
                    }
                }
                System.out.println("    - " + customers.get(i) + ": " + counter + " purchases");
            }
            System.out.println(" ");
            System.out.println("Items - ");
            for (int i = 0; i < items.size(); i++) {
                int counter = 0;
                for (int j = 0; j < lines.size(); j++) {
                    if (lines.get(j).contains(items.get(i))) {
                        counter++;
                    }
                }
                System.out.println("    - " + items.get(i) + ": " + counter + " purchases");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // updates userinfo
    public void updateUserInfo() {
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("userInfo.txt"));
            String line = "";
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals(getEmail())) {
                for (int x = 0; x < getStores().size(); x++) {
                    if (lines.get(i + 3).equalsIgnoreCase("No stores created")) {
                        lines.remove(i + 3);
                    }
                    lines.add(i + 3, "Store: " + getStores().get(x).getName() + ", " + getStores().get(x).getProductFilename() +
                            ", " + getStores().get(x).getSalesFileName());
                }
            }
        }

        ArrayList<String> filtered = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("Store:")) {
                if (!(filtered.contains(lines.get(i)))) {
                    filtered.add(lines.get(i));
                }
            } else {
                filtered.add(lines.get(i));
            }
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("userInfo.txt"));
            for (int i = 0; i < filtered.size(); i++) {
                bw.write(filtered.get(i) + "\n");
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Store> getStores() {
        return stores;
    }
}