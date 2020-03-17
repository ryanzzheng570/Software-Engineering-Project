package ShopifyProj.Model;

public abstract class User {
    private String userName;
    private String password;

    public User() {
    }

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
