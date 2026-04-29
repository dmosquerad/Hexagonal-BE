package com.architecture.hexagonal.infrastructure.inbound.rest.exception;

import com.architecture.hexagonal.infrastructure.contract.rest.user.server.dto.ResponseErrorDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.ResponseErrorDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import jakarta.validation.ConstraintViolationException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
  void createResponseEntity_shouldReturnResponseEntity_whenProblemDetailProvided() {
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

  @Test
  void handleConstraintViolationException_shouldReturn400() {
    final ConstraintViolationException ex = new ConstraintViolationException("constraint violated", Set.of());

    final ResponseEntity<Object> response =
        restControllerExceptionHandler.handleConstraintViolationException(ex, null);

    final ResponseErrorDto expected = ResponseErrorDtoTestDataBuilder.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .detail(ex.getMessage())
        .date(OffsetDateTime.now(TestClock.FIXED_CLOCK))
        .build()
        .responseErrorDto();

    final ResponseEntity<Object> responseExpected = new ResponseEntity<>(
        expected,
        new HttpHeaders(),
        expected.getStatus());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(clock).instant();
  }

  @Test
  void handleNonControlledException_shouldReturn500() {
    final RuntimeException ex = new RuntimeException("unexpected error");

    final ResponseEntity<Object> response =
        restControllerExceptionHandler.handleNonControlledException(ex, null);

    final ResponseErrorDto expected = ResponseErrorDtoTestDataBuilder.builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .title(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .detail(ex.getMessage())
        .date(OffsetDateTime.now(TestClock.FIXED_CLOCK))
        .build()
        .responseErrorDto();

    final ResponseEntity<Object> responseExpected = new ResponseEntity<>(
        expected,
        new HttpHeaders(),
        expected.getStatus());

    AssertionsForClassTypes.assertThat(response)
        .usingRecursiveComparison()
        .isEqualTo(responseExpected);

    Mockito.verify(clock).instant();
  }

}
