package service;

import dao.RecordDao;
import dao.StockDao;
import exceptions.InsufficientStock;
import model.BuyStockRequest;
import model.StockDetail;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BuyStockService implements Service {

    private final StockDao stockDao;
    private final UserService userService;
    private final RecordDao recordDao;
    private final TransactionTemplate transactionTemplate;

    @Inject
    public BuyStockService(StockDao stockDao, UserService userService, RecordDao recordDao, TransactionTemplate transactionTemplate) {
        this.stockDao = stockDao;
        this.userService = userService;
        this.recordDao = recordDao;
        this.transactionTemplate = transactionTemplate;
    }

    public void buyStock(BuyStockRequest buyStockRequest) {
        System.out.println("this is "+transactionTemplate);
        transactionTemplate.execute(transactionStatus -> {
            userService.makeTransaction(buyStockRequest.getUserID(), buyStockRequest.getAmount(), buyStockRequest.getSymbol());
            StockDetail stockDetail = stockDao.getStockForUpdate(buyStockRequest.getSymbol());
            if (stockDetail.getCount() < buyStockRequest.getAmount()) {
                throw new InsufficientStock("less stock");
            }
            stockDetail.setCount(stockDetail.getCount() - buyStockRequest.getAmount());
            stockDao.updateStock(stockDetail);
            return 1;
        });
    }
}
