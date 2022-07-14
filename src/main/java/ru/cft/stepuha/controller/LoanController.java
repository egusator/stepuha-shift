package ru.cft.stepuha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.stepuha.controller.dto.EmptyDTO;
import ru.cft.stepuha.controller.dto.ErrorDTO;
import ru.cft.stepuha.controller.dto.LoanDTO;
import ru.cft.stepuha.controller.errors.AppError;
import ru.cft.stepuha.repository.model.LoanEntity;

import ru.cft.stepuha.service.LoanService;
import ru.cft.stepuha.service.exceptions.NotEnoughMoneyException;


import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/loan")
public class LoanController {
    private LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("create")
    public ResponseEntity<?> createLoan(@RequestParam long borrowerId,
                           @RequestParam BigDecimal money) {
        try {
            loanService.createLoan(borrowerId, money);
            return new ResponseEntity<>(new EmptyDTO("OK"), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping ("avalibleForLending")
    public ResponseEntity<?> getLoansForLending(@RequestParam long personId) {
        try {
            List<LoanEntity> result = loanService.getLoansForLending(personId);
            return new ResponseEntity<>(new LoanDTO("OK", result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping ("avalibleForRefunding")
    public ResponseEntity<?> getLoansForRefunding(@RequestParam long personId) {
        try {
            List<LoanEntity> result = loanService.getLoansForRefunding(personId);
            return new ResponseEntity<>(new LoanDTO("OK", result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("lend")
    public ResponseEntity<?> lendMoney(@RequestParam long lenderId, @RequestParam long loanId) {
        try {
            loanService.lendMoney(lenderId,loanId);
            return new ResponseEntity<>(new EmptyDTO("OK"), HttpStatus.OK);
        }  catch (NotEnoughMoneyException notEnoughMoneyException) {

            return new ResponseEntity<>(new ErrorDTO("ERROR",
                    new AppError(1001,
                            "Not enough balance to complete the action")),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("promised")
    public ResponseEntity<?> getDebtors(@RequestParam long lenderId) {
        try {
            List<LoanEntity> result = loanService.getPromisedLoans(lenderId);
            return new ResponseEntity<>(new LoanDTO("OK", result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("requestsOfUser")
    public ResponseEntity<?> getLoanRequestsOfUser(@RequestParam long userId) {
        try {
            List<LoanEntity> result = loanService.getLoanRequestsOfUser(userId);
            return new ResponseEntity<>(new LoanDTO("OK", result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("refund")
    public ResponseEntity<?> refundMoney(@RequestParam long loanId){

        try {
            loanService.refundMoney(loanId);
            return new ResponseEntity<>(new EmptyDTO("OK"), HttpStatus.OK);
        } catch (NotEnoughMoneyException notEnoughMoneyException) {
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
