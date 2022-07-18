package ru.cft.stepuha.repository.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.cft.stepuha.repository.PersonRepository;
import ru.cft.stepuha.repository.model.PersonEntity;

import java.math.BigDecimal;
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
    public void insertPerson(String firstName,
                             String lastName,
                             String middleName,
                             BigDecimal money,
                             String login) {
        jdbcTemplate.update("insert into person(first_name, " +
                                    "last_name, " +
                                    "middle_name," +
                                    " money, " +
                                    "login)" +
                                    "values (?, ?, ?, ?, ?);", firstName, lastName, middleName, money, login);
    }

    @Override
    public void addMoneyToPersonById(long id, BigDecimal moneyAmount) {
        jdbcTemplate.update("update person set money = " +
                "(select money from person where id = " +
                 id + ") + ? where id = ?", moneyAmount, id);
    }

    @Override
    public void takeMoneyFromPersonById(long id, BigDecimal moneyAmount) {
        jdbcTemplate.update("update person set money = " +
                "(select money from person where id = " +
                id + ") - ? where id = ?", moneyAmount, id);
    }

    @Override
    public PersonEntity getPersonById(long personId) {
        return jdbcTemplate.query("select * from person where id = " + personId+";", rowMapper).get(0);
    }

    @Override
    public boolean personExists(long id) {
        return jdbcTemplate.query("select * from person where id = " + id, rowMapper).size() > 0;
    }
}
