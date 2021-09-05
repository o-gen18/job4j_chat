package ru.job4j.chat.handlers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.PropertyValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class.getSimpleName());

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private void genericExceptionHandler(Exception e,
                                         HttpServletRequest req,
                                         HttpServletResponse resp,
                                         int httpStatus,
                                         String message) throws IOException {
        resp.setStatus(httpStatus);
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {
            {
                put("message", message);
                put("details", e.getMessage());
                put("type", e.getClass());
            }
        }));
        LOGGER.error(e.getMessage());
    }

    @ExceptionHandler(value = {NullPointerException.class, HibernateException.class})
    public void handleInputValidationException(Exception e, HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        genericExceptionHandler(e, req, resp, HttpStatus.BAD_REQUEST.value(),
                "Some input occurred to be empty!");
    }

    @ExceptionHandler(value = {TokenExpiredException.class})
    public void handleAuthorizationException(Exception e, HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        genericExceptionHandler(e, req, resp, HttpStatus.UNAUTHORIZED.value(),
                "A problem occurred with JWT authorization!");
    }
}
