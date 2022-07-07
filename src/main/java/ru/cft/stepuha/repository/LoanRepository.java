package ru.cft.stepuha.repository;

import ru.cft.stepuha.repository.model.LoanEntity;

import java.util.List;

public interface LoanRepository {
    public List<LoanEntity> selectAllLoan();
}
