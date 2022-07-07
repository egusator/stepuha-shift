package ru.cft.stepuha.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.cft.stepuha.repository.LoanRepository;
import ru.cft.stepuha.repository.model.LoanEntity;

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
    public List<LoanEntity> selectAllLoan() {
        return  jdbcTemplate.query("Select * from loan", rowMapper);
    }

}
