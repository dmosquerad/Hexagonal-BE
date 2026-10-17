package com.architecture.hexagonal.application.testutils.usecase.common;

import com.architecture.hexagonal.contract.bus.model.common.Pagination;
import lombok.Builder;

@Builder
public class PaginationTestDataBuilder {

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    public Pagination pagination() {
        final Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setSize(size);

        return pagination;
    }
}
