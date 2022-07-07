package ru.cft.stepuha.service;

import ru.cft.stepuha.repository.model.LoanEntity;

import java.util.List;


public interface LoanService {
    public List<LoanEntity> getAllLoan();
}
