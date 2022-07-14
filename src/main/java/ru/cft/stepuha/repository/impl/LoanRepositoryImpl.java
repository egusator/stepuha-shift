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
        jdbcTemplate.update("insert into loan (borrower_id, " +
                        "        state, money, creation_time) values (?, 1, ?, ?);", borrowerId,
                                                                                            moneyAmount,
                                                                                            System.currentTimeMillis() / 1000);
    }

    @Override
    public List<LoanEntity> getLoansForLendingById(long lenderId) {
        return jdbcTemplate.query("select * from loan where borrower_id != "+lenderId+" and state = 1;",rowMapper);
    }

    @Override
    public List<LoanEntity> getLoansForRefundingById (long borrowerId) {
        return jdbcTemplate.query("select * from loan where borrower_id = "+borrowerId+" and state = 2;",rowMapper);

    }

    @Override
    public List<LoanEntity> getPromisedLoansByLenderId(long lenderId) {
        return jdbcTemplate.query("select * from loan where state = 2 and lender_id = " + lenderId+";",rowMapper);
    }

    @Override
    public void lendMoneyByLoanId(long loanId, long lenderId) {
        jdbcTemplate.update("update loan set lender_id = ?, state = 2, lending_time = ? where id = ?;",
                lenderId, System.currentTimeMillis() / 1000, loanId);
    }

    @Override
    public LoanEntity getLoanById(long loanId) {
        return jdbcTemplate.query("select * from loan where id = "+loanId+";",rowMapper).get(0);
    }
    @Override
    public void refundMoneyByLoanId(long loanId){
        jdbcTemplate.update("update loan set state = 3, refunding_time = ? where id = ?",
                                                System.currentTimeMillis() / 1000, loanId);
    }

    @Override
    public List<LoanEntity> getLoanRequestsByUserId(long userId) {
        return jdbcTemplate.query("select * from loan where state = 1 and borrower_id = " + userId +";", rowMapper);
    }
}
