package ru.cft.stepuha.repository;

import ru.cft.stepuha.repository.model.PersonEntity;

import java.math.BigDecimal;



public interface PersonRepository {
    public PersonEntity getPersonById(long personId);
    public void insertPerson(String firstName,
                             String lastName,
                             String middleName,
                             BigDecimal money,
                             String login);
    public void addMoneyToPersonById(long id, BigDecimal moneyAmount);
    public void takeMoneyFromPersonById(long id, BigDecimal moneyAmount);

    public boolean personExists (long id);
    public boolean loginIsUsed (String login);
    public boolean personWithThisLoginExists(String login);
    public PersonEntity getPersonByLogin(String login);
}
