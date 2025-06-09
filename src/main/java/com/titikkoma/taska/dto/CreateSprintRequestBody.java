package com.titikkoma.taska.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSprintRequestBody {
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

