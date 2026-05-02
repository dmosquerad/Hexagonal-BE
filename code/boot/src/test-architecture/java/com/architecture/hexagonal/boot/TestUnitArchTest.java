package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.OnlyIncludeTests.class)
class TestUnitArchTest {

  @ArchTest
  static final ArchRule unit_tests_should_not_use_spring_boot_test =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Test")
          .should()
          .notBeAnnotatedWith("org.springframework.boot.test.context.SpringBootTest")
          .because("Unit tests must not spin up a Spring context");

}

