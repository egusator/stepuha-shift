package ru.cft.stepuha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.stepuha.repository.PersonRepository;
import ru.cft.stepuha.repository.model.PersonEntity;
import ru.cft.stepuha.service.PersonService;

import java.util.ArrayList;
import java.util.List;


@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<PersonEntity> getAllPerson() {
        return personRepository.selectAllPerson();
    }
}