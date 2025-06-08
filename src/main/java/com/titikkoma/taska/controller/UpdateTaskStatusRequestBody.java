package com.titikkoma.taska.controller;

import jakarta.validation.constraints.NotBlank;

public class UpdateTaskStatusRequestBody {


    @NotBlank(message = "Status must not be blank")
    private String status;

    public UpdateTaskStatusRequestBody() {}
    public UpdateTaskStatusRequestBody(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}