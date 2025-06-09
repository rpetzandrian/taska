package com.titikkoma.taska.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTaskRequestBody {
    private String name;
    private String description;
    private String sprint_id;
    private Integer priority;
    private Integer story_point;
    private String type;
    private String assignee_id;
    private String status;
}
