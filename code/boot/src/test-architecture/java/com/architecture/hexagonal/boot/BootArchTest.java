package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class BootArchTest {

  @ArchTest
  static final ArchRule boot_should_not_depend_on_domain_input_or_usecase_impl =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..boot..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..domain.input..", "..application.usecase..")
          .because("Boot must wire modules, not execute use case logic");

  @ArchTest
  static final ArchRule boot_should_only_contain_bootstrap_or_config_types =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..boot..")
          .and()
          .haveSimpleNameNotEndingWith("Test")
          .should()
          .beAnnotatedWith(SpringBootApplication.class)
          .orShould()
          .beAnnotatedWith(Configuration.class)
          .because("Boot package must contain only bootstrap/configuration types");
}
