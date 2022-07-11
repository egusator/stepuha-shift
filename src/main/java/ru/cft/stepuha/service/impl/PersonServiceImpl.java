package ru.cft.stepuha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.stepuha.repository.PersonRepository;
import ru.cft.stepuha.service.PersonService;

import java.math.BigDecimal;


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
                             String middleName,
                             BigDecimal money,
                             String login) {
        personRepository.insertPerson(firstName, lastName, middleName, money, login);
    }

    @Override
    public void addMoneyToPerson(long id, BigDecimal moneyAmount) {
        personRepository.addMoneyToPersonById(id, moneyAmount);
    }

    @Override
    public void takeMoneyFromPerson (long id, BigDecimal moneyAmount) {
        personRepository.takeMoneyFromPersonById(id, moneyAmount);
    }

}