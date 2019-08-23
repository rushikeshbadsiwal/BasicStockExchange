package service;

import dao.RecordDao;
import dao.StockDao;
import exceptions.InsufficientStock;
import model.BuyStockRequest;
import model.StockDetail;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

@Singleton
public class BuyStockService implements Service {

    private final StockDao stockDao;
    private final UserService userService;
    private final RecordDao recordDao;
    private final ExecutorService executorService;
    private final TransactionTemplate transactionTemplate;

    @Inject
    public BuyStockService(StockDao stockDao, UserService userService, RecordDao recordDao, ExecutorService executorService, TransactionTemplate transactionTemplate) {
        this.stockDao = stockDao;
        this.userService = userService;
        this.recordDao = recordDao;
        this.executorService = executorService;
        this.transactionTemplate = transactionTemplate;
    }

    public void buyStock(BuyStockRequest buyStockRequest) {
        executorService.execute(() -> {
            transactionTemplate.execute(transactionStatus -> {
                userService.makeTransaction(buyStockRequest.getUserID(), buyStockRequest.getAmount(), buyStockRequest.getSymbol());
                StockDetail stockDetail =  stockDao.getStockForUpdate(buyStockRequest.getSymbol());
                if(stockDetail.getCount() < buyStockRequest.getAmount()){
                    throw new InsufficientStock("less stock");
                }
                stockDetail.setCount(stockDetail.getCount() - buyStockRequest.getAmount());
                return 1;
            });
        });
    }
}
