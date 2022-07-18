package ru.cft.stepuha.repository.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

public class LoanEntity {
 

    private Long id;

    private Long borrowerId;

    private Long lenderId;

    private Integer state;

    private BigDecimal money;

    private Long creationTime;

    private Long lendingTime;

    private Long refundingTime;

    public LoanEntity(Long id, Long borrowerId, Long lenderId, Integer state,
                      BigDecimal money, Long creationTime, Long lendingTime,
                      Long refundingTime) {
        this.id = id;
        this.borrowerId = borrowerId;
        this.lenderId = lenderId;
        this.state = state;
        this.money = money;
        this.creationTime = creationTime;
        this.lendingTime = lendingTime;
        this.refundingTime = refundingTime;
    }

    public LoanEntity() {}

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public long getLenderId() {
        return lenderId;
    }

    public void setLenderId(Long lenderId) {
        this.lenderId = lenderId;
    }

    public int getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public long getLendingTime() {
        return lendingTime;
    }

    public void setLendingTime(Long lendingTime) {
        this.lendingTime = lendingTime;
    }

    public long getRefundingTime() {
        return refundingTime;
    }

    public void setRefundingTime(Long refundingTime) {
        this.refundingTime = refundingTime;
    }


}
