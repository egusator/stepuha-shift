package ru.cft.stepuha.controller.dto;

import ru.cft.stepuha.repository.model.LoanAndPersonEntity;
import ru.cft.stepuha.repository.model.PersonEntity;

import java.util.ArrayList;
import java.util.List;

public class LoanAndPersonDTO {
    private String status;
    private List<LoanAndPersonEntity> loanAndPersonList;

    public LoanAndPersonDTO(String status, List<LoanAndPersonEntity> loanAndPersonList) {
        this.status = status;
        this.loanAndPersonList = loanAndPersonList;

    }

    public LoanAndPersonDTO(String status, LoanAndPersonEntity loanAndPerson) {
        this.status = status;
        this.loanAndPersonList = new ArrayList<LoanAndPersonEntity>();
        this.loanAndPersonList.add(loanAndPerson);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LoanAndPersonEntity> getLoanAndPersonList() {
        return loanAndPersonList;
    }

    public void setLoanAndPersonList(List<LoanAndPersonEntity> loanAndPersonList) {
        this.loanAndPersonList = loanAndPersonList;
    }
}
