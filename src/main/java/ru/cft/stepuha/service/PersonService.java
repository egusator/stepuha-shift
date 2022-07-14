package ru.cft.stepuha.service;

import ru.cft.stepuha.repository.model.PersonEntity;

import java.math.BigDecimal;
import java.util.List;

public interface PersonService {

    //добавить нового пользователя в бд
    public void createPerson(String firstName,
                             String lastName,
                             String middleName,
                             BigDecimal money,
                             String login);
    //дать денег пользователю
    public void addMoneyToPerson(long id, BigDecimal moneyAmount);

    //взять деньги у пользователя
    public void takeMoneyFromPerson(long id, BigDecimal moneyAmount);
}
