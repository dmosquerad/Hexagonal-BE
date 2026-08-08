package com.architecture.hexagonal.infrastructure.inbound.rest.exception;

import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.exception.ResourceNotFoundException;
import com.architecture.hexagonal.infrastructure.inbound.contract.rest.user.server.dto.ResponseErrorDto;
import jakarta.validation.ConstraintViolationException;
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

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handleIllegalArgumentException(
      IllegalArgumentException ex, WebRequest request) {
    final ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    return this.handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFoundException(
      ResourceNotFoundException ex, WebRequest request) {
    final ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    return this.handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationException(
      final ConstraintViolationException ex, final WebRequest request) {
    final ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    return this.handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<Object> handleDomainException(DomainException ex, WebRequest request) {
    final ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    return this.handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleNonControlledException(
      final Exception ex, final WebRequest request) {
    final ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    return this.handleExceptionInternal(
        ex, problemDetail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
}
