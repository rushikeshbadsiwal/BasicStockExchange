package service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dao.StockDao;
import model.StockDetail;

import java.util.List;

@Singleton
public class StockService implements Service{

    private final StockDao stockdao;

    @Inject
    public StockService(StockDao stockdao) {
        this.stockdao = stockdao;
    }

    public List<StockDetail> getAllStocks() {
        return stockdao.getAllStocks();
    }

    public String addStock(StockDetail stockDetail) {
        return stockdao.addStock(stockDetail);
    }

    public String deleteStock(String symbol) {
        return stockdao.deleteStock(symbol);
    }

    public StockDetail getStock(String symbol) {
        return stockdao.getStock(symbol);
    }

    public String updateStock(StockDetail stockDetail) {
        return stockdao.updateStock(stockDetail);
    }

}
