package ru.cft.stepuha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.stepuha.controller.dto.EmptyDTO;
import ru.cft.stepuha.controller.dto.ErrorDTO;
import ru.cft.stepuha.controller.errors.AppError;

import ru.cft.stepuha.service.PersonService;
import org.apache.commons.lang3.StringUtils;
import ru.cft.stepuha.service.exceptions.*;

import java.math.BigDecimal;

import java.util.Optional;

@RestController
@RequestMapping("api/person")
public class PersonController {
    private final PersonService personService;
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("create")
    public ResponseEntity<?> createPerson (@RequestParam String firstName,
                                           @RequestParam String lastName,
                                           @RequestParam (required = false) Optional<String> middleName,
                                           @RequestParam BigDecimal money,
                                           @RequestParam String login) {
        try {
            if (!(StringUtils.isNotBlank(firstName)
                    && StringUtils.isNotEmpty(firstName))) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(5000,
                        "Name part can't be empty")),
                        HttpStatus.BAD_REQUEST);
            }

            if (!(StringUtils.isNotBlank(lastName)
                    && StringUtils.isNotEmpty(lastName))) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(5000,
                        "Name part can't be empty")),
                        HttpStatus.BAD_REQUEST);
            }

            if (middleName.isPresent())
                if (!(StringUtils.isNotBlank(middleName.get())
                        && StringUtils.isNotEmpty(middleName.get()))) {
                    return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(5000,
                            "Name part can't be empty")),
                            HttpStatus.BAD_REQUEST);
                }

            if (money == null) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(2001,
                        "Money value cannot be null")),
                        HttpStatus.BAD_REQUEST);
            }

            if (money.compareTo(BigDecimal.valueOf(0)) <= 0) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(3001,
                        "Money should be positive")),
                        HttpStatus.BAD_REQUEST);
            }

            if (!(StringUtils.isNotBlank(login)
                    && StringUtils.isNotEmpty(login))) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(5007,
                        "Login can't be empty")),
                        HttpStatus.BAD_REQUEST);
            }

            personService.createPerson(firstName, lastName, middleName, money, login);

            return new ResponseEntity<>(new EmptyDTO("OK"), HttpStatus.OK);
        } catch (WrongNamePartFormatException e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(5001,
                    "Wrong format of the name part" +
                    " (Example of correct format: 'Egor')")),
                    HttpStatus.BAD_REQUEST);
        } catch (WrongLoginFormatException e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(5002,
                    "Wrong format of the login" +
                    " (login has to contain only letters and numbers)")), HttpStatus.BAD_REQUEST);
        } catch (NamePartTooLongException e){
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(5003,
                    "Name part is too long" +
                    " (max length: 50)")), HttpStatus.BAD_REQUEST);
        } catch (LoginTooLongException e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(5004,
                    "Login is too long" +
                    " (max length: 30)")), HttpStatus.BAD_REQUEST);
        } catch (LoginIsUsedException e){
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(5005,
                    "There is user with this login" +
                    " (login must be unique)")), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                                                                            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("addMoney")
    public ResponseEntity<?> addMoneyToPerson (@RequestParam Long id, @RequestParam BigDecimal amount) {
        try {
            if (id == null) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(2000,
                        "Id can not be null")),
                        HttpStatus.BAD_REQUEST);
            } else if (id <= 0) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(3000,
                        "Id should be positive")),
                        HttpStatus.BAD_REQUEST);
            } else if (amount == null) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(2001,
                        "Money value cannot be null")),
                        HttpStatus.BAD_REQUEST);
            } else if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(3001,
                        "Money should be positive")),
                        HttpStatus.BAD_REQUEST);
            }
            personService.addMoneyToPerson(id, amount);
            return new ResponseEntity<>(new EmptyDTO("OK"), HttpStatus.OK);
        } catch (UserNotFoundException e){
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(6001,
                    "There is no user with this id")),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000,
                    "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("takeMoney")
    public ResponseEntity<?> takeMoneyFromPerson (@RequestParam Long id, @RequestParam BigDecimal amount) {
        try {
            if (id == null) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(2000,
                        "Id can not be null")),
                        HttpStatus.BAD_REQUEST);
            } else if (id <= 0) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(3000,
                        "Id should be positive")),
                        HttpStatus.BAD_REQUEST);
            } else if (amount == null) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(2001,
                        "Money value cannot be null")),
                        HttpStatus.BAD_REQUEST);
            } else if (amount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(3001,
                        "Money should be positive")),
                        HttpStatus.BAD_REQUEST);
            }
            personService.takeMoneyFromPerson(id, amount);
            return new ResponseEntity<>(new EmptyDTO("OK"), HttpStatus.OK);
        } catch (UserNotFoundException e){
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(6001,
                    "There is no user with this id")),
                    HttpStatus.BAD_REQUEST);
        } catch (NotEnoughMoneyException e){
            return new ResponseEntity<>(new ErrorDTO("ERROR",
                    new AppError(1001,
                            "Not enough balance to complete the action")),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
