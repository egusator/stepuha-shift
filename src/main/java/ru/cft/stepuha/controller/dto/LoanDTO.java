package ru.cft.stepuha.controller.dto;

import ru.cft.stepuha.controller.errors.AppError;
import ru.cft.stepuha.repository.model.LoanEntity;

import java.util.List;

public class LoanDTO {
    private String status;
    private List<LoanEntity> loanList;


    public LoanDTO(String status, List<LoanEntity> loanList) {
        this.status = status;
        this.loanList = loanList;

    }

    public List<LoanEntity> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<LoanEntity> loanList) {
        this.loanList = loanList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
