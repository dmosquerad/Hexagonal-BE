package com.architecture.hexagonal.application.bus.query;

import java.util.List;

import com.architecture.hexagonal.application.bus.query.impl.QueryBusImpl;
import com.architecture.hexagonal.application.handler.query.QueryHandler;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class QueryBusTest {

  @Test
  void shouldDispatchQueryToRegisteredHandler() throws Exception {
    final QueryHandler<TestQuery, String> handler = new QueryHandler<>() {
      @Override
      public Class<TestQuery> getQueryType() {
        return TestQuery.class;
      }

      @Override
      public String handle(final TestQuery query) {
        return query.value;
      }
    };

    final QueryBus queryBus = new QueryBusImpl(List.of(handler));
    final String result = queryBus.execute(new TestQuery("hello"));

    assertThat(result).isEqualTo("hello");
  }

  private static final class TestQuery {

    private final String value;

    private TestQuery(final String value) {
      this.value = value;
    }
  }
}
