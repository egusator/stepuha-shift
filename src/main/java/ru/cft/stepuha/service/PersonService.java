package ru.cft.stepuha.service;

import ru.cft.stepuha.repository.model.PersonEntity;

import java.util.List;

public interface PersonService {
    public List<PersonEntity> getAllPerson();
}
