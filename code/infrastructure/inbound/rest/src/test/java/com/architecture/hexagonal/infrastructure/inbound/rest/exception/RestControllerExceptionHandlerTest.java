package com.architecture.hexagonal.infrastructure.inbound.rest.exception;

import com.architecture.hexagonal.infrastructure.inbound.rest.dto.ResponseErrorDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.ResponseErrorDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import java.time.Clock;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class RestControllerExceptionHandlerTest {

  @InjectMocks
  RestControllerExceptionHandler restControllerExceptionHandler;

  @Spy
  Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void createResponseEntity() {
    final ResponseErrorDto responseErrorDto = ResponseErrorDtoTestDataBuilder
        .builder()
        .build()
        .responseErrorDto();

    final ResponseEntity<Object> responseExpected = new ResponseEntity<>(
        responseErrorDto,
        new HttpHeaders(),
        responseErrorDto.getStatus());

    final ProblemDetail problemDetail = ProblemDetail.forStatus(responseErrorDto.getStatus());
    problemDetail.setDetail(responseErrorDto.getDetail());

    final ResponseEntity<Object> response = restControllerExceptionHandler.createResponseEntity(
        problemDetail,
        new HttpHeaders(),
        HttpStatusCode.valueOf(responseErrorDto.getStatus()),
        null
    );

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(clock).instant();
  }

}
