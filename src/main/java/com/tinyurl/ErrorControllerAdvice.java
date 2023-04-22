package com.tinyurl;

import com.tinyurl.core.data.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ErrorControllerAdvice {
    @Autowired
    private MessageSource messageSource;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(HttpServletRequest request, Exception ex) {
        logger.error(ex.getMessage(), ex);
        HttpStatus status = getStatus(request);
        String msg = ex.getMessage() != null ? ex.getMessage() : "";
        Response response = Response.of(messageSource).error(msg);
        return ResponseEntity.status(status).body(response);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Object statusCode = request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.valueOf((Integer) statusCode);
        }
    }
}
