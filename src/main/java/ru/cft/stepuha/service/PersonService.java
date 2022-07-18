package ru.cft.stepuha.service;

import ru.cft.stepuha.repository.model.PersonEntity;

import java.math.BigDecimal;
import java.util.List;

public interface PersonService {

    public void createPerson(String firstName,
                             String lastName,
                             String middleName,
                             BigDecimal money,
                             String login);

    public void addMoneyToPerson(long id, BigDecimal moneyAmount);

    public void takeMoneyFromPerson(long id, BigDecimal moneyAmount);
}
