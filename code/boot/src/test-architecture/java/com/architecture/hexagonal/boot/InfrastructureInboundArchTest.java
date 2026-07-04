package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
class InfrastructureInboundArchTest {

  @ArchTest
  static final ArchRule controller_should_reside_in_inbound =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("ControllerImpl")
          .should()
          .resideInAPackage("..infrastructure.inbound..")
          .because("Controllers must be in infrastructure.inbound");

  @ArchTest
  static final ArchRule controller_and_handlers_should_depend_on_inbound_ports =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("ControllerImpl")
          .and()
          .haveSimpleNameNotEndingWith("HandlerImpl")
          .and()
          .resideInAPackage("..infrastructure.inbound..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..infrastructure.inbound.orchestration..")
          .because("Controllers must depend on inbound ports or Orchestration request types");

  @ArchTest
  static final ArchRule rest_controller_advice_should_reside_in_inbound =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(RestControllerAdvice.class)
          .should()
          .resideInAPackage("..infrastructure.inbound..")
          .because("@RestControllerAdvice classes must be in inbound layer");

}
