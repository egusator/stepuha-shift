package ru.cft.stepuha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cft.stepuha.repository.model.PersonEntity;
import ru.cft.stepuha.service.PersonService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/person")
public class PersonController {
    private PersonService personService;
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("create")
    public void createPerson (@RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam (required = false) String middleName,
                              @RequestParam BigDecimal money,
                              @RequestParam String login) {
        personService.createPerson(firstName, lastName, middleName, money, login);
    }
    @PostMapping("addMoney")
    public void addMoneyToPerson (@RequestParam long id, @RequestParam BigDecimal amount) {
        personService.addMoneyToPerson(id, amount);
    }

    @PostMapping("takeMoney")
    public void takeMoneyFromPerson (@RequestParam long id, @RequestParam BigDecimal amount) {
        personService.takeMoneyFromPerson(id, amount);
    }

}
