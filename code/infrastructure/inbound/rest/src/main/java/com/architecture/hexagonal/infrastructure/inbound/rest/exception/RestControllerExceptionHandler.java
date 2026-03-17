package com.architecture.hexagonal.infrastructure.inbound.rest.exception;

import com.architecture.hexagonal.infrastructure.inbound.rest.dto.ResponseErrorDto;
import java.sql.SQLException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

  private final Clock clock;

  @Override
  public ResponseEntity<Object> createResponseEntity(@Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
    final ResponseErrorDto responseErrorDto = new ResponseErrorDto();
    responseErrorDto.setDate(OffsetDateTime.now(clock));
    responseErrorDto.setStatus(statusCode.value());

    if (statusCode instanceof HttpStatus status) {
      responseErrorDto.setTitle(status.getReasonPhrase());
    }

    if (Objects.nonNull(body) && body instanceof ProblemDetail problem) {
      responseErrorDto.setDetail(problem.getDetail());
    }

    return new ResponseEntity(responseErrorDto, headers, statusCode);
  }

  @ExceptionHandler(SQLException.class)
  public ResponseEntity<Object> handleSQLException(SQLException ex, WebRequest request) {
    return this.handleExceptionInternal(ex, ex.getMessage(), null, HttpStatusCode.valueOf(500), request);
  }
}
