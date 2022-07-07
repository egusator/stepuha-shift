package ru.cft.stepuha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.stepuha.repository.model.PersonEntity;
import ru.cft.stepuha.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("api/person")
public class PersonController {
    private PersonService personService;
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("get/all")
    public List<PersonEntity> getAll() {
        return personService.getAllPerson();
    }
}
