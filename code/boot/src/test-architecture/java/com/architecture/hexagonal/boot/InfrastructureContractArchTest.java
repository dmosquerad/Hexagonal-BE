package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
class InfrastructureContractArchTest {

  @ArchTest
  static final ArchRule contract_should_not_depend_on_adapter_layers =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..infrastructure.inbound.contract..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(
              "..domain..",
              "..application..",
              "..infrastructure.inbound.rest..",
              "..infrastructure.inbound.orchestration..",
              "..infrastructure.inbound.execution..",
              "..infrastructure.outbound..",
              "..boot..")
          .because("Infrastructure contracts must not depend on any other project module");
}
