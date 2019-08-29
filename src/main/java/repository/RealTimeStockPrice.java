package repository;

import dao.ListedStockDao;
import dao.StockDao;
import model.ListedStock;
import model.StockDetail;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;

@Singleton
public class RealTimeStockPrice {

    private final String URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&apikey=6EFOOOGWARAQS37A&symbol=";
    private final String API_KEY = "6EFOOOGWARAQS37A";
    private final HttpClient client;
    private final StockDao stockDao;
    private final ListedStockDao listedStockDao;

    @Inject
    public RealTimeStockPrice(HttpClient client, StockDao stockDao, ListedStockDao listedStockDao) {
        this.client = client;
        this.stockDao = stockDao;
        this.listedStockDao = listedStockDao;
    }

    public void loadAllStockPrices() throws IOException {
        System.out.println("loading real time stock data");
        List<ListedStock> listedStocks = listedStockDao.getAllListedStocks();
        for (ListedStock listedStock : listedStocks) {
            StockDetail stock = stockDao.getStock(listedStock.getSymbol());
            if (stock == null) {
                stockDao.addStock(getStockRealTimePrice(listedStock.getSymbol()));
            } else {
                stockDao.updateStock(getStockRealTimePrice(listedStock.getSymbol()));
            }
        }
    }

    private StockDetail getStockRealTimePrice(String symbol) throws IOException {
        HttpGet request = new HttpGet(URL + symbol);
        HttpResponse response = client.execute(request);
        JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
        System.out.println("response from real time stock data api is "+jsonObject);
        JSONObject jsonObject1 = new JSONObject(jsonObject.get("Global Quote").toString());
        return new StockDetail(symbol, stringToInt((String) jsonObject1.get("06. volume")), (int)(stringTodouble((String) jsonObject1.get("05. price"))));
    }

    private int stringToInt(String value) {
        return Integer.parseInt(value);
    }
    private double stringTodouble(String value){
        return Double.parseDouble(value);
    }
}
