package com.titikkoma.taska.dto;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSprintRequestBody {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    @DateTimeFormat
    private String start_date;

    @NotBlank
    @DateTimeFormat
    private String end_date;
}
