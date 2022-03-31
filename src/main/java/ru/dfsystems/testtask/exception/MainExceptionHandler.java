package ru.dfsystems.testtask.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class MainExceptionHandler {

    @ExceptionHandler(MainServiceException.class)
    @ResponseBody
    public ResponseEntity<ErrorDto> handleMainServiceException(MainServiceException mainEx) {
        return ResponseEntity.badRequest().body(new ErrorDto(mainEx.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorDto> handleOtherException(Exception ex) {
        return ResponseEntity.internalServerError().body(new ErrorDto(ex.getMessage()));
    }
}
