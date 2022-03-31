package ru.dfsystems.testtask.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ErrorDto {

    private LocalDateTime dateTime;
    private String error;

    public ErrorDto(String error) {
        this.error = error;
    }
}
