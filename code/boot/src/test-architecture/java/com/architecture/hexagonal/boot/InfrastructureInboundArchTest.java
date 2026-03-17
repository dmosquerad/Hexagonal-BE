package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class InfrastructureInboundArchTest {

  @ArchTest
  static final ArchRule controller_should_reside_in_inbound =
      ArchRuleDefinition.classes()
          .that().haveSimpleNameEndingWith("Controller")
          .should().resideInAPackage("..infrastructure.inbound..")
          .because("Controllers are inbound adapters in a hexagonal architecture");

  @ArchTest
  static final ArchRule dtos_should_reside_in_inbound =
      ArchRuleDefinition.classes()
          .that().haveSimpleNameEndingWith("Dto")
          .should().resideInAPackage("..infrastructure.inbound..")
          .because("DTOs used for incoming requests belong to inbound adapters");

  @ArchTest
  static final ArchRule inbound_not_depend_on_outbound_ports =
      ArchRuleDefinition.noClasses()
          .that().resideInAPackage("..infrastructure.inbound..")
          .should().dependOnClassesThat()
          .resideInAPackage("..domain.port.out..")
          .because("Controllers are inbound adapters and must only depend on inbound ports from the domain");

  @ArchTest
  static final ArchRule controller_should_depend_on_inbound_ports =
      ArchRuleDefinition.classes()
          .that().haveSimpleNameEndingWith("Controller")
          .and().resideInAPackage("..infrastructure.inbound..")
          .should().dependOnClassesThat()
          .resideInAPackage("..domain.port.in..")
          .because("Controllers must drive the application through inbound ports defined in the domain");

  @ArchTest
  static final ArchRule rest_controller_can_only_in_in_layer =
      ArchRuleDefinition.classes()
          .that().areAnnotatedWith(RestController.class)
          .should()
          .resideInAPackage("..infrastructure.inbound..");
}
