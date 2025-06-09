package com.titikkoma.taska.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequestFilterParams {
    private String status;
    private String assignee_id;
    private String type;
}
