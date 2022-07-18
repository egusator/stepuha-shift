
package ru.cft.stepuha.repository.model;

import javax.persistence.*;
import java.math.BigDecimal;

public class PersonEntity {


    private Long id;

    private String firstName;


    private String lastName;


    private String middleName;


    private String login;


    private BigDecimal money;

    public PersonEntity(long id, String firstName, String lastName, String middleName, String login, BigDecimal money) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.login = login;
        this.money = money;
    }

    public PersonEntity() {

    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }



}