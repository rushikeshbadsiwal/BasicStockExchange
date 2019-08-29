package controller;

import annotation.Path;
import annotation.PrefixPath;
import com.google.gson.Gson;
import model.StockDetail;
import service.StockService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.util.List;

@Singleton
@PrefixPath(path = "/stock/")
public class StockController implements Controller {
    private final StockService stockService;

    class Temp{
        String symbol;
    }

    @Inject
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @Path(path = "/stock/getallstocks",type = "GET")
    public List<StockDetail> getAllStocks(BufferedReader requestBody) {
        return stockService.getAllStocks();
    }

    @Path(path = "/stock/getstock",type = "GET")
    public StockDetail getStock(BufferedReader requestBody) {
        StockDetail result = stockService.getStock(getSymbol(requestBody).symbol);
        System.out.println("stock price is "+result.getSymbol()+ " "+result.getPrice());
//        return stockService.getStock(getSymbol(requestBody).symbol);
        return result;
    }

    @Path(path = "/stock/addstock",type = "POST")
    public String addStock(BufferedReader requestBody) {
        return stockService.addStock(getStockDetail(requestBody));
    }

//    public String deleteStock(BufferedReader requestBody) {
//        return stockService.deleteStock(getSymbol(requestBody));
//    }

    public String updateStock(BufferedReader requestBody) {
        return stockService.updateStock(getStockDetail(requestBody));
    }

    private Temp getSymbol(BufferedReader requestBody){
        return new Gson().fromJson(requestBody, Temp.class);
    }

    private StockDetail getStockDetail(BufferedReader requestBody) {
        return new Gson().fromJson(requestBody, StockDetail.class);
    }
}
