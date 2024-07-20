package com.adityatomar.generate_token_edra.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Data
public class Token {
    private String id;
    private Date createdAt;
    private Date blockedAt;
    private boolean isBlocked;
}
