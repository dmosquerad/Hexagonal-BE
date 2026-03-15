package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class InfrastructureArchTest {

  @ArchTest
  static final ArchRule infrastructure_should_not_depend_on_application =
      ArchRuleDefinition.noClasses()
          .that().resideInAPackage("..infrastructure..")
          .should().dependOnClassesThat()
          .resideInAnyPackage("..application..")
          .because("Infrastructure adapters must depend only on domain ports, never on the application layer");

  @ArchTest
  static final ArchRule mappers_should_reside_in_infrastructure =
      ArchRuleDefinition.classes()
          .that().haveSimpleNameEndingWith("Mapper")
          .should().resideInAPackage("..infrastructure..")
          .because("Mapping between layers or DTOs is an infrastructure concern");

  @ArchTest
  static final ArchRule inbound_adapters_should_not_depend_on_outbound_ports =
      ArchRuleDefinition.noClasses()
          .that().resideInAPackage("..infrastructure.inbound..")
          .should().dependOnClassesThat()
          .resideInAPackage("..domain.port.out..")
          .because("Inbound adapters must only interact with inbound ports to maintain hexagonal isolation");

  @ArchTest
  static final ArchRule outbound_adapters_should_not_depend_on_inbound_ports =
      ArchRuleDefinition.noClasses()
          .that().resideInAPackage("..infrastructure.outbound..")
          .should().dependOnClassesThat()
          .resideInAPackage("..domain.port.in..")
          .because("Outbound adapters must only implement outbound ports to maintain hexagonal isolation");
}

