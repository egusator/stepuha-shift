package ru.cft.stepuha.repository;

import ru.cft.stepuha.repository.model.PersonEntity;

import java.math.BigDecimal;
import java.util.List;


public interface PersonRepository {
    public List<PersonEntity> selectAllPerson();
    public void insertPerson(String firstName,
                             String lastName,
                             String middleName,
                             BigDecimal money,
                             String login);
    public void addMoneyToPersonById(long id, BigDecimal moneyAmount);
    public void takeMoneyFromPersonById(long id, BigDecimal moneyAmount);
}
