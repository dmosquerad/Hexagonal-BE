package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class TestUnitArchTest {

  @ArchTest
  static final ArchRule unit_tests_should_not_use_spring_boot_test =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Test")
          .and()
          .haveSimpleNameNotEndingWith("TestIT")
          .should()
          .notBeAnnotatedWith("org.springframework.boot.test.context.SpringBootTest")
          .because("Unit tests must not spin up a Spring context;"
              + " use @SpringBootTest only in *TestIT classes");

  @ArchTest
  static final ArchRule domain_tests_should_not_depend_on_spring =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("org.springframework..")
          .because("Domain tests must be free of Spring dependencies"
              + " to keep domain logic pure and independently verifiable");

  @ArchTest
  static final ArchRule use_case_unit_tests_should_use_mockito_extension =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("UseCaseTest")
          .should()
          .beAnnotatedWith("org.junit.jupiter.api.extension.ExtendWith")
          .because("Use case unit tests must declare @ExtendWith(MockitoExtension.class)"
              + " to manage mock lifecycle without loading a Spring context")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule inbound_unit_tests_should_use_mockito_extension =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("ControllerTest")
          .or()
          .haveSimpleNameEndingWith("HandlerTest")
          .should()
          .beAnnotatedWith("org.junit.jupiter.api.extension.ExtendWith")
          .because("Inbound unit tests must declare @ExtendWith(MockitoExtension.class)"
              + " to manage mock lifecycle without loading a Spring context")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule outbound_unit_tests_should_use_mockito_extension =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("AdapterTest")
          .should()
          .beAnnotatedWith("org.junit.jupiter.api.extension.ExtendWith")
          .because("Outbound unit tests must declare @ExtendWith(MockitoExtension.class)"
              + " to manage mock lifecycle without loading a Spring context")
          .allowEmptyShould(true);}

