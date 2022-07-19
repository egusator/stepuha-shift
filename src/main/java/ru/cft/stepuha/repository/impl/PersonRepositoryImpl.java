package ru.cft.stepuha.repository.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.cft.stepuha.repository.PersonRepository;
import ru.cft.stepuha.repository.model.PersonEntity;
import java.math.BigDecimal;


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
        final String sql = "insert into person(first_name, last_name, middle_name, money, login) values (?, ?, ?, ?, ?);";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, middleName);
            preparedStatement.setBigDecimal(4,  money);
            preparedStatement.setString(5, login);
        });
    }


    @Override
    public void addMoneyToPersonById(long id, BigDecimal moneyAmount) {
        final String sql ="update person set money = ((select money from person where id = ?) + ?) where id = ?;";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setLong(1, id);
            preparedStatement.setBigDecimal(2, moneyAmount);
            preparedStatement.setLong(3, id);
        });
    }

    @Override
    public void takeMoneyFromPersonById(long id, BigDecimal moneyAmount) {
        final String sql ="update person set money = ((select money from person where id = ?) - ?) where id = ?;";
        jdbcTemplate.update(sql, preparedStatement -> {
            preparedStatement.setLong(1, id);
            preparedStatement.setBigDecimal(2, moneyAmount);
            preparedStatement.setLong(3, id);
        });
    }

    @Override
    public PersonEntity getPersonById(long personId) {
        final String sql = "select * from person where id = ?;";
        return jdbcTemplate.query(sql, preparedStatement -> preparedStatement.setLong(1, personId), rowMapper).get(0);
    }

    @Override
    public boolean personExists(long id) {
        final String sql = "select * from person where id = ?;";
        return jdbcTemplate.query(sql, preparedStatement -> preparedStatement.setLong(1, id), rowMapper).size() > 0;
    }

    @Override
    public boolean loginIsUsed(String login) {
        final String sql = "select * from person where login = ?";
        return jdbcTemplate.query(sql, preparedStatement -> {
            preparedStatement.setString(1, login);
        },rowMapper).size() > 0;
    }
}
