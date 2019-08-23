package dao;

import model.User;
import model.UserStockDetail;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class UserDao {

    private final NamedParameterJdbcTemplate template;
    private static final String ADD_USER_QUERY = "insert into user (userUUID, name, walletBalance) values (:userUUID, :name, :walletBalance)";
    private static final String GET_USER_QUERY = "select * from User where userUUID=:userUUID";
    private static final String GET_ALL_USERS_QUERY = "select * from User";
    private static final String UPDATE_USER_QUERY="update stockdetail set name=:name,walletBalance=:walletBalance where userUUID=:userUUID";
    private static final String GET_USER_FORUPDATE_QUERY = "select * from User where userUUID=:userUUID FOR UPDATE";

    private static final String ADD_USERSTOCKDETAIL_QUERY = "insert into user (userUUID, symbol, count) values (:userUUID, :symbol, :count)";
    private static final String GET_USERSTOCKDETAIL_QUERY = "select * from userstockdetail where userUUID=:userUUID AND symbol=:symbol";
    private static final String GET_ALL_USERSSTOCKDETAIL_QUERY = "select * from userstockdetail";
    private static final String UPDATE_USERSTOCKDETAIL_QUERY="update stockdetail set count=:count where symbol=:symbol AND userUUID=:userUUID";

    @Inject
    public UserDao(BasicDataSource basicDataSource) {
        this.template = new NamedParameterJdbcTemplate(basicDataSource);
    }

    public class UserRowMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserUUID(rs.getString("userUUID"));
            user.setName(rs.getString("name"));
            user.setWalletBalance(rs.getInt("walletBalance"));
            return user;
        }
    }

    public class UserStockDetailRowMapper implements RowMapper<UserStockDetail> {
        public UserStockDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserStockDetail userStockDetail = new UserStockDetail();
            userStockDetail.setUserUUID(rs.getString("userUUID"));
            userStockDetail.setSymbol(rs.getString("symbol"));
            userStockDetail.setCount(rs.getInt("count"));
            return userStockDetail;
        }
    }

    public List<User> getAllUsers() {
        return template.query(GET_ALL_USERS_QUERY, new UserRowMapper());
    }

    public List<UserStockDetail> getAllUsersStockDetail() {
        return template.query(GET_ALL_USERSSTOCKDETAIL_QUERY, new UserStockDetailRowMapper());
    }

    public String addUser(User user) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userUUID", user.getUserUUID());
        map.put("name", user.getName());
        map.put("walletBalance", user.getWalletBalance());
        template.update(ADD_USER_QUERY, map);
        return null;
    }

    public String addUserStockDetail(UserStockDetail userStockDetail) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userUUID", userStockDetail.getUserUUID());
        map.put("symbol", userStockDetail.getSymbol());
        map.put("count", userStockDetail.getCount());
        template.update(ADD_USERSTOCKDETAIL_QUERY, map);
        return null;
    }

    public User getUser(String userUUID) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("userUUID", userUUID);
        return template.queryForObject(GET_USER_QUERY, namedParameters, new UserRowMapper());
    }

    public User getUserForUpdate(String userUUID) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("userUUID", userUUID);
        return template.queryForObject(GET_USER_FORUPDATE_QUERY, namedParameters, new UserRowMapper());
    }

    public UserStockDetail getUserStockDetail(String userUUID, String symbol) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("userUUID", userUUID)
                .addValue("symbol", symbol);
        return template.queryForObject(GET_USERSTOCKDETAIL_QUERY, namedParameters, new UserStockDetailRowMapper());
    }

    public String updateUser(User user) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userUUID", user.getUserUUID());
        map.put("name", user.getName());
        map.put("walletBalance", user.getWalletBalance());
        template.update(UPDATE_USER_QUERY,map);
        return null;
    }

    public String updateUserStockDetail(UserStockDetail userStockDetail) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userUUID", userStockDetail.getUserUUID());
        map.put("symbol", userStockDetail.getSymbol());
        map.put("count", userStockDetail.getCount());
        template.update(UPDATE_USERSTOCKDETAIL_QUERY,map);
        return null;
    }

    public String deleteUser(String userUUID) {
        return null;
    }

}
