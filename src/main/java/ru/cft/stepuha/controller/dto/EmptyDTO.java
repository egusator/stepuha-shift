package ru.cft.stepuha.controller.dto;

public class EmptyDTO {
    private String status;

    public EmptyDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
