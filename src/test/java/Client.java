import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class Client {
    public static void main(String []args) throws IOException {
//        String url = "http://localhost:8090/status";
//        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=5min&apikey=6EFOOOGWARAQS37A";
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=MSFt&apikey=6EFOOOGWARAQS37A";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        System.out.println(response);
        JSONObject jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
        System.out.println(jsonObject);
        System.out.println(jsonObject.get("Global Quote"));
        JSONObject jsonObject1 = new JSONObject(jsonObject.get("Global Quote").toString());
        System.out.println("yeh haio yeh "+jsonObject1);
        System.out.println(jsonObject1.get("05. price"));
        System.out.println();
        System.out.println(((Map)jsonObject.get("Global Quote")).get("05. price"));
    }
}

// 6EFOOOGWARAQS37A

// https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=5min&apikey=demo