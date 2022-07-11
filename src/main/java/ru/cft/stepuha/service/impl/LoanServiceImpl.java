package ru.cft.stepuha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.stepuha.repository.LoanRepository;
import ru.cft.stepuha.repository.PersonRepository;
import ru.cft.stepuha.repository.model.LoanEntity;

import ru.cft.stepuha.service.LoanService;

import java.math.BigDecimal;
import java.util.List;


@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final PersonRepository personRepository;
    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, PersonRepository personRepository) {
        this.loanRepository = loanRepository;
        this.personRepository = personRepository;
    }

    @Override
    public void createLoan (long borrowerId, BigDecimal moneyAmount) {
        loanRepository.createLoan(borrowerId, moneyAmount);
    }

    @Override
    public List<LoanEntity> getLoansForLending(long lenderId) {
        return loanRepository.getLoansForLendingById(lenderId);
    }

    @Override
    public List<LoanEntity> getLoansForRefunding (long borrowerId) {
        return loanRepository.getLoansForRefundingById(borrowerId);
    }

    @Override
    public List<LoanEntity> getPromisedLoans(long lenderId) {
        return loanRepository.getPromisedLoansByLenderId(lenderId);
    }
    @Override
    public void lendMoney(long lenderId, long loanId) {
        LoanEntity currentLoan = loanRepository.getLoanById(loanId);
        long borrowerId = currentLoan.getBorrowerId();
        BigDecimal moneyAmount = currentLoan.getMoney();
        personRepository.addMoneyToPersonById(borrowerId, moneyAmount);
        personRepository.takeMoneyFromPersonById(lenderId, moneyAmount);
        loanRepository.lendMoneyByLoanId(loanId, lenderId);
    }

    @Override
    public void refundMoney(long loanId) {
        LoanEntity currentLoan = loanRepository.getLoanById(loanId);
        long lenderId = currentLoan.getLenderId();
        long borrowerId = currentLoan.getBorrowerId();
        BigDecimal moneyAmount = currentLoan.getMoney();
        personRepository.takeMoneyFromPersonById(borrowerId, moneyAmount);
        personRepository.addMoneyToPersonById(lenderId, moneyAmount);
        loanRepository.refundMoneyByLoanId(loanId);
    }
}