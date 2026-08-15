package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.getallfiltered;

import com.architecture.hexagonal.application.business.email.getblockedrules.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.application.business.user.getall.input.GetUsersInput;
import com.architecture.hexagonal.application.business.user.getall.usecase.GetAllUsersUseCase;
import com.architecture.hexagonal.domain.model.aggregate.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.common.PaginationDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.GetUsersFilteredQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.config.transaction.TransactionBoundary;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.pagination.PaginationMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.common.PaginationDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.GetUsersFilteredQueryDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.transaction.TransactionBoundaryTest;
import java.util.Collections;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUsersFilteredQueryHandlerImplTest {

  @InjectMocks private GetUsersFilteredQueryHandlerImpl queryHandler;

  @Mock private GetAllUsersUseCase getAllUsersUseCase;

  @Mock private GetBlockedRulesUseCase getBlockedRulesUseCase;

  @Spy private PaginationMapper paginationMapper = Mappers.getMapper(PaginationMapper.class);

  @Spy private TransactionBoundary transactionBoundary = new TransactionBoundaryTest();

  @Test
  void handle_shouldReturnPaginationResult_whenBlockEmailIsNull() {
    final PaginationResult<User> expectedResult =
        PaginationResult.<User>builder()
            .data(Collections.emptyList())
            .page(0)
            .size(10)
            .totalElements(0L)
            .totalPages(0)
            .build();

    GetUsersFilteredQueryDto queryDto =
        GetUsersFilteredQueryDtoTestDataBuilder.builder()
            .host("example")
            .blockEmail(null)
            .pagination(null)
            .build()
            .getUsersFilteredQueryDto();
    Mockito.when(getAllUsersUseCase.execute(ArgumentMatchers.any(GetUsersInput.class)))
        .thenReturn(expectedResult);

    PaginationResult<User> result = queryHandler.handle(queryDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);

    Mockito.verify(paginationMapper).toPagination((PaginationDto) ArgumentMatchers.isNull());
    Mockito.verify(getAllUsersUseCase).execute(ArgumentMatchers.any(GetUsersInput.class));
    Mockito.verify(getBlockedRulesUseCase, Mockito.never()).execute();
    Mockito.verify(transactionBoundary).read(Mockito.any());
  }

  @Test
  void handle_shouldReturnPaginationResult_whenBlockEmailIsProvided() {
    final EmailBlockRulesVo blockedRules = EmailBlockRulesVo.builder().build();
    final PaginationResult<User> expectedResult =
        PaginationResult.<User>builder()
            .data(Collections.emptyList())
            .page(1)
            .size(20)
            .totalElements(0L)
            .totalPages(0)
            .build();

    GetUsersFilteredQueryDto queryDto =
        GetUsersFilteredQueryDtoTestDataBuilder.builder()
            .host("example")
            .blockEmail(true)
            .pagination(
                PaginationDtoTestDataBuilder.builder().page(1).size(20).build().paginationDto())
            .build()
            .getUsersFilteredQueryDto();
    Mockito.when(getBlockedRulesUseCase.execute()).thenReturn(blockedRules);
    Mockito.when(getAllUsersUseCase.execute(ArgumentMatchers.any(GetUsersInput.class)))
        .thenReturn(expectedResult);

    PaginationResult<User> result = queryHandler.handle(queryDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);

    Mockito.verify(paginationMapper).toPagination(ArgumentMatchers.any(PaginationDto.class));
    Mockito.verify(getBlockedRulesUseCase).execute();
    Mockito.verify(getAllUsersUseCase).execute(ArgumentMatchers.any(GetUsersInput.class));
    Mockito.verify(transactionBoundary, Mockito.times(2)).read(Mockito.any());
  }
}
