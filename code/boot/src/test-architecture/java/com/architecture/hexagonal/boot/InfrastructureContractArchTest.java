package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class InfrastructureContractArchTest {

  @ArchTest
  static final ArchRule dtos_should_reside_in_infrastructure_contract =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Dto")
          .should()
          .resideInAPackage("..infrastructure.contract..")
          .because("DTOs must be in infrastructure.contract");

  @ArchTest
  static final ArchRule contract_should_not_depend_on_adapter_layers =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..infrastructure.contract..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..infrastructure.inbound..", "..infrastructure.outbound..")
          .because("Contract classes must not depend on inbound or outbound adapter implementations");
}
