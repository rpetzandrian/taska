package com.titikkoma.taska.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListResponse {
    private String id;
    private String name;
    private String email;
    private String organization_code;
}
