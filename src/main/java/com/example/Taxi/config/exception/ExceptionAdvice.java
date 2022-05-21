package com.example.Taxi.config.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResDto> customException(CustomException ex) {
        CustomExceptionStatus status = ex.getCustomExceptionStatus();
        ExceptionResDto dto = new ExceptionResDto(status.getErrorCode(), status.getMessage());
        log.warn(status.getMessage());
        return new ResponseEntity(dto,HttpStatus.BAD_REQUEST);
    }

    @Data
    @AllArgsConstructor
    static class ExceptionResDto {
        int errorCode;
        String message;
    }


}
