package ru.cft.stepuha.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.cft.stepuha.repository.LoanAndPersonRepository;
import ru.cft.stepuha.repository.model.LoanAndPersonEntity;

import java.util.List;

@Repository
public class LoanAndPersonRepositoryImpl implements LoanAndPersonRepository {

    private final JdbcTemplate jdbcTemplate;
    //TODO unit testing
    private final RowMapper<LoanAndPersonEntity> rowMapper;
    @Autowired
    public LoanAndPersonRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<LoanAndPersonEntity> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<LoanAndPersonEntity> getLoansForLendingById(long lenderId) {
        final String sql = "select *, loan.id as \"loan_id\", person.id as \"person_id\" from loan left" +
                " outer join person on loan.borrower_id = person.id where state = 1 and borrower_id != ? order by creation_time;";

        return jdbcTemplate.query(sql, preparedStatement -> preparedStatement.setLong(1, lenderId), rowMapper);
    }
}
