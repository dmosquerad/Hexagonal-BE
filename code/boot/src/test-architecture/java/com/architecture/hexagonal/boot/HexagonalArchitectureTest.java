package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class HexagonalArchitectureTest {

  @ArchTest
  static final ArchRule packages_should_be_free_of_cycles =
      SlicesRuleDefinition.slices()
          .matching("com.architecture.hexagonal.(*)..")
          .should()
          .beFreeOfCycles()
          .because("Layers must be free of package cycles");

  @ArchTest
  static final ArchRule only_declared_adapters_should_exist =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..infrastructure..")
          .should()
          .resideInAnyPackage("..infrastructure.contract..",
              "..infrastructure.outbound.database..",
              "..infrastructure.outbound.configuration..",
              "..infrastructure.outbound.message..",
              "..infrastructure.inbound.rest..")
          .because("Infrastructure may contain only declared adapters;"
              + " undeclared adapter packages break the explicit port-adapter mapping");

  @ArchTest
  static final ArchRule layers_should_follow_hexagonal_architecture =
      Architectures.layeredArchitecture()
          .consideringAllDependencies()
          .layer("Domain").definedBy("..domain..")
          .layer("Application").definedBy("..application..")
          .layer("Infrastructure").definedBy("..infrastructure..")
          .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
          .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure")
          .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
          .because("Dependencies must always point inward:"
              + " Infrastructure -> Application -> Domain;"
              + " no outer layer may be accessed from a layer inside it");

  @ArchTest
  static final ArchRule inbound_adapters_should_not_depend_on_outbound_adapters =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..infrastructure.inbound..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..infrastructure.outbound..")
          .because("Inbound and outbound adapters must not communicate directly;"
              + " cross-adapter interaction must go through the application core via ports");

}
