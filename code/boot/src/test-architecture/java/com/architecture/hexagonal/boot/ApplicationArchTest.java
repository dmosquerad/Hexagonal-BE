package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.domain.JavaClass.Predicates;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

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
          .resideInAPackage("..application.feature..")
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
          .resideInAPackage("..application.feature..")
          .should().implement(Predicates.resideInAPackage("..application.feature..port.."))
          .because("Use cases must implement inbound ports");

  @ArchTest
  static final ArchRule use_cases_should_be_annotated_with_service =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("UseCase")
          .and()
          .resideInAPackage("..application.feature..")
          .should().beAnnotatedWith(org.springframework.stereotype.Service.class)
          .because("Use cases must be annotated with @Service");

  @ArchTest
  static final ArchRule commands_should_reside_in_application_command_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Command")
          .should()
          .resideInAPackage("..application.feature..command..")
          .because("Commands must be in application.feature.{domain}.{feature}.command");

  @ArchTest
  static final ArchRule queries_should_reside_in_application_query_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Query")
          .should()
          .resideInAPackage("..application.feature..query..")
          .because("Queries must be in application.feature.{domain}.{feature}.query");

  @ArchTest
  static final ArchRule ports_should_reside_in_application_port_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Port")
          .should()
          .resideInAnyPackage("..application.port..", "..application.feature..port..")
          .because("Inbound ports must be in application.feature.{domain}.{feature}.port;"
              + " outbound ports must be in application.port.{type}");

  @ArchTest
  static final ArchRule inbound_ports_should_reside_in_application_port_in =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("UseCasePort")
          .should()
          .resideInAPackage("..application.feature..port..")
          .because("Inbound use case ports must be in application.feature.{domain}.{feature}.port");

  @ArchTest
  static final ArchRule outbound_ports_should_reside_in_application_port_out =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Port")
          .and()
          .haveSimpleNameNotEndingWith("UseCasePort")
          .should()
          .resideInAPackage("..application.port..")
          .because("Outbound ports must be in application.port.{type}");

  @ArchTest
  static final ArchRule handler_implementations_should_reside_in_handler_impl =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Handler")
          .and()
          .areNotInterfaces()
          .and()
          .resideInAPackage("..application.feature..")
          .should()
          .resideInAPackage("..application.feature..handler..")
          .because("CQRS handler implementations must be in application.feature.{domain}.{feature}.{command|query}.handler");

  @ArchTest
  static final ArchRule bus_implementations_should_reside_in_dispatcher_impl =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("BusImpl")
          .should()
          .resideInAPackage("..infrastructure.inbound.cqrs.bus..impl..")
          .because("Bus implementations must be in infrastructure.cqrs.bus.{command|query|event}.impl");

  @ArchTest
  static final ArchRule inbound_ports_should_be_annotated_with_validated =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..application.feature..port..")
          .and()
          .areInterfaces()
          .should()
          .beAnnotatedWith("jakarta.validation.Valid")
          .orShould()
          .beAnnotatedWith("org.springframework.validation.annotation.Validated")
          .because("Inbound ports must declare @Validated to enforce input validation"
              + " at the application boundary before reaching the use case");

  @ArchTest
  static final ArchRule command_handler_impls_should_implement_command_handler =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("CommandHandler")
          .and()
          .areNotInterfaces()
          .should()
          .beAssignableTo(
              "com.architecture.hexagonal.application.common.cqrs.command.CommandHandler")
          .because("Command handler implementations must implement the CommandHandler"
              + " interface to guarantee a uniform CQRS dispatch contract");

  @ArchTest
  static final ArchRule query_handler_impls_should_implement_query_handler =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("QueryHandler")
          .and()
          .areNotInterfaces()
          .should()
          .beAssignableTo(
              "com.architecture.hexagonal.application.common.cqrs.query.QueryHandler")
          .because("Query handler implementations must implement the QueryHandler"
              + " interface to guarantee a uniform CQRS dispatch contract");

  @ArchTest
  static final ArchRule commands_should_be_immutable =
      ArchRuleDefinition.fields()
          .that()
          .areDeclaredInClassesThat()
          .haveSimpleNameEndingWith("Command")
          .and()
          .areNotStatic()
          .should()
          .beFinal()
          .andShould()
          .bePrivate()
          .because("Command fields must be private final to guarantee immutability;"
              + " Commands are write-once messages that must not mutate during the CQRS dispatch chain");

  @ArchTest
  static final ArchRule queries_should_be_immutable =
      ArchRuleDefinition.fields()
          .that()
          .areDeclaredInClassesThat()
          .haveSimpleNameEndingWith("Query")
          .and()
          .areNotStatic()
          .should()
          .beFinal()
          .andShould()
          .bePrivate()
          .because("Query fields must be private final to guarantee immutability;"
              + " Queries are read-only request objects that must not mutate during the CQRS dispatch chain");

  @ArchTest
  static final ArchRule ports_should_be_interfaces =
      ArchRuleDefinition.classes()
          .that()
          .resideInAnyPackage("..application.port..", "..application.feature..port..")
          .should()
          .beInterfaces()
          .because("Ports define contracts between layers;"
              + " they must be interfaces so that adapters can be swapped without touching the application core");

  @ArchTest
  static final ArchRule command_handlers_should_be_annotated_with_component =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("CommandHandler")
          .and()
          .areNotInterfaces()
          .should()
          .beAnnotatedWith("org.springframework.stereotype.Component")
          .because("Command handlers are infrastructure wiring components, not business services;"
              + " @Component is the correct stereotype — @Service is reserved for use cases");

  @ArchTest
  static final ArchRule query_handlers_should_be_annotated_with_component =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("QueryHandler")
          .and()
          .areNotInterfaces()
          .should()
          .beAnnotatedWith("org.springframework.stereotype.Component")
          .because("Query handlers are infrastructure wiring components, not business services;"
              + " @Component is the correct stereotype — @Service is reserved for use cases");
}
