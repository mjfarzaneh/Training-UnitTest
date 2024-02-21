package com.training.unittest.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ResponseDto {
    private HttpStatus code;
    private String errorCode;
    private Object response;
}
