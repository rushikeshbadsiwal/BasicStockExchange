package dao;

import model.ListedStock;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Singleton
public class ListedStockDao {

    private final NamedParameterJdbcTemplate template;
    private static final String GET_ALL_LISTEDSTOCK_QUERY = "select * from listedstock";
    private static final String GET_LISTEDSTOCK_QUERY = "select * from listedstock where symbol=:symbol";

    @Inject
    public ListedStockDao(BasicDataSource basicDataSource) {
        this.template = new NamedParameterJdbcTemplate(basicDataSource);
    }

    public class ListedStockRowMapper implements RowMapper<ListedStock> {
        public ListedStock mapRow(ResultSet rs, int rowNum) throws SQLException {
            ListedStock listedStock = new ListedStock();
            listedStock.setSymbol(rs.getString("symbol"));
            listedStock.setName(rs.getString("name"));
            return listedStock;
        }
    }

    public List<ListedStock> getAllListedStocks() {
        return template.query(GET_ALL_LISTEDSTOCK_QUERY,new ListedStockRowMapper());
    }

    public ListedStock getListedStock(String symbol) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("symbol", symbol);
        return template.queryForObject(GET_LISTEDSTOCK_QUERY, namedParameters, new ListedStockRowMapper());
    }
}
