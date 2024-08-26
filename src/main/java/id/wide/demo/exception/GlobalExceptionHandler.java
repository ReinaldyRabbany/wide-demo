package id.wide.demo.exception;

import id.wide.demo.dto.response.RestResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.net.ssl.SSLHandshakeException;
import java.util.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            Exception.class,
            RuntimeException.class,
            ResourceAccessException.class,
            SSLHandshakeException.class,
    })
    public ResponseEntity<RestResponse> defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error(e.getMessage(), e);

        RestResponse errorResponse = RestResponse.builder()
                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(value = {
            NoResourceFoundException.class,
            NoSuchElementException.class,
            EntityNotFoundException.class
    })
    public ResponseEntity<RestResponse> dataNotFound(HttpServletRequest req, Exception e) {
        log.error(e.getMessage(), e);

        RestResponse errorResponse = RestResponse.builder()
                .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .message(HttpStatus.NOT_FOUND.getReasonPhrase())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
    }

    @ExceptionHandler(value = {
            BadRequestException.class,
            MissingRequestHeaderException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class,
            HandlerMethodValidationException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<RestResponse> badRequest(HttpServletRequest req, Exception e) {
        Map<String, Object> data = new HashMap<>();
        if (e instanceof MethodArgumentNotValidException err) {
            Map<String, Object> finalData = data;
            err.getBindingResult()
                    .getFieldErrors()
                    .forEach(ex -> {
                        List<String> messages = (finalData.containsKey(ex.getField()))
                                ? (ArrayList<String>) finalData.get(ex.getField()) : new ArrayList<>();
                        messages.add(ex.getDefaultMessage());
                        finalData.put(ex.getField(), messages);
                    });

        } else if (e instanceof BadRequestException err) {
            data = simpleMessage(err.getMessage());
        } else if (e instanceof MissingRequestHeaderException err) {
            data = simpleMessage(err.getMessage());
        }
        RestResponse errorResponse = RestResponse.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .data(data.isEmpty() ? null : data)
                .build();
        return ResponseEntity.ok().body(errorResponse);
    }

    private Map<String, Object> simpleMessage(String message) {
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("error", message);
        return messageMap;
    }

    @ExceptionHandler({
            ProductAvailabilityException.class
    })
    public ResponseEntity<RestResponse> handleProductAvailabilityException(
            HttpServletRequest req, Exception e) {
        RestResponse errorResponse = RestResponse.builder()
                .code("PRODUCT_NOT_AVAILABLE")
                .message((e.getMessage()!=null) ? e.getMessage()
                        : "Product does not exists or currently not available")
                .build();
        return ResponseEntity.ok().body(errorResponse);
    }
}
