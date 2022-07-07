package ru.cft.stepuha.repository.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.cft.stepuha.repository.PersonRepository;
import ru.cft.stepuha.repository.model.PersonEntity;

import java.util.List;

@Repository
public class PersonRepositoryImpl implements PersonRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PersonEntity> rowMapper;
    @Autowired
    public PersonRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<PersonEntity> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }
    @Override
    public List<PersonEntity> selectAllPerson () {
        return jdbcTemplate.query("Select * from person;", rowMapper);
    }
}
