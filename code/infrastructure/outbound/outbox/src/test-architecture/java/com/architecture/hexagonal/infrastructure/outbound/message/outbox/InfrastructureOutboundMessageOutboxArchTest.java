package com.architecture.hexagonal.infrastructure.outbound.message.outbox;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@AnalyzeClasses(
    packages = "com.architecture.hexagonal.infrastructure.outbound.message.outbox",
    importOptions = ImportOption.DoNotIncludeTests.class)
class InfrastructureOutboundMessageOutboxArchTest {

  private InfrastructureOutboundMessageOutboxArchTest() {}

  @ArchTest
  static final ArchRule services_should_reside_in_service_package =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(Service.class)
          .should()
          .resideInAPackage("..infrastructure.outbound.message.outbox.service..")
          .because("Spring service beans must be grouped under the service package");

  @ArchTest
  static final ArchRule repositories_should_reside_in_adapter_package =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(Repository.class)
          .should()
          .resideInAPackage("..infrastructure.outbound.message.outbox.adapter..")
          .because("Outbound port adapters must be grouped under the adapter package");

  @ArchTest
  static final ArchRule mappers_should_reside_in_mapper_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Mapper")
          .should()
          .resideInAPackage("..infrastructure.outbound.message.outbox.mapper..")
          .because("MapStruct mapper interfaces must be grouped under the mapper package");

  @ArchTest
  static final ArchRule config_should_reside_in_config_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Config")
          .should()
          .resideInAPackage("..infrastructure.outbound.message.outbox.config..")
          .because("Configuration classes must be grouped under the config package");
}
