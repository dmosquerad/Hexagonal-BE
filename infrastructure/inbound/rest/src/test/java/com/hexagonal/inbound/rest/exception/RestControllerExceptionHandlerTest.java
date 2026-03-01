package com.hexagonal.inbound.rest.exception;

import com.architecture.hexagonal.inbound.rest.dto.ResponseErrorDto;
import com.architecture.hexagonal.inbound.rest.exception.RestControllerExceptionHandler;
import com.hexagonal.stub.InstantStub;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class RestControllerExceptionHandlerTest {

  @InjectMocks
  RestControllerExceptionHandler restControllerExceptionHandler;

  @Spy
  Clock clock = Clock.fixed(InstantStub.INSTANT_DATE,  ZoneOffset.UTC);

  @Test
  void createResponseEntity() {
    final ResponseErrorDto responseErrorDto = new ResponseErrorDto();
    responseErrorDto.setDate(OffsetDateTime.ofInstant(InstantStub.INSTANT_DATE, ZoneOffset.UTC));
    responseErrorDto.setStatus(HttpStatus.BAD_REQUEST.value());

    final ResponseEntity<Object> responseExpected =
        new ResponseEntity<>(responseErrorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST);

    final ResponseEntity<Object> response = restControllerExceptionHandler.createResponseEntity(
        null,
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        null
    );

    Assertions.assertEquals(responseExpected, response);

    Mockito.verify(clock).instant();
  }

}
