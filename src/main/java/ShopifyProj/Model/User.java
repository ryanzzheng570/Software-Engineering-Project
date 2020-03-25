package ShopifyProj.Model;

import javax.persistence.Id;

public abstract class User {
    private String userName;
    private String password;

    private boolean isActiveUser = false;

    public User() {
    }

    public User(String id) {
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

    public abstract String getId();
}
