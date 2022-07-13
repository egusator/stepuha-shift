package ru.cft.stepuha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.stepuha.controller.dto.EmptyDTO;
import ru.cft.stepuha.controller.dto.ErrorDTO;
import ru.cft.stepuha.controller.errors.AppError;
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
    public ResponseEntity<?> createPerson (@RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam (required = false) String middleName,
                              @RequestParam BigDecimal money,
                              @RequestParam String login) {
        try {
            personService.createPerson(firstName, lastName, middleName, money, login);
            return new ResponseEntity<>(new EmptyDTO("OK"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                                                                            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("addMoney")
    public ResponseEntity<?> addMoneyToPerson (@RequestParam long id, @RequestParam BigDecimal amount) {
        try {
            personService.addMoneyToPerson(id, amount);
            return new ResponseEntity<>(new EmptyDTO("OK"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("takeMoney")
    public ResponseEntity<?> takeMoneyFromPerson (@RequestParam long id, @RequestParam BigDecimal amount) {
        try {
            personService.takeMoneyFromPerson(id, amount);
            return new ResponseEntity<>(new EmptyDTO("OK"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
