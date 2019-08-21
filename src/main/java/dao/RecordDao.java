package dao;

import model.Record;
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
public class RecordDao {

    private final NamedParameterJdbcTemplate template;
    private static final String ADD_RECORD_QUERY = "insert into record (eventID, userID, symbol, status, type, amount) values (:eventID, :userID, :symbol, :status, :type, :amount)";
    private static final String GET_RECORD_QUERY = "select * from record where eventID=:eventID";
    private static final String GET_ALL_RECORD_QUERY = "select * from record";

    @Inject
    public RecordDao(BasicDataSource basicDataSource) {
        this.template = new NamedParameterJdbcTemplate(basicDataSource);
    }

    public class RecordRowMapper implements RowMapper<Record> {
        public Record mapRow(ResultSet rs, int rowNum) throws SQLException {
            Record record = new Record();
            record.setEventID(rs.getString("eventID"));
            record.setUserID(rs.getString("userID"));
            record.setSymbol(rs.getString("symbol"));
            record.setStatus(rs.getString("status"));
            record.setType(rs.getString("type"));
            record.setAmount(rs.getInt("amount"));
            return record;
        }
    }

    public List<Record> getAllRecords() {
        return template.query(GET_ALL_RECORD_QUERY,new RecordRowMapper());
    }

    public String addRecord(Record record) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("eventID",record.getEventID());
        map.put("userID",record.getUserID());
        map.put("symbol",record.getSymbol());
        map.put("status",record.getStatus());
        map.put("type",record.getType());
        map.put("amount",record.getAmount());
        template.update(ADD_RECORD_QUERY, map);
        return null;
    }

    public Record getRecord(String eventID) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("eventID", eventID);
        return template.queryForObject(GET_RECORD_QUERY, namedParameters, new RecordRowMapper());
    }
}
