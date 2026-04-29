package com.architecture.hexagonal.infrastructure.inbound.rest.exception;

import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.ResponseErrorDto;
import jakarta.validation.ConstraintViolationException;
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
  public ResponseEntity<Object> createResponseEntity(
      @Nullable final Object body,
      final HttpHeaders headers,
      final HttpStatusCode statusCode,
      final WebRequest request) {
    final ResponseErrorDto responseErrorDto = new ResponseErrorDto();
    responseErrorDto.setDate(OffsetDateTime.now(clock));
    responseErrorDto.setStatus(statusCode.value());

    if (statusCode instanceof HttpStatus status) {
      responseErrorDto.setTitle(status.getReasonPhrase());
    }

    if (Objects.nonNull(body) && body instanceof ProblemDetail problem) {
      responseErrorDto.setDetail(problem.getDetail());
    }

    return new ResponseEntity<>(responseErrorDto, headers, statusCode);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationException(
      final ConstraintViolationException ex, final WebRequest request) {
    final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setDetail(ex.getMessage());
    return this.handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleNonControlledException(final Exception ex, final WebRequest request) {
    final ProblemDetail problemDetail = ProblemDetail.forStatus( HttpStatus.INTERNAL_SERVER_ERROR);
    problemDetail.setDetail(ex.getMessage());
    return this.handleExceptionInternal(ex, problemDetail, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
}
