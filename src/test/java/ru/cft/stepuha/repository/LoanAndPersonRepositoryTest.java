package ru.cft.stepuha.repository;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.cft.stepuha.repository.model.LoanAndPersonEntity;
import ru.cft.stepuha.repository.model.LoanEntity;

import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoanAndPersonRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LoanAndPersonRepository loanAndPersonRepository;
    @Autowired
    private RowMapper<LoanAndPersonEntity> rowMapper;
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

        jdbcTemplate.update("create table loan\n" +
                "(\n" +
                "    id             bigserial\n" +
                "        primary key,\n" +
                "    borrower_id    bigint  not null,\n" +


                "    lender_id      bigint,\n" +

                "    state          integer not null,\n" +
                "    money          numeric not null,\n" +
                "    creation_time  bigint  not null,\n" +
                "    lending_time   bigint,\n" +
                "    refunding_time bigint\n" +
                ");");
    }
    @BeforeEach
    public void setup() {
        jdbcTemplate.update("alter table loan add constraint fk_borrower foreign key(borrower_id) references person(id);");
        jdbcTemplate.update("alter table loan add constraint fk_lender foreign key(lender_id) references person(id);");

        jdbcTemplate.update("insert into person (first_name, last_name, middle_name, money, login) " +
                "values ('Egor', 'Chupin', 'Vladimirovich', '1000', 'egusator');");
        jdbcTemplate.update("insert into person (first_name, last_name, middle_name, money, login) " +
                "                            values ('Andrey', 'Mustafa', 'Valerievich', '1500', 'iamdursinear');");
        jdbcTemplate.update("insert into person (first_name, last_name, middle_name, money, login) " +
                "                            values ('Grisha', 'Alexeev', 'Alexeevich', '2000', 'reallyrealfr');");

        jdbcTemplate.update("insert into loan (borrower_id, state, money, creation_time) values " +
                "(1, 1, 400, ?)", System.currentTimeMillis()/1000);
        jdbcTemplate.update("insert into loan (borrower_id, state, money, creation_time) values " +
                "(2, 1, 500, ?)", System.currentTimeMillis()/1000);
        jdbcTemplate.update("insert into loan (borrower_id, state, money, creation_time) values " +
                "(3, 1, 600, ?)", System.currentTimeMillis()/1000);

        jdbcTemplate.update("insert into loan (borrower_id, lender_id, state, money, creation_time, lending_time) values " +
                "(2, 3, 2, 300, ?, ?)", System.currentTimeMillis()/1000 - 600, System.currentTimeMillis()/1000 );
        jdbcTemplate.update("insert into loan (borrower_id, lender_id, state, money, creation_time, lending_time) values " +
                "(2, 1, 2, 200, ?, ?)", System.currentTimeMillis()/1000 - 600, System.currentTimeMillis()/1000 );
        jdbcTemplate.update("insert into loan (borrower_id, lender_id, state, money, creation_time, lending_time) values " +
                "(3, 1, 2, 250, ?, ?)", System.currentTimeMillis()/1000 - 600, System.currentTimeMillis()/1000 );
    }

    @Test
    public void getLoansForLendingById_ShouldReturnListOfLoansFreeForLendingAndBorrowers() {
        List<LoanAndPersonEntity> loanAndPersonEntityList = loanAndPersonRepository.getLoansForLendingById(1);
        Assert.assertEquals(2, loanAndPersonEntityList.size());
        Assert.assertEquals(2, loanAndPersonEntityList.get(0).getLoan().getId());
        Assert.assertEquals(2, loanAndPersonEntityList.get(0).getPerson().getId());
        Assert.assertEquals(3, loanAndPersonEntityList.get(1).getLoan().getId());
        Assert.assertEquals(3, loanAndPersonEntityList.get(1).getPerson().getId());
    }
    @AfterEach
    public void teardown(){
        jdbcTemplate.update("alter table loan drop constraint fk_borrower;");
        jdbcTemplate.update("alter table loan drop constraint fk_lender;");
        jdbcTemplate.update("truncate table loan RESTART IDENTITY;");
        jdbcTemplate.update("truncate table person RESTART IDENTITY;");
    }

    @AfterAll
    public void dropTables() {
        jdbcTemplate.update("drop table person;");
        jdbcTemplate.update("drop table loan;");
    }

}
