package ru.cft.stepuha.service;

import ru.cft.stepuha.repository.model.LoanEntity;

import java.math.BigDecimal;
import java.util.List;


public interface LoanService {

    //создать заявку на займ
    public void createLoan (long borrowerId, BigDecimal moneyAmount);

    //получить список чужих заявок на займ
    public List<LoanEntity> getLoansForLending(long lenderId);

    //получить список тех займов, по которым нужно вернуть деньги
    public List<LoanEntity> getLoansForRefunding(long borrowerId);

    //ответить на чужую заявку на займ и дать денег
    public void lendMoney(long lenderId, long loanId);

    //получить список тех займов, по которым тебе должны
    public List<LoanEntity> getPromisedLoans(long lenderId);

    //вернуть деньги по займу
    public void refundMoney(long loanId);

    //посмотреть свои заявки на займ
    List<LoanEntity> getLoanRequestsOfUser(long userId);
}
