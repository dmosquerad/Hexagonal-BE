package com.architecture.hexagonal.application;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal.application", importOptions = ImportOption.DoNotIncludeTests.class)
class ApplicationArchTest {

  @ArchTest
  static final ArchRule application_should_not_depend_on_spring =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..application..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("org.springframework..")
          .because("Application must not depend on Spring");

  @ArchTest
  static final ArchRule application_should_not_depend_on_boot_or_infrastructure =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..application..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..boot..", "..infrastructure..")
          .because("Application must not depend on boot or infrastructure");

  @ArchTest
  static final ArchRule input_types_should_reside_in_application_input_package =
      ArchRuleDefinition.classes()
          .that(
              DescribedPredicate.or(
                  DescribedPredicate.describe("class name ends with Command", clazz -> clazz.getSimpleName().endsWith("Command")),
                  DescribedPredicate.describe("class name ends with Query", clazz -> clazz.getSimpleName().endsWith("Query")),
                  DescribedPredicate.describe("class name ends with Event", clazz -> clazz.getSimpleName().endsWith("Event"))
              )
          )
          .should()
          .resideInAPackage("..application..input..")
          .because("Inputs must be in application.{domain}.{feature}.input");

  @ArchTest
  static final ArchRule input_fields_should_be_immutable =
      ArchRuleDefinition.fields()
          .that()
          .areDeclaredInClassesThat(
              DescribedPredicate.or(
                  DescribedPredicate.describe("class name ends with Command", clazz -> clazz.getSimpleName().endsWith("Command")),
                  DescribedPredicate.describe("class name ends with Query", clazz -> clazz.getSimpleName().endsWith("Query")),
                  DescribedPredicate.describe("class name ends with Event", clazz -> clazz.getSimpleName().endsWith("Event"))
              )
          )
          .and()
          .areNotStatic()
          .should()
          .beFinal()
          .andShould()
          .bePrivate()
          .because("Inputs must be private final to guarantee immutability");

    @ArchTest
    static final ArchRule use_cases_should_get_interfaces =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("UseCase")
            .and()
            .resideInAPackage("..application..usecase..")
            .should()
            .beInterfaces()
            .because("Use cases interfaces should be defined");

    @ArchTest
    static final ArchRule use_case_impl_should_implement_use_case_interface =
        ArchRuleDefinition.classes()
            .that()
            .resideInAPackage("..application..usecase.impl..")
            .and()
            .haveSimpleNameEndingWith("UseCaseImpl")
            .should()
            .implement(JavaClass.Predicates.resideInAPackage("..application..usecase.."))
            .because("Use case implementations must implement an use case interface");

    @ArchTest
    static final ArchRule ports_should_be_on_port_directory =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("Port")
            .and()
            .resideInAPackage("..application.port..")
            .should()
            .beInterfaces()
            .because("Ports should be in application.port package");

    @ArchTest
    static final ArchRule feature_use_cases_should_not_depend_on_other_feature_use_cases =
        ArchRuleDefinition.noClasses()
            .that()
            .resideInAPackage("..application..usecase..")
            .and()
            .areInterfaces()
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..application..usecase..")
            .because("Feature use cases must be independent business operations;"
                    + " orchestration must go through ports and the CQRS bus, not direct use case coupling");
}
