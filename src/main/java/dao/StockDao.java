package dao;

import javassist.compiler.SymbolTable;
import model.StockDetail;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class StockDao {

    private final NamedParameterJdbcTemplate template;
    private static final String ADD_STOCK_QUERY = "insert into Stockdetail (symbol, count, price) values (:symbol, :count, :price)";
    private static final String GET_STOCK_QUERY = "select * from Stockdetail where symbol=:symbol";
    private static final String GET_STOCKFORUPDATE_QUERY = "select * from Stockdetail where symbol=:symbol FOR UPDATE";
    private static final String GET_ALL_STOCK_QUERY = "select * from Stockdetail";
    private static final String UPDATE_STOCK_QUERY = "update stockdetail set count=:count,price=:price where symbol=:symbol";

    @Inject
    public StockDao(BasicDataSource basicDataSource) {
        this.template = new NamedParameterJdbcTemplate(basicDataSource);
    }

    public class StockRowMapper implements RowMapper<StockDetail> {
        public StockDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
            StockDetail stockDetail = new StockDetail();
            stockDetail.setSymbol(rs.getString("symbol"));
            stockDetail.setCount(rs.getInt("count"));
            stockDetail.setPrice(rs.getInt("price"));
            return stockDetail;
        }
    }

    public List<StockDetail> getAllStocks() {
        return template.query(GET_ALL_STOCK_QUERY, new StockRowMapper());
    }

    public String addStock(StockDetail stockDetail) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("symbol", stockDetail.getSymbol());
        map.put("count", stockDetail.getCount());
        map.put("price", stockDetail.getPrice());
        template.update(ADD_STOCK_QUERY, map);
        return null;
    }

    public StockDetail getStock(String symbol) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("symbol", symbol);
        try {
            return template.queryForObject(GET_STOCK_QUERY, namedParameters, new StockRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public StockDetail getStockForUpdate(String symbol) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("symbol", symbol);
        return template.queryForObject(GET_STOCKFORUPDATE_QUERY, namedParameters, new StockRowMapper());
    }

    public String updateStock(StockDetail stockDetail) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("symbol", stockDetail.getSymbol());
        map.put("count", stockDetail.getCount());
        map.put("price", stockDetail.getPrice());
        template.update(UPDATE_STOCK_QUERY, map);
        return null;
    }

    public String deleteStock(String symbol) {
        return null;
    }

    public String updateStockPrice(StockDetail stockDetail) {
        return null;
    }
}

