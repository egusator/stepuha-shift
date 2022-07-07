package ru.cft.stepuha.repository;

import ru.cft.stepuha.repository.model.PersonEntity;

import java.util.List;


public interface PersonRepository {
    public List<PersonEntity> selectAllPerson();
}
