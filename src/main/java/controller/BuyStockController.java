package controller;

import annotation.Path;
import annotation.PrefixPath;
import com.google.gson.Gson;
import model.BuyStockRequest;
import service.BuyStockService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;

@Singleton
@PrefixPath(path = "/buystock/")
public class BuyStockController implements Controller{

    private final BuyStockService buyStockService;

    @Inject
    public BuyStockController(BuyStockService buyStockService) {
        this.buyStockService = buyStockService;
    }

    @Path(path = "/buystock/", type = "POST")
    public void buyStock(BufferedReader requestBody){
        buyStockService.buyStock(getBuyStockRequest(requestBody));
    }

    private BuyStockRequest getBuyStockRequest(BufferedReader requestBody){
        return new Gson().fromJson(requestBody, BuyStockRequest.class);
    }
}
