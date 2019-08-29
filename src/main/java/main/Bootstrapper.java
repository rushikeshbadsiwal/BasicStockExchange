package main;

import Server.JettyServer;
import repository.RealTimeStockPrice;
import util.Cron;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Singleton
class Bootstrapper {

    @Inject
    private JettyServer jettyServer;
    @Inject
    private RealTimeStockPrice realTimeStockPrice;

    void start() throws Exception {
        Cron cron =new Cron(2, TimeUnit.MINUTES, () -> {
            try {
                realTimeStockPrice.loadAllStockPrices();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        cron.start();
        jettyServer.start();
    }
}
