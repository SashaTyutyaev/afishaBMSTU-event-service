package ru.afishaBMSTU.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private HttpStatus status;
    private String description;
    private String timestamp;
}
