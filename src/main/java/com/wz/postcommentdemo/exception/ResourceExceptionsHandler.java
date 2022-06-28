package com.wz.postcommentdemo.exception;

import com.wz.postcommentdemo.exception.exception.ResourceNotFoundException;
import com.wz.postcommentdemo.exception.exception.FileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ResourceExceptionsHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(RuntimeException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<String> handleUnableToSaveFileException(FileException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, List<String>> handleValidationException(MethodArgumentNotValidException exception){
        return exception.getBindingResult().getAllErrors()
                .stream()
                .collect(Collectors.toMap(error -> ((FieldError) error).getField(),
                        error -> List.of(error.getDefaultMessage()),
                        (l1, l2) -> Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toList())
                ));
        //return  new ErrorMessage(exception.getBindingResult().getAllErrors().stream().map(error -> error.getDefaultMessage()).toList());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public Map<String, List<String>>  handleValidationException(MissingServletRequestPartException exception){
        Map<String, List<String>> errors = new HashMap<>();
        errors.put(exception.getRequestPartName(), List.of("%s is required".formatted(exception.getRequestPartName())));
        return  errors;
    }
}
