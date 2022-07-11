package ru.cft.stepuha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cft.stepuha.repository.model.LoanEntity;

import ru.cft.stepuha.service.LoanService;


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
    public void createLoan(@RequestParam long borrowerId,
                           @RequestParam BigDecimal money) {
        loanService.createLoan(borrowerId, money);
    }

    @GetMapping ("avalibleForLending")
    public List<LoanEntity> getLoansForLending(@RequestParam long personId) {
        return loanService.getLoansForLending(personId);
    }

    @GetMapping ("avalibleForRefunding")
    public List<LoanEntity> getLoansForRefunding(@RequestParam long personId) {
        return loanService.getLoansForRefunding(personId);
    }
    @PostMapping("lend")
    public void lendMoney(@RequestParam long lenderId, @RequestParam long loanId) {
        loanService.lendMoney(lenderId,loanId);
    }

    @GetMapping("promised")
    public List<LoanEntity> getDebtors(@RequestParam long lenderId) {
        return loanService.getPromisedLoans(lenderId);
    }

    @GetMapping("")
    @PostMapping("refund")
    public void refundMoney(@RequestParam long loanId){
        loanService.refundMoney(loanId);
    }

}
