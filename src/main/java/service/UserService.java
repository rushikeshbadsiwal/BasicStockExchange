package service;

import dao.UserDao;
import exceptions.LowBalanceException;
import model.User;
import model.UserStockDetail;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Singleton
public class UserService implements Service {

    private final UserDao userDao;
    private final ExecutorService executorService;
    private final TransactionTemplate transactionTemplate;

    @Inject
    public UserService(UserDao userDao, ExecutorService executorService, TransactionTemplate transactionTemplate) {
        this.userDao = userDao;
        this.executorService = executorService;
        this.transactionTemplate = transactionTemplate;
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public String addUser(User user) {
        return userDao.addUser(user);
    }

    public String deleteUser(String userUUID) {
        return userDao.deleteUser(userUUID);
    }

    public User getUser(String userUUID) {
        return userDao.getUser(userUUID);
    }

    public User getUserForUpdate(String userUUID) {
        return userDao.getUserForUpdate(userUUID);
    }

    public void makeTransaction(String userUUID, int payment, String symbol) {
        transactionTemplate.execute(transactionStatus -> {
            User user = getUserForUpdate(userUUID);
            if (user.getWalletBalance() < payment && payment > 0) {
                throw new LowBalanceException("low balance");
            }
            user.setWalletBalance(user.getWalletBalance() - payment);
            updateUser(user);
            UserStockDetail userStockDetail = userDao.getUserStockDetail(userUUID, symbol);
            if (userStockDetail == null) {
                userDao.addUserStockDetail(new UserStockDetail(userUUID, symbol, payment));
            } else {
                userStockDetail.setCount(userStockDetail.getCount() + payment);
                userDao.updateUserStockDetail(userStockDetail);
            }
            return 1;
        });
    }

    public String updateUser(User user) {
        return userDao.updateUser(user);
    }
}
