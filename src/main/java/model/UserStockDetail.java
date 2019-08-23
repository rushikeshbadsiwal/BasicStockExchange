package model;

public class UserStockDetail {
    private String userUUID;
    private String symbol;
    private int count;

    public UserStockDetail(String userUUID, String symbol, int count) {
        this.userUUID = userUUID;
        this.symbol = symbol;
        this.count = count;
    }

    public UserStockDetail() {
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
