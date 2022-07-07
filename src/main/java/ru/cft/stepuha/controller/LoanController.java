package ru.cft.stepuha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.stepuha.repository.model.LoanEntity;

import ru.cft.stepuha.service.LoanService;


import java.util.List;

@RestController
@RequestMapping("api/loan")
public class LoanController {
    private LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("get/all")
    public List<LoanEntity> getAll() {
        return loanService.getAllLoan();
    }
}
