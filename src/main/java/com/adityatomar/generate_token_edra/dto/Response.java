package com.adityatomar.generate_token_edra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class Response {

    private final HttpStatus status;
    private final String Message;
    private final Object response;
}
