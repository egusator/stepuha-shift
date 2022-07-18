package ru.cft.stepuha.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.stepuha.repository.LoanRepository;
import ru.cft.stepuha.repository.PersonRepository;
import ru.cft.stepuha.repository.model.LoanEntity;

import ru.cft.stepuha.service.LoanService;
import ru.cft.stepuha.service.exceptions.LoanNotFoundException;
import ru.cft.stepuha.service.exceptions.NotEnoughMoneyException;
import ru.cft.stepuha.service.exceptions.UserNotFoundException;

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
    public void createLoanRequest(long borrowerId, BigDecimal moneyAmount) throws UserNotFoundException {
        if (personRepository.personExists(borrowerId)) {
        loanRepository.createLoan(borrowerId, moneyAmount);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public List<LoanEntity> getLoanRequestsFreeForLending(long lenderId) throws UserNotFoundException {
        if (personRepository.personExists(lenderId)) {
        return loanRepository.getLoansForLendingById(lenderId);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public List<LoanEntity> getLoansWhichNeedToBeRefunded(long borrowerId) throws UserNotFoundException {
        if (personRepository.personExists(borrowerId)) {
            return loanRepository.getLoansForRefundingById(borrowerId);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public List<LoanEntity> getPromisedLoans(long lenderId) throws UserNotFoundException {
        if (personRepository.personExists(lenderId)) {
            return loanRepository.getPromisedLoansByLenderId(lenderId);
        } else {
            throw new UserNotFoundException();
        }
    }
    @Override
    public void lendMoney(long lenderId, long loanId) throws NotEnoughMoneyException, UserNotFoundException, LoanNotFoundException {
        if (!loanRepository.loanExists(loanId)) {
            throw new LoanNotFoundException();
        } else if (!personRepository.personExists(lenderId)) {
            throw new UserNotFoundException();
        } else {
            LoanEntity currentLoan = loanRepository.getLoanById(loanId);
            long borrowerId = currentLoan.getBorrowerId();
            BigDecimal moneyAmount = currentLoan.getMoney();
            BigDecimal personBalance = personRepository.getPersonById(lenderId).getMoney();
            if (personBalance.compareTo(moneyAmount) >= 0) {
                personRepository.addMoneyToPersonById(borrowerId, moneyAmount);
                personRepository.takeMoneyFromPersonById(lenderId, moneyAmount);
                loanRepository.lendMoneyByLoanId(loanId, lenderId);
            } else {
                throw new NotEnoughMoneyException();
            }
        }
    }

    @Override
    public void refundMoney(long loanId) throws NotEnoughMoneyException, LoanNotFoundException {
        if (!loanRepository.loanExists(loanId)) {
            throw new LoanNotFoundException();
        } else {
            LoanEntity currentLoan = loanRepository.getLoanById(loanId);
            long lenderId = currentLoan.getLenderId();
            long borrowerId = currentLoan.getBorrowerId();
            BigDecimal moneyAmount = currentLoan.getMoney();
            BigDecimal personBalance = personRepository.getPersonById(borrowerId).getMoney();
            if (personBalance.compareTo(moneyAmount) >= 0) {
                personRepository.takeMoneyFromPersonById(borrowerId, moneyAmount);
                personRepository.addMoneyToPersonById(lenderId, moneyAmount);
                loanRepository.refundMoneyByLoanId(loanId);
            } else {
                throw new NotEnoughMoneyException();
            }
        }
    }

    @Override
    public List<LoanEntity> getLoanRequestsOfUser(long userId) throws UserNotFoundException {
        if (personRepository.personExists(userId)) {
        return loanRepository.getLoanRequestsByUserId(userId);
        } else  {
            throw new UserNotFoundException();
        }
    }
}