package ru.cft.stepuha.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.cft.stepuha.repository.model.LoanAndPersonEntity;
import ru.cft.stepuha.repository.model.LoanEntity;
import ru.cft.stepuha.repository.model.PersonEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LoanAndPersonEntityRowMapper implements RowMapper<LoanAndPersonEntity> {
    @Override
    public LoanAndPersonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        PersonEntity person = new PersonEntity(rs.getLong("person_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("middle_name"),
                rs.getString("login"),
                rs.getBigDecimal("money"));

        LoanEntity loan = new LoanEntity(rs.getLong("loan_id"),
                rs.getLong("borrower_id"),
                rs.getLong("lender_id"),
                rs.getInt("state"),
                rs.getBigDecimal("money"),
                rs.getLong("creation_time"),
                rs.getLong("lending_time"),
                rs.getLong("refunding_time")
        );
        LoanAndPersonEntity result = new LoanAndPersonEntity(loan, person);
        return result;
    }
}
