package ru.cft.stepuha.controller.dto;

import ru.cft.stepuha.controller.errors.AppError;

public class ErrorDTO {
    private String status;
    private AppError error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AppError getError() {
        return error;
    }

    public void setError(AppError error) {
        this.error = error;
    }

    public ErrorDTO(String status, AppError error) {
        this.status = status;
        this.error = error;
    }
}
