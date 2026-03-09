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
  static final ArchRule application_should_not_depend_on_infrastructure =
      ArchRuleDefinition.noClasses()
          .that().resideInAPackage("..application..")
          .should().dependOnClassesThat()
          .resideInAnyPackage("..infrastructure..")
          .because("Application layer must be independent from infrastructure layer");

  @ArchTest
  static final ArchRule use_cases_should_depend_on_domain =
      ArchRuleDefinition.classes()
          .that().haveSimpleNameEndingWith("UseCase")
          .and().resideInAPackage("..application.usecase..")
          .should().dependOnClassesThat()
          .resideInAnyPackage("..domain..")
          .because("Use cases must depend on domain ports and entities");

  @ArchTest
  static final ArchRule use_cases_should_reside_in_application_layer =
      ArchRuleDefinition.classes()
          .that().haveSimpleNameEndingWith("UseCase")
          .should().resideInAPackage("..application..")
          .because("Use cases belong to the application layer");

  @ArchTest
  static final ArchRule use_cases_should_implement_inbound_ports =
      ArchRuleDefinition.classes()
          .that().haveSimpleNameEndingWith("UseCase")
          .and().resideInAPackage("..application.usecase..")
          .should().implement(Predicates.resideInAPackage("..domain.port.in.."))
          .because("Use cases must implement the inbound ports defined in the domain");

  @ArchTest
  static final ArchRule use_cases_should_be_annotated_with_service =
      ArchRuleDefinition.classes()
          .that().haveSimpleNameEndingWith("UseCase")
          .and().resideInAPackage("..application.usecase..")
          .should().beAnnotatedWith(org.springframework.stereotype.Service.class)
          .because("Use cases must be registered as Spring @Service beans for dependency injection");

  @ArchTest
  static final ArchRule service_can_only_in_application_layer =
      ArchRuleDefinition.classes()
          .that().areAnnotatedWith(Service.class)
          .should()
          .resideInAPackage("..application.usecase..");
}
