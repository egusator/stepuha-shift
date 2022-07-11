package ru.cft.stepuha.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.cft.stepuha.repository.model.LoanEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class LoanEntityRowMapper  implements RowMapper<LoanEntity> {
    @Override
    public LoanEntity mapRow (ResultSet rs, int rowNum) throws SQLException {

        LoanEntity result = new LoanEntity(rs.getLong("id"),
                rs.getLong("borrower_id"),
                rs.getLong("lender_id"),
                rs.getInt("state"),
                rs.getBigDecimal("money"),
                rs.getLong("creation_time"),
                rs.getLong("lending_time"),
                rs.getLong("refunding_time")
                );
        return result;
    }
}
