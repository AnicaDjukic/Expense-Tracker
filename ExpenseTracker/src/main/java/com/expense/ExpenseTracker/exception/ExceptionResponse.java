package com.expense.ExpenseTracker.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExceptionResponse {

    private String message;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateTime;
}
