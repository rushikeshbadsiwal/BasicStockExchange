package service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dao.StockDao;
import model.StockDetail;
import util.CustomizedCache;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Singleton
public class StockService implements Service{

    private final StockDao stockdao;
    private final CustomizedCache<String, StockDetail> cache;

    @Inject
    public StockService(StockDao stockdao) {
        this.stockdao = stockdao;
        this.cache = new CustomizedCache<>(10, 5, TimeUnit.MINUTES, Runtime.getRuntime().availableProcessors(), stockdao::getStock);
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
        return cache.getValue(symbol);
    }

    public String updateStock(StockDetail stockDetail) {
        return stockdao.updateStock(stockDetail);
    }

}
