package com.example.Taxi.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    CustomExceptionStatus customExceptionStatus;
}
