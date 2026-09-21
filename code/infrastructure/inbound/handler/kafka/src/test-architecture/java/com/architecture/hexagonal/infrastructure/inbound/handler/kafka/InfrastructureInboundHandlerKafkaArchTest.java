package com.architecture.hexagonal.infrastructure.inbound.handler.kafka;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(
    packages = "com.architecture.hexagonal.infrastructure.inbound.handler.kafka",
    importOptions = ImportOption.DoNotIncludeTests.class)
class InfrastructureInboundHandlerKafkaArchTest {

  private InfrastructureInboundHandlerKafkaArchTest() {}

  @ArchTest
  static final ArchRule consumers_should_reside_in_consumer_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Consumer")
          .should()
          .resideInAPackage("..infrastructure.inbound.handler.kafka.consumer..")
          .because("Kafka consumers must be grouped under the consumer package");

  @ArchTest
  static final ArchRule mappers_should_reside_in_mapper_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Mapper")
          .should()
          .resideInAPackage("..infrastructure.inbound.handler.kafka.mapper..")
          .because("Kafka message mappers must be grouped under the mapper package");
}
