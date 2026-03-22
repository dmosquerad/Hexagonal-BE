package com.architecture.hexagonal.infrastructure.inbound.rest.factory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseFactory {

  public static ErrorResponseException of(final HttpStatus status, final Exception e) {
    final ProblemDetail problemDetail = ProblemDetail.forStatus(status);
    problemDetail.setDetail(e.getMessage());
    return new ErrorResponseException(status, problemDetail, e);
  }
}
