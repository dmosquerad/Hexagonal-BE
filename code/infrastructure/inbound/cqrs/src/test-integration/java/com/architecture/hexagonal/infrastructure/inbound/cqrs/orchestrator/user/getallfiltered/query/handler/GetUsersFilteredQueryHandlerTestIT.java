package com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.getallfiltered.query.handler;

import com.architecture.hexagonal.application.email.getblockedrules.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.application.user.getall.input.GetUsersQuery;
import com.architecture.hexagonal.application.user.getall.usecase.GetAllUsersUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.common.PaginationDto;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.user.GetUsersFilteredQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.mapper.pagination.PaginationMapper;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.orchestrator.user.getallfiltered.query.handler.GetUsersFilteredQueryHandler;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.common.PaginationDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.testutils.data.query.GetUsersFilteredQueryDtoTestDataBuilder;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Collections;

@SpringBootTest(classes = GetUsersFilteredQueryHandler.class)
@ContextConfiguration(classes = TestApplication.class)
class GetUsersFilteredQueryHandlerTestIT {

  @Autowired
  private GetUsersFilteredQueryHandler getUsersFilteredQueryHandler;

  @MockitoSpyBean
  private PaginationMapper paginationMapper;

  @MockitoBean
  private GetAllUsersUseCase getAllUsersUseCase;

  @MockitoBean
  private GetBlockedRulesUseCase getBlockedRulesUseCase;

  @Test
  void getUsersFilteredQueryHandler_shouldReturnPaginationResult_whenBlockEmailIsProvided() throws DomainException {
    final GetUsersFilteredQueryDto queryDto = GetUsersFilteredQueryDtoTestDataBuilder.builder()
        .host("example")
        .blockEmail(true)
        .pagination(PaginationDtoTestDataBuilder.builder().page(1).size(20).build().paginationDto())
        .build()
        .getUsersFilteredQueryDto();
    final EmailBlockRulesVo blockedRules = EmailBlockRulesVo.builder().build();
    final PaginationResult<User> expectedResult = PaginationResult.<User>builder()
        .data(Collections.emptySet())
        .page(1)
        .size(20)
        .totalElements(0L)
        .totalPages(0)
        .build();

    Mockito.when(getBlockedRulesUseCase.execute()).thenReturn(blockedRules);
    Mockito.when(getAllUsersUseCase.execute(ArgumentMatchers.any(GetUsersQuery.class)))
        .thenReturn(expectedResult);

    PaginationResult<User> result = getUsersFilteredQueryHandler.handle(queryDto);

    AssertionsForClassTypes.assertThat(result)
        .isSameAs(expectedResult);

    Mockito.verify(paginationMapper).toPagination(ArgumentMatchers.any(PaginationDto.class));
    Mockito.verify(getBlockedRulesUseCase).execute();
    Mockito.verify(getAllUsersUseCase).execute(ArgumentMatchers.any(GetUsersQuery.class));
  }
}
