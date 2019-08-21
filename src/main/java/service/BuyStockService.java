package service;

import dao.RecordDao;
import dao.StockDao;
import model.BuyStockRequest;
import model.StockDetail;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Singleton
public class BuyStockService implements Service {

    private final StockDao stockDao;
    private final UserService userService;
    private final RecordDao recordDao;
    private final List<ExecutorService> executors;
    private final TransactionTemplate transactionTemplate;

    @Inject
    public BuyStockService(StockDao stockDao, UserService userService, RecordDao recordDao, List<ExecutorService> executors, TransactionTemplate transactionTemplate) {
        this.stockDao = stockDao;
        this.userService = userService;
        this.recordDao = recordDao;
        this.executors = executors;
        this.transactionTemplate = transactionTemplate;
    }

    public void buyStock(BuyStockRequest buyStockRequest) {
        executors.get(buyStockRequest.getSymbol().hashCode()).execute(() -> {
            transactionTemplate.execute(transactionStatus -> {
                StockDetail stock = stockDao.getStock(buyStockRequest.getSymbol());
                if(stock.getCount()< buyStockRequest.getAmount()){
                    throw null;
                }
                userService.makeTransaction(buyStockRequest.getUserID(), buyStockRequest.getAmount());
                return 1;
            });
        });
    }
}
