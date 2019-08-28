package repository;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class RealTimeStockPrice {

    final String URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&apikey=6EFOOOGWARAQS37A";
    final HttpClient client;

    @Inject
    public RealTimeStockPrice(HttpClient client) {
        this.client = client;
    }

    String getStockRealTimePrice(String symbol) throws IOException {
        HttpGet request = new HttpGet(URL + "&" + symbol);
        HttpResponse response = client.execute(request);
        JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
        JSONObject jsonObject1 = new JSONObject(jsonObject.get("Global Quote").toString());
        System.out.println(jsonObject1.get("05. price"));
        return (String)jsonObject1.get("05. price");
    }
}
