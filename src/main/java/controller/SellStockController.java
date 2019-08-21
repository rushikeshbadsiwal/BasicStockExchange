package controller;

import annotation.Path;
import annotation.PrefixPath;
import javax.inject.Singleton;

@Singleton
@PrefixPath(path = "/sellstock/")
public class SellStockController implements Controller{

    @Path(path = "/sellstock/", type = "POST")
    public void sellStock(){

    }
}
