package model;

public class User {

    private String userUUID;
    private String name = null;
    private int walletBalance = 0;

    public User(){

    }

    public User(String userUUID) {
        this.userUUID = userUUID;
    }

    public User(String userUUID, String name) {
        this.userUUID = userUUID;
        this.name = name;
    }

    public User(String userUUID, String name, int walletBalance) {
        this.userUUID = userUUID;
        this.name = name;
        this.walletBalance = walletBalance;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }
    public String getUserUUID() {
        return userUUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(int walletBalance) {
        this.walletBalance = walletBalance;
    }
}
