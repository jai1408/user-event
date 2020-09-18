package com.syf.develop.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.syf.develop.model.DebugInfo;
import com.syf.develop.model.ErrorResponse;
import com.syf.develop.model.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class UserEventExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String REQUEST_TRACKING_ID = "requestTrackingId";
    Logger logger = LoggerFactory.getLogger(UserEventExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("{\"exception\"\"{}\",{\"stackTrace\"\"{}\"}", ex.getClass().getSimpleName(), ex.getMessage());
        List<Errors> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> {
            Errors error = new Errors();
            error.setField(x.getField());
            error.setLocation("body");
            error.setReason(x.getDefaultMessage());
            return error;
        }).collect(Collectors.toList());
        ErrorResponse errorResponse = createBadRequestError();
        errorResponse.setErrors(errors);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = createInternalServerError();
        List<Errors> errors = new ArrayList<>();
        Errors error = new Errors();
        error.setReason(ex.getCause().toString());
        error.setLocation("body");
        error.setField(ex.getLocalizedMessage());
        errors.add(error);
        errorResponse.setErrors(errors);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }

    @ExceptionHandler(UserEventException.class)
    public ResponseEntity<Object> handleUserEventException(UserEventException ex) {
        ErrorResponse errorResponse = createInternalServerError();
        List<Errors> errors = new ArrayList<>();
        Errors error = new Errors();
        error.setReason(ex.getUserEventError().getText());
        error.setLocation("body");
        error.setField(ex.getLocalizedMessage());
        errors.add(error);
        errorResponse.setErrors(errors);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = createInternalServerError();
        List<Errors> errors = new ArrayList<>();
        Errors error = new Errors();
        error.setReason(ex.getCause().toString());
        error.setLocation("body");
        error.setField(ex.getLocalizedMessage());
        errors.add(error);
        errorResponse.setErrors(errors);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex) {
        ErrorResponse errorResponse = createInternalServerError();
        List<Errors> errors = new ArrayList<>();
        Errors error = new Errors();
        error.setReason(ex.getCause().toString());
        error.setLocation("body");
        error.setField(ex.getLocalizedMessage());
        errors.add(error);
        errorResponse.setErrors(errors);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(errorResponse);
    }

    private ErrorResponse createInternalServerError() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode("userEvent.application.internalServerError");
        errorResponse.setMessage("Internal Server Error");
        errorResponse.setDocumentationUrl("https://github.com/jai1408");
        errorResponse.setRemediationInfo("Your request has invalid data or missing parameters. Please verify and resubmit.");
        errorResponse.setTrackingId("673654dce3-dhg6-hds4s2");
        errorResponse.setTransientType(false);
        List<DebugInfo> debugInfo = new ArrayList<>();

        debugInfo.add(DebugInfo.builder().type("validation Error").reason("Invalid Data").remediationMessage("Please Correct the input fields").build());

        errorResponse.setDebugInfo(debugInfo);
        return errorResponse;
    }

    private ErrorResponse createBadRequestError() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode("userEvent.application.badRequest");
        errorResponse.setMessage("BAD Request");
        errorResponse.setDocumentationUrl("https://github.com/jai1408");
        errorResponse.setRemediationInfo("Your request has invalid data or missing parameters. Please verify and resubmit.");
        errorResponse.setTrackingId("673654dce3-dhg6-hds4s2");
        errorResponse.setTransientType(false);
        List<DebugInfo> debugInfo = new ArrayList<>();

        debugInfo.add(DebugInfo.builder().type("validation Error").reason("Invalid Data").remediationMessage("Please Correct the input fields").build());

        errorResponse.setDebugInfo(debugInfo);
        return errorResponse;
    }
}
