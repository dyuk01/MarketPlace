/**
 * Javadoc
 *
 * @author Dhanush Biddala, Gurshaan Kang, Chloe Lin, Peter Yuk
 * @version 2022-11-14
 * <p>
 * Parent class that contains basic account information.
 */
public class Account {
    private String email;
    private String password;
    private String username;

    public Account(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}