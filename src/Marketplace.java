import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Javadoc
 *
 * @author Dhanush Biddala, Gurshaan Kang, Chloe Lin, Peter Yuk
 * @version 2022-11-14
 * <p>
 * Class that allows users to interact with program.
 */

public class Marketplace extends JComponent implements Runnable {
    JButton sellerSignInButton; //Sign In -> Seller Button
    JButton customerSignInButton; //Sign In -> Customer Button
    JButton sellerCreateButton; //Create Account -> Seller Button
    JButton customerCreateButton; //Create Account -> Customer Button
    JButton sellerDeleteButton; //Delete Account -> Seller Button
    JButton customerDeleteButton; //Delete Account -> Customer Button
    JButton logInButton = new JButton("Login");
    JButton createSellerButton = new JButton("Create Account");
    JButton createCustomerButton = new JButton("Create Account");
    JButton deleteSellerButton = new JButton("Delete Account");
    JButton deleteCustomerButton = new JButton("Delete Account");
    JTextField emailTextField = new JTextField(20);
    JPasswordField passwordTextField = new JPasswordField(20);
    JTextField usernameTextField = new JTextField(20);
    Marketplace marketPlace;
    JLabel signInLabel = new JLabel("Sign in as:");
    JLabel createAccountLabel = new JLabel("Create account as:");
    JLabel deleteAccountLabel = new JLabel("Delete account as:");
    JLabel emailLabel = new JLabel("Enter e-mail: ");
    JLabel passwordLabel = new JLabel("Enter password: ");
    JLabel usernameLabel = new JLabel("Enter username: ");
    private static final String[] signInOptions = {"Sign in", "Create Account", "Delete Account"};

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sellerSignInButton) { //Log In -> Seller
                marketPlace.sellerSignIn();
            }
            if (e.getSource() == customerSignInButton) { //Log In -> Customer
                marketPlace.customerSignIn();
            }
            if (e.getSource() == logInButton) { //Log In -> Seller/Customer -> Log In Button
                marketPlace.verifyEmail(String.valueOf(emailTextField.getText()));
                if (marketPlace.verifyEmail(String.valueOf(emailTextField.getText()))) {
                    marketPlace.verifyPassword(String.valueOf(emailTextField.getText()),
                            String.valueOf(passwordTextField.getText()));
                }
            }
            if (e.getSource() == sellerCreateButton) { //Create Account -> Seller
                marketPlace.sellerCreateAccount();
            }
            if (e.getSource() == customerCreateButton) { //Create Account -> Customer
                marketPlace.customerCreateAccount();
            }
            if (e.getSource() == createSellerButton) { //Create Account -> Seller -> Create Account Button
                marketPlace.confirmEmail(String.valueOf(emailTextField.getText()));
                if (marketPlace.confirmEmail(String.valueOf(emailTextField.getText()))) {
                    marketPlace.accountCreate(String.valueOf(emailTextField.getText()),
                            String.valueOf(passwordTextField.getText()), String.valueOf(usernameTextField.getText()));
                }
            }
            if (e.getSource() == createCustomerButton) { //Create Account -> Customer -> Create Account Button
                marketPlace.verifyEmail(String.valueOf(emailTextField.getText()));
                if (marketPlace.verifyEmail(String.valueOf(emailTextField.getText()))) {
                    marketPlace.accountCreate(String.valueOf(emailTextField.getText()),
                            String.valueOf(passwordTextField.getText()), String.valueOf(usernameTextField.getText()));
                }
            }
            if (e.getSource() == sellerDeleteButton) { //Delete Account -> Seller
                marketPlace.sellerDeleteAccount();
            }
            if (e.getSource() == customerDeleteButton) { //Delete Account -> Customer
                marketPlace.customerDeleteAccount();
            }
            if (e.getSource() == deleteSellerButton) { //Delete Account -> Seller -> Delete Account Button
                marketPlace.accountDelete(String.valueOf(emailTextField.getText()));
            }
            if (e.getSource() == deleteCustomerButton) { //Delete Account -> Customer -> Delete Account Button
                marketPlace.accountDelete(String.valueOf(emailTextField.getText()));
            }
        }
    }; //End of ActionListener

    public boolean verifyEmail(String email) {
        ArrayList<String> lines = readFile();
        boolean cond = true;
        if (!(email.contains("@"))) {
            JOptionPane.showMessageDialog(null,
                    "Email must contain '@' symbol",
                    "MarketPlace-Seller", JOptionPane.ERROR_MESSAGE);
            cond = false;
        } else if ((!lines.contains(email)) || (lines.get(((lines.indexOf(email)) + 3))).contains("orders.csv")) {
            JOptionPane.showMessageDialog(null,
                    "Email not found. No seller account is linked to that email",
                    "MarketPlace-Seller", JOptionPane.ERROR_MESSAGE);
            cond = false;
        }
        return cond;
    } //Checks for any flaws in entered email (Used for Log In)

    public boolean confirmEmail(String email) {
        ArrayList<String> lines = readFile();
        boolean cond = true;
        if (!(email.contains("@"))) {
            JOptionPane.showMessageDialog(null,
                    "Email must contain '@' symbol",
                    "MarketPlace-Seller", JOptionPane.ERROR_MESSAGE);
            cond = false;
        } else if ((lines.contains(email)) || !(lines.get(((lines.indexOf(email)) + 3))).contains("orders.csv")) {
            JOptionPane.showMessageDialog(null,
                    "Email already exists!",
                    "MarketPlace-Seller", JOptionPane.ERROR_MESSAGE);
            cond = false;
        }
        return cond;
    } //Checks for any flaws in entered email (Used for Create Account)

    public void verifyPassword(String email, String password) {
        ArrayList<String> lines = readFile();
        for (int i = lines.indexOf(email); i < lines.size(); i++) {
            if (lines.get(i).equalsIgnoreCase(email)) {
                if (!password.equals(lines.get(i + 1))) {
                    JOptionPane.showMessageDialog(null,
                            "Password entered incorrect. Try again",
                            "MarketPlace-Seller", JOptionPane.ERROR_MESSAGE);
                }
                String username = lines.get(i + 2);
                ArrayList<Store> storeList;
                if (lines.get(i + 3).equalsIgnoreCase("No stores created")) {
                    storeList = new ArrayList<>();
                } else {
                    ArrayList<String> stores = new ArrayList<>();
                    storeList = new ArrayList<>();
                    int counter = i + 3;
                    boolean keepAddingStores = true;
                    do {
                        // updates stores
                        stores.add(lines.get(counter));
                        if (counter <= lines.size() - 1) {
                            keepAddingStores = false;
                        }
                    } while (keepAddingStores);

                    for (String store : stores) {
                        String[] info = store.split(", ");
                        String storeName = info[0].substring(info[0].indexOf(":") + 2);
                        String productLog = info[1];
                        String salesLog = info[2]; //ERROR
                        Store newStore = new Store(storeName, productLog, salesLog);
                        storeList.add(newStore);
                    }
                }
                Seller seller = new Seller(email, password, username, storeList);
                JOptionPane.showMessageDialog(null, "Successfully logged in!\nWelcome "
                        + seller.getUsername() + "!", "MarketPlace", JOptionPane.PLAIN_MESSAGE);
                logic(seller);
            }
        }
    } //Checks for email and password (Used for Log In) (Work in Progress)

    public void sellerSignIn() {
        JFrame frame = new JFrame("Sign In - Seller");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        marketPlace = new Marketplace();
        content.add(marketPlace, BorderLayout.CENTER);

        frame.setSize(270, 180);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        emailTextField.setText("");
        passwordTextField.setText("");
        logInButton.addActionListener(actionListener);

        JPanel panel = new JPanel();
        emailLabel.setBounds(100, 8, 70, 20);
        panel.add(emailLabel);
        emailTextField.setBounds(100, 27, 193, 28);
        panel.add(emailTextField);
        passwordLabel.setBounds(100, 55, 70, 20);
        panel.add(passwordLabel);
        passwordTextField.setBounds(100, 75, 193, 28);
        panel.add(passwordTextField);
        logInButton.setBounds(200, 150, 50, 50);
        panel.add(logInButton);
        content.add(panel, BorderLayout.CENTER);
    } //Sets up GUI for Seller -> LogIn

    public void customerSignIn() {
        JFrame frame = new JFrame("Sign In - Customer");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        marketPlace = new Marketplace();
        content.add(marketPlace, BorderLayout.CENTER);

        frame.setSize(270, 180);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        emailTextField.setText("");
        passwordTextField.setText("");
        logInButton.addActionListener(actionListener);

        JPanel panel = new JPanel();
        emailLabel.setBounds(100, 8, 70, 20);
        panel.add(emailLabel);
        emailTextField.setBounds(100, 27, 193, 28);
        panel.add(emailTextField);
        passwordLabel.setBounds(100, 55, 70, 20);
        panel.add(passwordLabel);
        passwordTextField.setBounds(100, 75, 193, 28);
        panel.add(passwordTextField);
        logInButton.setBounds(200, 150, 50, 50);
        panel.add(logInButton);
        content.add(panel, BorderLayout.CENTER);
    } //Sets up GUI for Customer -> LogIn

    public void sellerCreateAccount() {
        JFrame frame = new JFrame("Create Account - Seller");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        marketPlace = new Marketplace();
        content.add(marketPlace, BorderLayout.CENTER);

        frame.setSize(270, 180);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        emailTextField.setText("");
        passwordTextField.setText("");
        createSellerButton.addActionListener(actionListener);

        JPanel panel = new JPanel();
        emailLabel.setBounds(100, 8, 70, 20);
        panel.add(emailLabel);
        emailTextField.setBounds(100, 27, 193, 28);
        panel.add(emailTextField);
        passwordLabel.setBounds(100, 55, 70, 20);
        panel.add(passwordLabel);
        passwordTextField.setBounds(100, 75, 193, 28);
        panel.add(passwordTextField);
        createSellerButton.setBounds(200, 150, 50, 50);
        panel.add(createSellerButton);
        content.add(panel, BorderLayout.CENTER);
    } //Sets up GUI for Seller -> Create Account

    public void customerCreateAccount() {
        JFrame frame = new JFrame("Create Account - Customer");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        marketPlace = new Marketplace();
        content.add(marketPlace, BorderLayout.CENTER);

        frame.setSize(270, 180);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        emailTextField.setText("");
        passwordTextField.setText("");
        createCustomerButton.addActionListener(actionListener);

        JPanel panel = new JPanel();
        emailLabel.setBounds(100, 8, 70, 20);
        panel.add(emailLabel);
        emailTextField.setBounds(100, 27, 193, 28);
        panel.add(emailTextField);
        passwordLabel.setBounds(100, 55, 70, 20);
        panel.add(passwordLabel);
        passwordTextField.setBounds(100, 75, 193, 28);
        panel.add(passwordTextField);
        createCustomerButton.setBounds(200, 150, 50, 50);
        panel.add(createCustomerButton);
        content.add(panel, BorderLayout.CENTER);
    } //Sets up GUI for Customer -> Create Account

    public void accountCreate(String email, String password, String username) {
        ArrayList<String> lines = readFile();

        if (lines.contains(email)) {
            // checks if email already exists
            JOptionPane.showMessageDialog(null, "Account not created. " +
                    "An account is already linked to that email", "MarketPlace", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Enter password:");
        System.out.println("Enter username:");
        ArrayList<Store> stores = new ArrayList<>();
//        Seller newSeller = new Seller(email, password, username);
//        try {
//            // updates user info
//            BufferedWriter bw = new BufferedWriter(new FileWriter("userInfo.txt", true));
//            bw.write("\n" + newSeller.getEmail() + "\n");
//            bw.write(newSeller.getPassword() + "\n");
//            bw.write(newSeller.getUsername() + "\n");
//            bw.write("No stores created");
//            bw.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("Account successfully created.");
    } //Main method for creating account / writes info to userInfo.txt (Work in Progress)

    public void sellerDeleteAccount() {
        JFrame frame = new JFrame("Delete Account - Seller");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        marketPlace = new Marketplace();
        content.add(marketPlace, BorderLayout.CENTER);

        frame.setSize(270, 120);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        emailTextField.setText("");
        deleteSellerButton.addActionListener(actionListener);

        JPanel panel = new JPanel();
        emailLabel.setBounds(100, 8, 70, 20);
        panel.add(emailLabel);
        emailTextField.setBounds(100, 27, 193, 28);
        panel.add(emailTextField);
        deleteSellerButton.setBounds(200, 80, 50, 50);
        panel.add(deleteSellerButton);
        content.add(panel, BorderLayout.CENTER);
    } //Sets up GUI for Seller -> Delete Account

    public void customerDeleteAccount() {
        JFrame frame = new JFrame("Delete Account - Customer");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        marketPlace = new Marketplace();
        content.add(marketPlace, BorderLayout.CENTER);

        frame.setSize(270, 120);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        emailTextField.setText("");
        deleteCustomerButton.addActionListener(actionListener);

        JPanel panel = new JPanel();
        emailLabel.setBounds(100, 8, 70, 20);
        panel.add(emailLabel);
        emailTextField.setBounds(100, 27, 193, 28);
        panel.add(emailTextField);
        deleteCustomerButton.setBounds(200, 80, 50, 50);
        panel.add(deleteCustomerButton);
        content.add(panel, BorderLayout.CENTER);
    } //Sets up GUI for Customer -> Delete Account

    public void accountDelete(String email) {
//    } else if (choice.equals("2")) {
//                boolean missingAt;
//                String email;
//                do {
//                    missingAt = false;
//                    System.out.println("Enter email:");
//                    email = scan.nextLine();
//                    if (!(email.contains("@"))) {
//                        System.out.println("Email must contain '@' symbol.");
//                        missingAt = true;
//                    }
//                } while (missingAt);
//                if (lines.contains(email)) {
//                    System.out.println("Account not created. An account is already linked to that email.");
//                    break;
//                }
//                System.out.println("Enter password:");
//                String password = scan.nextLine();
//                System.out.println("Enter username:");
//                String username = scan.nextLine();
//                Customer newCustomer = new Customer(email, password, username);
//                try {
//                    BufferedWriter bw = new BufferedWriter(new FileWriter("userInfo.txt", true));
//                    bw.write("\n" + newCustomer.getEmail() + "\n");
//                    bw.write(newCustomer.getPassword() + "\n");
//                    bw.write(newCustomer.getUsername() + "\n");
//                    bw.write(newCustomer.getOrderLogName());
//                    bw.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                System.out.println("Account successfully created.");
//            } else {
//                System.out.println("Invalid input. Enter 1 or 2.");
//                repeatRoleSelect = true;
//            }
//        }
//        while (repeatRoleSelect);repeatRoleSelect
    } //Main method for deleting account / deletes info from userInfo.txt (Work in Progress)

    public void signIn() {
        ArrayList<String> lines = readFile();
        JFrame frame = new JFrame("MarketPlace");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        marketPlace = new Marketplace();
        content.add(marketPlace, BorderLayout.CENTER);
        sellerSignInButton = new JButton("Seller");
        sellerSignInButton.addActionListener(actionListener);
        customerSignInButton = new JButton("Customer");
        customerSignInButton.addActionListener(actionListener);

        frame.setSize(200, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JPanel panel = new JPanel();
        panel.add(signInLabel);
        content.add(panel, BorderLayout.NORTH); // End of Panel On top

        JPanel panel2 = new JPanel();
        panel2.add(sellerSignInButton);
        panel2.add(customerSignInButton);
        content.add(panel2, BorderLayout.CENTER); // End of Panel On top
    } //GUI for selecting Seller/Customer for Sign In

    public void createAccount() {
        // creates userinfo file
        String user = "/Users/peteryuk/CS180-PJ04/userInfo";
        File userInfo = new File(user);
        try {
            userInfo.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> lines = readFile();
        JFrame frame = new JFrame("MarketPlace");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        marketPlace = new Marketplace();
        content.add(marketPlace, BorderLayout.CENTER);
        sellerCreateButton = new JButton("Seller");
        sellerCreateButton.addActionListener(actionListener);
        customerCreateButton = new JButton("Customer");
        customerCreateButton.addActionListener(actionListener);

        frame.setSize(200, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JPanel panel = new JPanel();
        panel.add(createAccountLabel);
        content.add(panel, BorderLayout.NORTH); // End of Panel On top

        JPanel panel2 = new JPanel();
        panel2.add(sellerCreateButton);
        panel2.add(customerCreateButton);
        content.add(panel2, BorderLayout.CENTER); // End of Panel On top
    } //GUI for selecting Seller/Customer for Create Account

    public void deleteAccount() {
        String user = "/Users/peteryuk/CS180-PJ04/userInfo";
        File userInfo = new File(user);
        try {
            userInfo.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> lines = readFile();
        JFrame frame = new JFrame("MarketPlace");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        marketPlace = new Marketplace();
        content.add(marketPlace, BorderLayout.CENTER);
        sellerDeleteButton = new JButton("Seller");
        sellerDeleteButton.addActionListener(actionListener);
        customerDeleteButton = new JButton("Customer");
        customerDeleteButton.addActionListener(actionListener);

        frame.setSize(200, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JPanel panel = new JPanel();
        panel.add(deleteAccountLabel);
        content.add(panel, BorderLayout.NORTH); // End of Panel On top

        JPanel panel2 = new JPanel();
        panel2.add(sellerDeleteButton);
        panel2.add(customerDeleteButton);
        content.add(panel2, BorderLayout.CENTER); // End of Panel On top
//        Scanner scan = new Scanner(System.in);
//        ArrayList<String> lines = readFile();
//
//        String email;
//        System.out.println("Enter email:");
//        email = scan.nextLine();
//
//        //checks if email exists
//        if (!lines.contains(email)) {
//            System.out.println("No account is linked to that email.");
//        } else {
//            System.out.println("Enter password:");
//            String password = scan.nextLine();
//
//            for (int i = 0; i < lines.size(); i++) {
//                if (lines.get(i).contains(email)) {
//                    if (!password.equals(lines.get(i + 1))) {
//                        // incorrect password
//                        System.out.println("Account not deleted. The password entered does not match the password linked to the email.");
//                    } else {
//                        boolean foundNextEmail = false;
//                        int index = lines.indexOf(email);
//                        do {
//                            if ((lines.get(index).contains("@")) && ((lines.get(index).equals(email)))) {
//                                lines.remove(index);
//                            } else if ((lines.get(index).contains("@")) && !((lines.get(index).equals(email)))) {
//                                foundNextEmail = true;
//                            } else if (index < lines.size() - 1) {
//                                lines.remove(index);
//                            } else {
//                                lines.remove(lines.size() - 1);
//                                foundNextEmail = true;
//                            }
//                        } while (!(foundNextEmail));
//                        break;
//                    }
//                }
//            }
//
//            // updates userinfo
//            try {
//                BufferedWriter bw = new BufferedWriter(new FileWriter("userInfo.txt"));
//                for (int i = 0; i < lines.size(); i++) {
//                    bw.write(lines.get(i) + "\n");
//                }
//                bw.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//
//            System.out.println("Account successfully deleted.");
//        }
    } //GUI for selecting Seller/Customer for Delete Account

    public Marketplace() {
    }

    public void logic(Account account) {
        Scanner scan = new Scanner(System.in);
        if (account instanceof Seller) {
            boolean repeatStorePrompt = true;
            do {
                ((Seller) account).listStores(false);
                boolean repeatSellerChoices;
                System.out.println("1. Select Store\n2. Add Store\n3. Log Out");
                String choice = scan.nextLine();
                if (choice.equals("1")) {
                    boolean repeatStoreNamePrompt = true;
                    do {
                        System.out.println("Enter name of store:");
                        String storeName = scan.nextLine();
                        Store store = null;
                        for (int i = 0; i < ((Seller) account).getStores().size(); i++) {
                            if (((Seller) account).getStores().get(i).getName().equalsIgnoreCase(storeName)) {
                                store = ((Seller) account).getStores().get(i);
                            }
                        }
                        if (store != null) {
                            boolean repeatProductPrompt = true;
                            do {
                                ArrayList<Product> products = store.getProductLog();
                                System.out.println("\nProducts in " + store.getName() + ":");
                                for (int i = 0; i < products.size(); i++) {
                                    System.out.printf(products.get(i).getName() + "\n   - " + products.get(i).getDesc() + "\n   - Stock: " +
                                            products.get(i).getQuantity() + "\n   - $%.2f\n", products.get(i).getPrice());
                                }
                                System.out.println("\n1. Add Product\n2. Remove Product\n3. Edit Product" +
                                        "\n4. Import Products\n5. View store statistics\n6. Back");
                                String productAction = scan.nextLine();
                                if (productAction.equals("1")) {
                                    ((Seller) account).addProduct(storeName);
                                } else if (productAction.equals("2")) {
                                    ((Seller) account).removeProduct(storeName);
                                } else if (productAction.equals("3")) {
                                    ((Seller) account).editProduct(storeName);
                                } else if (productAction.equals("4")) {
                                    System.out.println("Enter .csv file path to import products:");
                                    String fileName = scan.nextLine();
                                    ((Seller) account).importProducts(fileName, store);
                                } else if (productAction.equals("5")) {
                                    System.out.println("-------------------------------------");
                                    ((Seller) account).viewStatistics(store);
                                    System.out.println("-------------------------------------");
                                } else if (productAction.equals("6")) {
                                    repeatProductPrompt = false;
                                } else {
                                    System.out.println("Invalid input. Enter a number between 1-6");
                                }
                                repeatStoreNamePrompt = false;
                            } while (repeatProductPrompt);
                        } else {
                            System.out.println("Store does not exist.");
                        }
                    } while (repeatStoreNamePrompt);
                } else if (choice.equals("2")) {
                    ((Seller) account).createStore();
                } else if (choice.equals("3")) {
                    System.out.println("Logging out...");
                    repeatStorePrompt = false;
                } else {
                    System.out.println("Invalid input. Enter a number from 1-3.");
                }
            } while (repeatStorePrompt);
        } else if (account instanceof Customer) {
            boolean goBack;
            do {
                goBack = false;
                System.out.println("\nWelcome " + account.getUsername() + "!\n1. View entire marketplace\n" +
                        "2. Search by store\n3. Search by item\n4. Search by description\n" +
                        "5. View Store Statistics\n6. View Recent Purchases\n7. Log out");
                String searchAction = scan.nextLine();
                if (searchAction.equals("1")) {
                    boolean repeatSort;
                    do {
                        ArrayList<Product> products = ((Customer) account).viewMarketPlace();
                        repeatSort = true;
                        int repeatSortInt = ((Customer) account).sortProducts(products);
                        repeatSort = (repeatSortInt < 5);
                        if (repeatSortInt == 6) {
                            goBack = true;
                        } else if (repeatSortInt == 0) {
                            repeatSort = false;
                            goBack = true;
                        }
                    } while (repeatSort);
                } else if (searchAction.equals("2")) {
                    boolean repeatStoreSearch;
                    ArrayList<Product> products;
                    String storeName;
                    do {
                        repeatStoreSearch = false;
                        System.out.println("Enter store name:");
                        storeName = scan.nextLine();
                        products = ((Customer) account).searchByStore(storeName);
                        if (products == null) {
                            repeatStoreSearch = true;
                        }
                    } while (repeatStoreSearch);
                    boolean repeatSort;
                    do {
                        int repeatSortInt = ((Customer) account).sortProducts(products);
                        repeatSort = (repeatSortInt < 5);
                        if (repeatSortInt == 6) {
                            goBack = true;
                        }
                    } while (repeatSort);
                } else if (searchAction.equals("3")) {
                    ArrayList<Product> products;
                    System.out.println("Enter item name:");
                    String itemName = scan.nextLine();
                    boolean repeatSort;
                    products = ((Customer) account).searchByItem(itemName);
                    do {
                        repeatSort = true;
                        int repeatSortInt = ((Customer) account).sortProducts(products);
                        repeatSort = (repeatSortInt < 5);
                        if (repeatSortInt == 6) {
                            goBack = true;
                        } else if (repeatSortInt == 0) {
                            repeatSort = false;
                            goBack = true;
                        }
                    } while (repeatSort);
                } else if (searchAction.equals("4")) {
                    ArrayList<Product> products;
                    System.out.println("Enter description:");
                    String desc = scan.nextLine();
                    boolean repeatSort;
                    products = ((Customer) account).searchByDesc(desc);
                    do {
                        repeatSort = true;
                        int repeatSortInt = ((Customer) account).sortProducts(products);
                        repeatSort = (repeatSortInt < 5);
                        if (repeatSortInt == 6) {
                            goBack = true;
                        } else if (repeatSortInt == 0) {
                            repeatSort = false;
                            goBack = true;
                        }
                    } while (repeatSort);
                } else if (searchAction.equals("5")) {
                    boolean repeatSort;
                    ((Customer) account).viewStatistics();
                    goBack = true;
                } else if (searchAction.equals("6")) {
                    ((Customer) account).viewRecentPurchases();
                    goBack = true;
                } else if (searchAction.equals("7")) {
                    System.out.println("Logging out...");
                } else {
                    System.out.println("Invalid input. Enter a number between 1-7.");
                    goBack = true;
                }
            } while (goBack);
        }
    } //Main logic for the marketplace (Work in Progress)

    public static void showMessageDialog() {
        JOptionPane.showMessageDialog(null, "Welcome to MarketPlace!",
                "MarketPlace", JOptionPane.INFORMATION_MESSAGE);
    }

    public static String showOptionDialog() {
        String signIn;
        do {
            signIn = (String) JOptionPane.showInputDialog(null, "Would you like to sign in?",
                    "MarketPlace", JOptionPane.QUESTION_MESSAGE, null, signInOptions,
                    signInOptions[0]);
            if (signIn == null) {
                JOptionPane.showMessageDialog(null, "Thank you for using MarketPlace!", "MarketPlace",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } while (signIn == null);
        return signIn;
    }

    public void run() {
        String option;
        showMessageDialog();
        option = showOptionDialog();
        if (option.equals(signInOptions[0])) { //Sign In
            signIn();
        } else if (option.equals(signInOptions[1])) { //Create Account
            createAccount();
        } else if (option.equals(signInOptions[2])) { //Delete Account
            deleteAccount();
        }
    }

    public ArrayList<String> readFile() {
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("/Users/peteryuk/CS180-PJ04/userInfo"));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    } //Read lines from userInfo to check credentials

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Marketplace());
    }
}