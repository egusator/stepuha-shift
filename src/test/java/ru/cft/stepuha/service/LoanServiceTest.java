package ru.cft.stepuha.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.cft.stepuha.repository.LoanRepository;
import ru.cft.stepuha.repository.PersonRepository;
import ru.cft.stepuha.repository.model.LoanEntity;
import ru.cft.stepuha.repository.model.PersonEntity;
import ru.cft.stepuha.service.exceptions.LoanNotFoundException;
import ru.cft.stepuha.service.exceptions.NotEnoughMoneyException;
import ru.cft.stepuha.service.exceptions.UserNotFoundException;
import ru.cft.stepuha.service.impl.LoanServiceImpl;

import java.math.BigDecimal;


@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;
    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    public void createLoanRequest_ShouldCreateRequestOrReturnException() {
        long existingPersonId = 1;
        long nonexistentPersonId = 2;
        given(personRepository.personExists(eq(existingPersonId))).willReturn(true);
        given(personRepository.personExists(eq(nonexistentPersonId))).willReturn(false);
        assertThrows(UserNotFoundException.class, () -> {
            loanService.createLoanRequest(nonexistentPersonId, BigDecimal.TEN);
        });

        assertDoesNotThrow(() -> {
            loanService.createLoanRequest(existingPersonId, BigDecimal.TEN);
        });
        verify(loanRepository, times(1)).createLoan(anyLong(), any(BigDecimal.class));
        verify(loanRepository, times(1)).createLoan(eq(existingPersonId), any(BigDecimal.class));
    }

    @Test
    public void getLoanRequestsFreeForLending_ShouldReturnListOfLoansAvalibleForLending() {
        long existingPersonId = 1;
        long nonexistentPersonId = 2;
        given(personRepository.personExists(existingPersonId)).willReturn(true);
        given(personRepository.personExists(nonexistentPersonId)).willReturn(false);
        assertThrows(UserNotFoundException.class, () -> {
            loanService.getLoanRequestsFreeForLending(nonexistentPersonId);
        });
        assertDoesNotThrow(() -> {
            loanService.getLoanRequestsFreeForLending(existingPersonId);
        });
        verify(loanRepository, times(1)).getLoansForLendingById(anyLong());
        verify(loanRepository, times(1)).getLoansForLendingById(existingPersonId);
    }

    @Test
    public void getLoansWhichNeedToBeRefunded_ShouldReturnListOfLoansWhichNeedToBeRefundedByThisUser() {
        long existingPersonId = 1;
        long nonexistentPersonId = 2;
        given(personRepository.personExists(existingPersonId)).willReturn(true);
        given(personRepository.personExists(nonexistentPersonId)).willReturn(false);
        assertThrows(UserNotFoundException.class, () -> {
            loanService.getLoansWhichNeedToBeRefunded(nonexistentPersonId);
        });
        assertDoesNotThrow(() -> {
            loanService.getLoansWhichNeedToBeRefunded(existingPersonId);
        });
        verify(loanRepository, times(1)).getLoansForRefundingById(anyLong());
        verify(loanRepository, times(1)).getLoansForRefundingById(existingPersonId);
    }

    @Test
    public void getPromisedLoans_ShouldReturnLoansPromisedToRefundToThisUser() {
        long existingPersonId = 1;
        long nonexistentPersonId = 2;
        given(personRepository.personExists(existingPersonId)).willReturn(true);
        given(personRepository.personExists(nonexistentPersonId)).willReturn(false);
        assertThrows(UserNotFoundException.class, () -> {
            loanService.getPromisedLoans(nonexistentPersonId);
        });
        assertDoesNotThrow(() -> {
            loanService.getPromisedLoans(existingPersonId);
        });

        verify(loanRepository, times(1)).getPromisedLoansByLenderId(anyLong());
        verify(loanRepository, times(1)).getPromisedLoansByLenderId(existingPersonId);
    }

    @Test
    public void getLoanRequestsOfUser_ShouldReturnLoanRequestsOfUser() {

        long existingPersonId = 1;
        long nonexistentPersonId = 2;
        given(personRepository.personExists(existingPersonId)).willReturn(true);
        given(personRepository.personExists(nonexistentPersonId)).willReturn(false);
        assertThrows(UserNotFoundException.class, () -> {
            loanService.getLoanRequestsOfUser(nonexistentPersonId);
        });
        assertDoesNotThrow(() -> {
            loanService.getLoanRequestsOfUser(existingPersonId);
        });
        verify(loanRepository, times(1)).getLoanRequestsByUserId(anyLong());
        verify(loanRepository, times(1)).getLoanRequestsByUserId(existingPersonId);
    }

    @Test
    public void lendMoney_AddingMoneyToBorrower_TakingMoneyFromLender_ChangingState () {

        long existingLoanId = 1;
        long nonexistentLoanId = 2;
        long existingLenderId = 1;
        long nonexistentLenderId = 2;
        given(loanRepository.loanExists(existingLoanId)).willReturn(true);
        given(loanRepository.loanExists(nonexistentLoanId)).willReturn(false);
        given(personRepository.personExists(existingLenderId)).willReturn(true);
        given(personRepository.personExists(nonexistentLenderId)).willReturn(false);
        assertThrows(LoanNotFoundException.class, () -> {
            loanService.lendMoney(existingLenderId, nonexistentLoanId);
        });

        assertThrows(UserNotFoundException.class, () -> {
            loanService.lendMoney(nonexistentLenderId, existingLoanId);
        });

        LoanEntity mockLoanEntity = Mockito.mock(LoanEntity.class);
        PersonEntity mockLender = Mockito.mock(PersonEntity.class);

        long borrowerIdOfMockedLoan = 3;

        Mockito.when(loanRepository.getLoanById(existingLoanId)).thenReturn(mockLoanEntity);
        Mockito.when(personRepository.getPersonById(existingLenderId)).thenReturn(mockLender);
        Mockito.when(mockLoanEntity.getBorrowerId()).thenReturn(Long.valueOf(borrowerIdOfMockedLoan));

        Mockito.when(mockLender.getMoney()).thenReturn(BigDecimal.valueOf(1000));
        Mockito.when(mockLoanEntity.getMoney()).thenReturn(BigDecimal.valueOf(1500));
        assertThrows(NotEnoughMoneyException.class, () -> {
            loanService.lendMoney(existingLenderId, existingLoanId);
        });

        Mockito.when(mockLender.getMoney()).thenReturn(BigDecimal.valueOf(1000));
        Mockito.when(mockLoanEntity.getMoney()).thenReturn(BigDecimal.valueOf(1000));
        assertDoesNotThrow(() -> {
            loanService.lendMoney(existingLenderId, existingLoanId);
        });
        verify(personRepository, times(1)).addMoneyToPersonById(anyLong(), any(BigDecimal.class));
        verify(personRepository, times(1)).addMoneyToPersonById(borrowerIdOfMockedLoan, BigDecimal.valueOf(1000));
        verify(personRepository, times(1)).addMoneyToPersonById(anyLong(), any(BigDecimal.class));
        verify(personRepository, times(1)).takeMoneyFromPersonById(existingLenderId, BigDecimal.valueOf(1000));
        verify(loanRepository, times(1)).lendMoneyByLoanId(anyLong(), anyLong());
        verify(loanRepository, times(1)).lendMoneyByLoanId(existingLoanId, existingLenderId);
    }

    @Test
    public void refundMoney_AddingMoneyToLender_TakingMoneyFromBorrower_ChangingState () {
        long existingLoanId = 1;
        long nonexistentLoanId = 2;
        given(loanRepository.loanExists(existingLoanId)).willReturn(true);
        given(loanRepository.loanExists(nonexistentLoanId)).willReturn(false);
        assertThrows(LoanNotFoundException.class, () -> {
            loanService.refundMoney(nonexistentLoanId);
        });

        LoanEntity mockLoanEntity = Mockito.mock(LoanEntity.class);
        PersonEntity mockBorrower = Mockito.mock(PersonEntity.class);

        long mockedLenderId = 1;
        long mockedBorrowerId = 2;
        Mockito.when(loanRepository.getLoanById(existingLoanId)).thenReturn(mockLoanEntity);
        Mockito.when(mockLoanEntity.getLenderId()).thenReturn(Long.valueOf(mockedLenderId));
        Mockito.when(mockLoanEntity.getBorrowerId()).thenReturn(Long.valueOf(mockedBorrowerId));
        Mockito.when(personRepository.getPersonById(mockedBorrowerId)).thenReturn(mockBorrower);


        Mockito.when(mockBorrower.getMoney()).thenReturn(BigDecimal.valueOf(1000));
        Mockito.when(mockLoanEntity.getMoney()).thenReturn(BigDecimal.valueOf(1500));
        assertThrows(NotEnoughMoneyException.class, () -> {
            loanService.refundMoney(existingLoanId);
        });

        Mockito.when(mockBorrower.getMoney()).thenReturn(BigDecimal.valueOf(1000));
        Mockito.when(mockLoanEntity.getMoney()).thenReturn(BigDecimal.valueOf(1000));
        assertDoesNotThrow(() -> {
            loanService.refundMoney(existingLoanId);
        });
        verify(personRepository, times(1)).addMoneyToPersonById(anyLong(), any(BigDecimal.class));
        verify(personRepository, times(1)).addMoneyToPersonById(mockedLenderId, BigDecimal.valueOf(1000));
        verify(personRepository, times(1)).addMoneyToPersonById(anyLong(), any(BigDecimal.class));
        verify(personRepository, times(1)).takeMoneyFromPersonById(mockedBorrowerId, BigDecimal.valueOf(1000));
        verify(loanRepository, times(1)).refundMoneyByLoanId(anyLong());
        verify(loanRepository, times(1)).refundMoneyByLoanId(existingLoanId);
    }

}
