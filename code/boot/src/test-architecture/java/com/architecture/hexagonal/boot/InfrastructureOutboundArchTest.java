package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.domain.JavaClass.Predicates;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
class InfrastructureOutboundArchTest {

  @ArchTest
  static final ArchRule adapters_should_reside_in_outbound_adapter =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("AdapterImpl")
          .should()
          .resideInAPackage("..infrastructure.outbound..")
          .because("Adapters must be in infrastructure.outbound");

  @ArchTest
  static final ArchRule outbound_adapters_should_depend_on_outbound_ports =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("AdapterImpl")
          .and()
          .resideInAPackage("..infrastructure.outbound..")
          .should().implement(Predicates.resideInAPackage("..application.port.."))
          .because("Outbound adapters must implement application port interfaces");

  @ArchTest
  static final ArchRule outbound_adapters_should_not_depend_on_inbound_ports =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..infrastructure.outbound..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..infrastructure.inbound..")
          .because("Outbound adapters must not depend on inbound infrastructure");

}
