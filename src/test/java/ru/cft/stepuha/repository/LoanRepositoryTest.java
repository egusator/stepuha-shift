package ru.cft.stepuha.repository;


import org.junit.Assert;

import org.junit.jupiter.api.*;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.RowMapper;

import ru.cft.stepuha.repository.impl.LoanRepositoryImpl;

import ru.cft.stepuha.repository.mapper.LoanEntityRowMapper;

import ru.cft.stepuha.repository.model.LoanEntity;

import java.math.BigDecimal;
import java.util.List;


@DataJdbcTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoanRepositoryTest {
    private JdbcTemplate jdbcTemplate;
    private LoanRepositoryImpl loanRepository;
    public RowMapper<LoanEntity> rowMapper = new LoanEntityRowMapper();

    @Autowired
    public LoanRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.loanRepository = new LoanRepositoryImpl(jdbcTemplate, rowMapper);
    }

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

    /*

    public void refundMoneyByLoanId(long loanId);

    */

    @Test
    public void getLoanById_ShouldReturnLoanById() {
        LoanEntity loan = loanRepository.getLoanById(1);
        Assert.assertEquals(1, loan.getBorrowerId());
        Assert.assertEquals(1, loan.getState());
        Assert.assertEquals(BigDecimal.valueOf(400), loan.getMoney());
    }
    @Test
    public void getLoansForLendingById_ShouldReturnLoansAvalibleForLending() {
        List<LoanEntity> loanList = loanRepository.getLoansForLendingById(1);
        Assert.assertEquals(2, loanList.size());
        Assert.assertEquals(2, loanList.get(0).getId());
        Assert.assertEquals(3, loanList.get(1).getId());
    }

    @Test
    public void createLoan_ShouldInsertNewLoan() {
        loanRepository.createLoan(1, BigDecimal.valueOf(1000));
        LoanEntity loan = jdbcTemplate.query("select * from loan where id = 7;", rowMapper).get(0);
        Assert.assertEquals(1, loan.getBorrowerId());
        Assert.assertEquals(1,loan.getState());
        Assert.assertEquals(BigDecimal.valueOf(1000), loan.getMoney());
    }

    @Test
    public void getLoanRequestsByUserId_ShouldReturnLoanRequestOfUserById() {
        List<LoanEntity> loanList = loanRepository.getLoanRequestsByUserId(1);
        Assert.assertEquals(1, loanList.size());
        Assert.assertEquals(1, loanList.get(0).getId());
    }

    @Test
    public void getLoansForRefundingById_ShouldReturnLoansWhichNeedToBeRefundedByThisUser() {
        List <LoanEntity> loanList = loanRepository.getLoansForRefundingById(2);
        Assert.assertEquals(2, loanList.size());
        Assert.assertEquals(4,loanList.get(0).getId());
        Assert.assertEquals(5,loanList.get(1).getId());
    }

    @Test
    public void lendMoneyByLoanId_ShouldChangeStateAndLendingTimeByLoanId() {
        loanRepository.lendMoneyByLoanId(2, 1);
        LoanEntity loan = jdbcTemplate.query("select * from loan where id = 2",rowMapper).get(0);
        Assert.assertEquals(2, loan.getState());
        Assert.assertEquals(1, loan.getLenderId());
    }

    @Test
    public void refundMoneyByLoanId_lendMoneyByLoanId_ShouldChangeStateAndTimeByLoanId() {
        loanRepository.refundMoneyByLoanId(5);
        LoanEntity loan = jdbcTemplate.query("select * from loan where id = 5",rowMapper).get(0);
        Assert.assertEquals(3, loan.getState());
    }
    @Test
    public void getPromisedLoansByLenderId_ShouldReturnLoansWhichNeedToBeRefundedToThisUser(){
        List <LoanEntity> loanList = loanRepository.getPromisedLoansByLenderId(1);
        Assert.assertEquals(2, loanList.size());
        Assert.assertEquals(5, loanList.get(0).getId());
        Assert.assertEquals(6, loanList.get(1).getId());
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
