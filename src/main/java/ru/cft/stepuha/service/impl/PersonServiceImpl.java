package ru.cft.stepuha.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.stepuha.repository.PersonRepository;
import ru.cft.stepuha.service.PersonService;
import ru.cft.stepuha.service.exceptions.*;

import java.math.BigDecimal;
import java.util.Optional;


@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void createPerson(String firstName,
                             String lastName,
                             Optional<String> middleName,
                             BigDecimal money,
                             String login) throws WrongNamePartFormatException, WrongLoginFormatException,
                             NamePartTooLongException, LoginTooLongException, LoginIsUsedException {

        if (firstName.length() > 50) {
            throw new NamePartTooLongException();
        }
        if (lastName.length() > 50) {
            throw new NamePartTooLongException();
        }
        if (middleName.isPresent()) {
            if (middleName.get().length() > 50) {
                throw new NamePartTooLongException();
            }
        }
        if (login.length() > 30) {
            throw new LoginTooLongException();
        }
        if (personRepository.loginIsUsed(login)) {
            throw new LoginIsUsedException();
        }
        if (!(firstName.substring(0, 1).matches("[A-Z]+") &&
                firstName.substring(1).matches("[a-z]+"))) {
            throw new WrongNamePartFormatException();
        }
        if (!(lastName.substring(0, 1).matches("[A-Z]+") &&
                lastName.substring(1).matches("[a-z]+"))) {
            throw new WrongNamePartFormatException();
        }
        if (middleName.isPresent()){
            if (!(middleName.get().substring(0, 1).matches("[A-Z]+") &&
                    middleName.get().substring(1).matches("[a-z]+"))) {
                throw new WrongNamePartFormatException();
            }
        }
        if (!(StringUtils.isAlphanumeric(login))) {
            throw new WrongLoginFormatException();
        }
        if (middleName.isPresent())
            personRepository.insertPerson(firstName, lastName, middleName.get(), money, login);
        else
            personRepository.insertPerson(firstName,lastName,null, money, login);
    }

    @Override
    public void addMoneyToPerson(long id, BigDecimal moneyAmount) throws UserNotFoundException{
        if(personRepository.personExists(id)) {
            personRepository.addMoneyToPersonById(id, moneyAmount);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public void takeMoneyFromPerson (long id, BigDecimal moneyAmount) throws UserNotFoundException, NotEnoughMoneyException {
        if(personRepository.personExists(id)) {
            if (personRepository.getPersonById(id).getMoney().compareTo(moneyAmount) < 0) {
                throw new NotEnoughMoneyException();
            }
            personRepository.takeMoneyFromPersonById(id, moneyAmount);
        } else {
            throw new UserNotFoundException();
        }
    }

}