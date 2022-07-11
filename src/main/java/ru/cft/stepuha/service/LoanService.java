package ru.cft.stepuha.service;

import ru.cft.stepuha.repository.model.LoanEntity;

import java.math.BigDecimal;
import java.util.List;


public interface LoanService {
    public void createLoan (long borrowerId, BigDecimal moneyAmount);

    public List<LoanEntity> getLoansForLending(long lenderId);

    public List<LoanEntity> getLoansForRefunding(long borrowerId);
    public void lendMoney(long lenderId, long loanId);

    public List<LoanEntity> getPromisedLoans(long lenderId);
    public void refundMoney(long loanId);
}
