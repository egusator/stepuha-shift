package ru.cft.stepuha.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.cft.stepuha.repository.LoanRepository;
import ru.cft.stepuha.repository.model.LoanEntity;

import java.math.BigDecimal;

import java.util.List;
@Repository
public class LoanRepositoryImpl implements LoanRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<LoanEntity> rowMapper;
    @Autowired
    public LoanRepositoryImpl (JdbcTemplate jdbcTemplate, RowMapper<LoanEntity> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public void createLoan (long borrowerId, BigDecimal moneyAmount) {
        final String sql = "insert into loan (borrower_id, state, money, creation_time) values (?, 1, ?, ?);";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setLong(1, borrowerId);
            preparedStatement.setBigDecimal(2, moneyAmount);
            preparedStatement.setLong(3, System.currentTimeMillis()/1000);
        });
    }

    @Override
    public boolean loanExists(long loanId) {
        final String sql = "select * from loan where id = ?;";
        return jdbcTemplate.query(sql, preparedStatement -> preparedStatement.setLong(1, loanId), rowMapper).size() > 0;
    }

    @Override
    public List<LoanEntity> getLoansForLendingById(long lenderId) {
        final String sql = "select * from loan where borrower_id != ? and state = 1;";
        return jdbcTemplate.query(sql, preparedStatement -> preparedStatement.setLong(1, lenderId), rowMapper);
    }


    @Override
    public List<LoanEntity> getLoansForRefundingById (long borrowerId) {
        final String sql = "select * from loan where borrower_id = ? and state = 2;";
        return jdbcTemplate.query(sql, preparedStatement -> preparedStatement.setLong(1, borrowerId), rowMapper);

    }

    @Override
    public List<LoanEntity> getPromisedLoansByLenderId(long lenderId) {
        final String sql = "select * from loan where state = 2 and lender_id = ?;";
        return jdbcTemplate.query(sql, preparedStatement -> preparedStatement.setLong(1, lenderId), rowMapper);
    }

    @Override
    public void lendMoneyByLoanId(long loanId, long lenderId) {
        final String sql = "update loan set lender_id = ?, state = 2, lending_time = ? where id = ?;";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setLong(1, lenderId);
            preparedStatement.setLong(2, System.currentTimeMillis()/1000);
            preparedStatement.setLong(3, loanId);
        });
    }

    @Override
    public LoanEntity getLoanById(long loanId) {
        final String sql = "select * from loan where id = ?;";
        return jdbcTemplate.query(sql, preparedStatement -> preparedStatement.setLong(1, loanId), rowMapper).get(0);
    }
    @Override
    public void refundMoneyByLoanId(long loanId){
        final String sql = "update loan set state = 3, refunding_time = ? where id = ?";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setLong(1, System.currentTimeMillis()/1000);
            preparedStatement.setLong(2, loanId);
        });
    }

    @Override
    public List<LoanEntity> getLoanRequestsByUserId(long userId) {
        final String sql = "select * from loan where state = 1 and borrower_id = ?;";
        return jdbcTemplate.query(sql, preparedStatement -> preparedStatement.setLong(1, userId), rowMapper);
    }
}
