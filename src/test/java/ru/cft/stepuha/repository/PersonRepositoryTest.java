package ru.cft.stepuha.repository;


import org.junit.Assert;

import org.junit.jupiter.api.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ru.cft.stepuha.repository.impl.PersonRepositoryImpl;
import ru.cft.stepuha.repository.mapper.PersonEntityRowMapper;
import ru.cft.stepuha.repository.model.LoanEntity;
import ru.cft.stepuha.repository.model.PersonEntity;

import java.math.BigDecimal;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RowMapper<PersonEntity> rowMapper;

    @BeforeAll
    public void initTables() {
        jdbcTemplate.update("create table person\n" +
                "(\n" +
                "    id          bigserial\n" +
                "        primary key,\n" +
                "    first_name  varchar(30) not null,\n" +
                "    last_name   varchar(30) not null,\n" +
                "    middle_name varchar(30),\n" +
                "    money       numeric     not null,\n" +
                "    login       varchar(30) not null\n" +
                ");");
    }
    @BeforeEach
    public void setup() {

        jdbcTemplate.update("insert into person (first_name, last_name, middle_name, money, login) " +
                                "values ('Egor', 'Chupin', 'Vladimirovich', '1000', 'egusator');");
        jdbcTemplate.update("insert into person (first_name, last_name, middle_name, money, login) " +
                "                            values ('Andrey', 'Mustafa', 'Valerievich', '1500', 'iamdursinear');");
    }

    @Test
    public void getPersonById_ShouldReturnPersonWithTheId() {
        PersonEntity person = personRepository.getPersonById(1);
        Assert.assertEquals("egusator", person.getLogin());
    }

    @Test
    public void insertPerson_shouldInsertNewPerson() {
        personRepository.insertPerson("Grisha", "Alexeev", "Sanich",
                                                BigDecimal.valueOf(2000), "reallyrealfr");

        PersonEntity person = personRepository.getPersonById(3);
        Assert.assertEquals("reallyrealfr", person.getLogin());
    }

    @Test
    public void addMoneyToPersonById_shouldUpdateMoneyOfPersonById() {
        personRepository.addMoneyToPersonById(1, BigDecimal.valueOf(2000));
        PersonEntity person = personRepository.getPersonById(1);
        Assert.assertEquals(new BigDecimal(3000), person.getMoney());
    }

    @Test
    public void personExists_ShouldReturnTrueIfPersonFoundEitherFalse() {
        Assert.assertEquals(true,personRepository.personExists(2));
        Assert.assertEquals(false,personRepository.personExists(3));
    }
    @Test
    public void takeMoneyToPersonById_shouldUpdateMoneyOfPersonById() {
        personRepository.takeMoneyFromPersonById(1, BigDecimal.valueOf(500));
        PersonEntity person = personRepository.getPersonById(1);
        Assert.assertEquals(new BigDecimal(500), person.getMoney());
    }
    @AfterEach
    public void teardown(){
        jdbcTemplate.update("truncate table person RESTART IDENTITY;");
    }
    @AfterAll
    public void dropTables() {
        jdbcTemplate.update("drop table person");
    }
}
