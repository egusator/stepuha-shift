package ru.cft.stepuha.service;

import ru.cft.stepuha.repository.model.LoanAndPersonEntity;
import ru.cft.stepuha.repository.model.LoanEntity;
import ru.cft.stepuha.service.exceptions.LoanNotFoundException;
import ru.cft.stepuha.service.exceptions.NotEnoughMoneyException;
import ru.cft.stepuha.service.exceptions.UserNotFoundException;

import java.math.BigDecimal;
import java.util.List;


public interface LoanService {

    public void createLoanRequest(long borrowerId, BigDecimal moneyAmount) throws UserNotFoundException;


    public List<LoanAndPersonEntity> getLoanRequestsFreeForLending(long lenderId)  throws UserNotFoundException;

    public List<LoanEntity> getLoansWhichNeedToBeRefunded(long borrowerId) throws UserNotFoundException;

    public void lendMoney(long lenderId, long loanId) throws NotEnoughMoneyException,  UserNotFoundException, LoanNotFoundException;


    public List<LoanEntity> getPromisedLoans(long lenderId) throws UserNotFoundException;

    public void refundMoney(long loanId) throws NotEnoughMoneyException, LoanNotFoundException;

    List<LoanEntity> getLoanRequestsOfUser(long userId) throws UserNotFoundException;
}
