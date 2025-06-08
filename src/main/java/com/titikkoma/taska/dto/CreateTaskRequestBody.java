package com.titikkoma.taska.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreateTaskRequestBody {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String sprint_id;

    private Integer priority;

    private Integer story_point;

    @NotBlank
    private String type;

    private String assignee_id;
}
