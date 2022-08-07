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
import ru.cft.stepuha.service.exceptions.LoanNotFoundException;
import ru.cft.stepuha.service.exceptions.NotEnoughMoneyException;
import ru.cft.stepuha.service.exceptions.UserNotFoundException;


import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/loan")
public class LoanController {
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("create")
    public ResponseEntity<?> createLoan(@RequestParam Long borrowerId,
                           @RequestParam BigDecimal money) {
        try {
            if (borrowerId == null) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(2000, "Id can not be null")),
                        HttpStatus.OK);
            } else if (borrowerId <= 0) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(3000, "Id should be positive")),
                        HttpStatus.OK);
            } else if (money == null) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(2001, "Money value cannot be null")),
                        HttpStatus.OK);
            } else if (money.compareTo(BigDecimal.valueOf(0)) <= 0) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(3001, "Money should be positive")),
                        HttpStatus.OK);
            }
            loanService.createLoanRequest(borrowerId, money);
            return new ResponseEntity<>(new EmptyDTO("OK"), HttpStatus.OK);
        } catch (UserNotFoundException e){
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(4001, "The user with this id is not found")),
                    HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.OK);
        }
    }

    @GetMapping ("free-requests")
    public ResponseEntity<?> getLoansForLending(@RequestParam Long personId) {
        try {
            if (personId == null) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(2000, "Id can not be null")),
                        HttpStatus.BAD_REQUEST);
            } else if (personId <= 0) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(3000, "Id should be positive")),
                        HttpStatus.BAD_REQUEST);
            }
            List<LoanEntity> result = loanService.getLoanRequestsFreeForLending(personId);
            return new ResponseEntity<>(new LoanDTO("OK", result),
                    HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(4001, "The user with this id is not found")),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping ("need-to-refund")
    public ResponseEntity<?> getLoansForRefunding(@RequestParam Long personId) {
        try {
            if (personId == null) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(2000, "Id can not be null")),
                        HttpStatus.BAD_REQUEST);
            } else if (personId <= 0) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(3000, "Id should be positive")),
                        HttpStatus.BAD_REQUEST);
            }
            List<LoanEntity> result = loanService.getLoansWhichNeedToBeRefunded(personId);
            return new ResponseEntity<>(new LoanDTO("OK", result),
                    HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(4001, "The user with this id is not found")),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("lend")
    public ResponseEntity<?> lendMoney(@RequestParam Long lenderId, @RequestParam Long loanId) {
        try {
            if (lenderId == null || loanId == null) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(2000, "Id can not be null")),
                        HttpStatus.BAD_REQUEST);
            } else if (lenderId <= 0 || loanId <= 0) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(3000, "Id should be positive")),
                        HttpStatus.BAD_REQUEST);
            }

            loanService.lendMoney(lenderId,loanId);
            return new ResponseEntity<>(new EmptyDTO("OK"), HttpStatus.OK);
        }  catch (NotEnoughMoneyException notEnoughMoneyException) {

            return new ResponseEntity<>(new ErrorDTO("ERROR",
                    new AppError(1001,
                            "Not enough balance to complete the action")),
                    HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(4001, "The user with this id is not found")),
                    HttpStatus.BAD_REQUEST);
        } catch (LoanNotFoundException e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(4002, "The loan with this id is not found")),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("promised")
    public ResponseEntity<?> getDebtors(@RequestParam Long lenderId) {
        try {
            if (lenderId == null) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(2000, "Id can not be null")),
                        HttpStatus.BAD_REQUEST);
            } else if (lenderId <= 0) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(3000, "Id should be positive")),
                        HttpStatus.BAD_REQUEST);
            }
            List<LoanEntity> result = loanService.getPromisedLoans(lenderId);
            return new ResponseEntity<>(new LoanDTO("OK", result),
                    HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(4001, "The user with this id is not found")),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("user-requests")
    public ResponseEntity<?> getLoanRequestsOfUser(@RequestParam Long userId) {
        try {
            if (userId == null) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(2000, "Id can not be null")),
                        HttpStatus.BAD_REQUEST);
            } else if (userId <= 0) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(3000, "Id should be positive")),
                        HttpStatus.BAD_REQUEST);
            }
            List<LoanEntity> result = loanService.getLoanRequestsOfUser(userId);
            return new ResponseEntity<>(new LoanDTO("OK", result),
                    HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(4001, "The user with this id is not found")),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("refund")
    public ResponseEntity<?> refundMoney(@RequestParam Long loanId){
        try {
            if (loanId == null) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(2000, "Id can not be null")),
                        HttpStatus.BAD_REQUEST);
            } else if (loanId <= 0) {
                return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(3000, "Id should be positive")),
                        HttpStatus.BAD_REQUEST);
            }
            loanService.refundMoney(loanId);
            return new ResponseEntity<>(new EmptyDTO("OK"), HttpStatus.OK);
        } catch (NotEnoughMoneyException notEnoughMoneyException) {
            return new ResponseEntity<>(new ErrorDTO("ERROR",
                                                        new AppError(1001,
                                                                "Not enough balance to complete the action")),
                                         HttpStatus.BAD_REQUEST);
        } catch (LoanNotFoundException e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(4002, "The loan with this id is not found")),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO("ERROR", new AppError(1000, "Unknown error")),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
