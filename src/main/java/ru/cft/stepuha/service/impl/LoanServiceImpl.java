package ru.cft.stepuha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.stepuha.repository.LoanRepository;
import ru.cft.stepuha.repository.model.LoanEntity;

import ru.cft.stepuha.service.LoanService;

import java.util.List;


@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public List<LoanEntity> getAllLoan() {
        return loanRepository.selectAllLoan();
    }
}