package model;

public class StockDetail {
    private String symbol;
    private int count;
    private int price;

    public StockDetail() {

    }

    public StockDetail(String symbol, int count, int price) {
        this.symbol = symbol;
        this.count = count;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getHashCode() {
        return symbol.hashCode();
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
