package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.domain.JavaClass.Predicates;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.stereotype.Service;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class ApplicationArchTest {

  @ArchTest
  static final ArchRule application_should_not_depend_on_boot =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..application..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..boot..")
          .because("Application must not depend on boot wiring");

  @ArchTest
  static final ArchRule application_should_not_depend_on_infrastructure =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..application..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..infrastructure..")
          .because("Application must not depend on infrastructure");

  @ArchTest
  static final ArchRule use_cases_should_depend_on_domain =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("UseCase")
          .and()
          .resideInAPackage("..application.usecase..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..domain..")
          .because("Use cases must depend on domain types");

  @ArchTest
  static final ArchRule use_cases_should_reside_in_application_layer =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("UseCase")
          .should()
          .resideInAPackage("..application..")
          .because("Use cases must stay in application layer");

  @ArchTest
  static final ArchRule use_cases_should_implement_inbound_ports =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("UseCase")
          .and()
          .resideInAPackage("..application.usecase..")
          .should().implement(Predicates.resideInAPackage("..application.port.in.."))
          .because("Use cases must implement inbound ports");

  @ArchTest
  static final ArchRule use_cases_should_be_annotated_with_service =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("UseCase")
          .and()
          .resideInAPackage("..application.usecase..")
          .should().beAnnotatedWith(org.springframework.stereotype.Service.class)
          .because("Use cases must be annotated with @Service");

  @ArchTest
  static final ArchRule service_can_only_in_application_layer =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(Service.class)
          .should()
          .resideInAPackage("..application.usecase..")
          .because("Only application use cases may be @Service");

  @ArchTest
  static final ArchRule commands_should_reside_in_application_command_package =
          ArchRuleDefinition.classes()
                  .that()
                  .haveSimpleNameEndingWith("Command")
                  .should()
                  .resideInAPackage("..application.cqrs.command.request..")
                  .because("Commands must be in application.cqrs.command.request");

  @ArchTest
  static final ArchRule queries_should_reside_in_application_query_package =
          ArchRuleDefinition.classes()
                  .that()
                  .haveSimpleNameEndingWith("Query")
                  .should()
                  .resideInAPackage("..application.cqrs.query.request..")
                  .because("Queries must be in application.cqrs.query.request");

  @ArchTest
  static final ArchRule ports_should_reside_in_application_port_package =
          ArchRuleDefinition.classes()
                  .that()
                  .haveSimpleNameEndingWith("Port")
                  .should()
                  .resideInAPackage("..application.port..")
                  .because("Ports must be in application.port");

  @ArchTest
  static final ArchRule inbound_ports_should_reside_in_application_port_in =
          ArchRuleDefinition.classes()
                  .that()
                  .haveSimpleNameEndingWith("UseCasePort")
                  .should()
                  .resideInAPackage("..application.port.in..")
                  .because("Inbound use case ports must be in application.port.in");

  @ArchTest
  static final ArchRule outbound_ports_should_reside_in_application_port_out =
          ArchRuleDefinition.classes()
                  .that()
                  .haveSimpleNameEndingWith("Port")
                  .and()
                  .haveSimpleNameNotEndingWith("UseCasePort")
                  .should()
                  .resideInAPackage("..application.port.out..")
                  .because("Outbound ports must be in application.port.out");
}
