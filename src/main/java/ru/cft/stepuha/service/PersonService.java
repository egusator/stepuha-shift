package ru.cft.stepuha.service;

import ru.cft.stepuha.service.exceptions.*;

import java.math.BigDecimal;

import java.util.Optional;

public interface PersonService {

    public void createPerson(String firstName,
                             String lastName,
                             Optional<String> middleName,
                             BigDecimal money,
                             String login) throws WrongNamePartFormatException, WrongLoginFormatException,
                                                    NamePartTooLongException, LoginTooLongException, LoginIsUsedException;

    public void addMoneyToPerson(long id, BigDecimal moneyAmount) throws UserNotFoundException;

    public void takeMoneyFromPerson(long id, BigDecimal moneyAmount) throws UserNotFoundException, NotEnoughMoneyException;
}
