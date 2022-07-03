package com.ouz.atmsimulation.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExceptionMessage {
    private String message;
    private String dsc;
    private LocalDateTime dateTime;
    private int statusCode;
}
