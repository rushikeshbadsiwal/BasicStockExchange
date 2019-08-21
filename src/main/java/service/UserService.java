package service;

import dao.UserDao;
import exceptions.LowBalanceException;
import model.User;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Singleton
public class UserService implements Service {

    private final UserDao userDao;
    private final List<ExecutorService> executors;
    private final TransactionTemplate transactionTemplate;

    @Inject
    public UserService(UserDao userDao, List<ExecutorService> executors, TransactionTemplate transactionTemplate) {
        this.userDao = userDao;
        this.executors = executors;
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

    public CompletableFuture<Boolean> makeTransaction(String userUUID, int payment) {
//        executors.get((userUUID.hashCode()) % 100).submit(() -> {
//            transactionTemplate.execute(transactionStatus -> {
//                User user = getUser(userUUID);
//                if (user.getWalletBalance() < payment && payment > 0) {
//                    throw new LowBalanceException("low balance");
//                }
//                user.setWalletBalance(user.getWalletBalance() - payment);
//                updateUser(user);
//                return 1;
//            });
//        });
        return null;
    }

    public String updateUser(User user) {
        return userDao.updateUser(user);
    }
}
