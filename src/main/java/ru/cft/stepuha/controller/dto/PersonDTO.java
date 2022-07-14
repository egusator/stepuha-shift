package ru.cft.stepuha.controller.dto;

import ru.cft.stepuha.controller.errors.AppError;
import ru.cft.stepuha.repository.model.PersonEntity;

import java.util.List;

public class PersonDTO {
    private String status;
    private List<PersonEntity> personList;



    public PersonDTO(String status, List<PersonEntity> personList) {
        this.status = status;
        this.personList = personList;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PersonEntity> getPersonList() {
        return personList;
    }

    public void setPersonList(List<PersonEntity> personList) {
        this.personList = personList;
    }

}
