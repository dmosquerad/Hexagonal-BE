package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.domain.JavaClass.Predicates;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
class InfrastructureArchTest {

  @ArchTest
  static final ArchRule mappers_should_reside_in_infrastructure =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Mapper")
          .should()
          .resideInAPackage("..infrastructure..")
          .andShould()
          .beAnnotatedWith("org.mapstruct.Mapper")
          .because("Classes named *Mapper must be infrastructure classes annotated with MapStruct @Mapper");

  @ArchTest
  static final ArchRule adapters_should_reside_in_infrastructure_outbound_and_port_on_application =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("AdapterImpl")
          .and()
          .resideInAnyPackage("..infrastructure.outbound..")
          .should().implement(Predicates.resideInAPackage("..application.port.."))
          .because("Outbound classes named *AdapterImpl must implement an application port");

  @ArchTest
  static final ArchRule inbound_entry_points_should_dispatch_through_application_bus =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(RestController.class)
          .or()
          .areAnnotatedWith(Controller.class)
          .or()
          .resideInAPackage("..infrastructure.inbound.handler.kafka.consumer..")
          .or()
          .areAssignableTo(SchedulingConfigurer.class)
          .and()
          .resideInAPackage("..infrastructure.inbound.execution.outbox..")
          .and()
          .areNotInterfaces()
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(
              "..application.port.in.bus.command..",
              "..application.port.in.bus.query..")
          .because("REST controllers, Kafka consumer classes in the consumer package, and outbox SchedulingConfigurer implementations must depend on CommandBus or QueryBus");

}