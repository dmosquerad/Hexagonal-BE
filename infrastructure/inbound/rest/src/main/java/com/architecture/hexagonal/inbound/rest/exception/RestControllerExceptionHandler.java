package com.architecture.hexagonal.inbound.rest.exception;

import com.architecture.hexagonal.inbound.rest.dto.ResponseErrorDto;
import java.time.Clock;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

  public final Clock clock;

  @Override
  public ResponseEntity<Object> createResponseEntity(@Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
    final ResponseErrorDto responseErrorDto = new ResponseErrorDto();
    responseErrorDto.setDate(OffsetDateTime.now(clock));
    responseErrorDto.setStatus(statusCode.value());

    return new ResponseEntity(responseErrorDto, headers, statusCode);
  }

}
