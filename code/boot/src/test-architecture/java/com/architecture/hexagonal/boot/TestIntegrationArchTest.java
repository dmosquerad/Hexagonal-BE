package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class TestIntegrationArchTest {

  @ArchTest
  static final ArchRule integration_tests_should_be_annotated_with_spring_boot_test =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("TestIT")
          .and()
          .haveSimpleNameNotEndingWith("Test")
          .should()
          .beAnnotatedWith("org.springframework.boot.test.context.SpringBootTest")
          .because("Integration tests must load a Spring context via @SpringBootTest"
              + " to verify real wiring and bean interactions")
          .allowEmptyShould(true);
}

