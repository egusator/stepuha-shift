package ru.cft.stepuha.repository;

import ru.cft.stepuha.repository.model.LoanEntity;

import java.math.BigDecimal;
import java.util.List;

public interface LoanRepository {
    public void createLoan (long borrowerId, BigDecimal moneyAmount);

    public List<LoanEntity> getLoansForLendingById(long lenderId);
    public List<LoanEntity> getLoansForRefundingById(long borrowerId);
    public void lendMoneyByLoanId(long loanId, long lenderId);

    public void refundMoneyByLoanId(long loanId);

    public boolean loanExists (long loanId);
    public  List<LoanEntity> getPromisedLoansByLenderId (long lenderId);
    public LoanEntity getLoanById(long loanId);

    List<LoanEntity> getLoanRequestsByUserId(long userId);
}
