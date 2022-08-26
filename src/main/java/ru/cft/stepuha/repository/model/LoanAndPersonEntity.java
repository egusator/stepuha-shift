package ru.cft.stepuha.repository.model;

public class LoanAndPersonEntity {
    private LoanEntity loan;
    private PersonEntity person;

    public LoanAndPersonEntity(LoanEntity loan, PersonEntity person) {
        this.loan = loan;
        this.person = person;
    }

    public LoanEntity getLoan() {
        return loan;
    }

    public void setLoan(LoanEntity loan) {
        this.loan = loan;
    }

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }
}
