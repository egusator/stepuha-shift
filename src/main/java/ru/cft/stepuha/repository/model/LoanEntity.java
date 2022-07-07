package ru.cft.stepuha.repository.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

public class LoanEntity {
    public enum State {
        STARTED(1),
        BORROWED(2),
        LENDED(3);
        private int value;
        State(int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }

    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="borrower_id", nullable = false)
    private long borrowerId;

    @Column(name="lender_id", nullable = true)
    private long lenderId;

    @Column(name="state", nullable = false)
    private State state;

    @Column(name="money", nullable = false)
    private BigDecimal money;

    @Column(name="creation_time", nullable = false)
    private long creationTime;

    @Column(name="lending_time", nullable = true)
    private long lendingTime;


    @Column(name="refunding_time", nullable = true)
    private long refundingTime;

    public LoanEntity(long id, long borrowerId, long lenderId, State state,
                      BigDecimal money, long creationTime, long lendingTime,
                      long refundingTime) {
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

    public void setId(long id) {
        this.id = id;
    }

    public long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public long getLenderId() {
        return lenderId;
    }

    public void setLenderId(long lenderId) {
        this.lenderId = lenderId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
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

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getLendingTime() {
        return lendingTime;
    }

    public void setLendingTime(long lendingTime) {
        this.lendingTime = lendingTime;
    }

    public long getRefundingTime() {
        return refundingTime;
    }

    public void setRefundingTime(long refundingTime) {
        this.refundingTime = refundingTime;
    }


}
