package ru.cft.stepuha.repository;

import ru.cft.stepuha.repository.model.LoanAndPersonEntity;

import java.util.List;

public interface LoanAndPersonRepository {
    public List<LoanAndPersonEntity> getLoansForLendingById(long lenderId);
}
