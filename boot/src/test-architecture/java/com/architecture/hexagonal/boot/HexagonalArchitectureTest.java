package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class HexagonalArchitectureTest {

  private static final String[] ALLOWED_ADAPTER_PACKAGES = {
      "..infrastructure.outbound.database..",
      "..infrastructure.inbound.rest.."
  };

  @ArchTest
  static final ArchRule packages_should_be_free_of_cycles =
      SlicesRuleDefinition.slices()
          .matching("com.architecture.hexagonal.(*)..")
          .should().beFreeOfCycles()
          .because("Packages must be free of cyclic dependencies between layers");

  @ArchTest
  static final ArchRule only_declared_adapters_should_exist =
      ArchRuleDefinition.classes()
          .that().resideInAPackage("..infrastructure..")
          .should().resideInAnyPackage(ALLOWED_ADAPTER_PACKAGES)
          .because("Only declared adapter packages are allowed in the infrastructure layer");

  @ArchTest
  static final ArchRule hexagonal_architecture_layers_check =
      Architectures.onionArchitecture()
          .domainModels("..domain.data..")
          .domainServices("..domain..")
          .applicationServices("..application..")
          .adapter("adapters", ALLOWED_ADAPTER_PACKAGES)
          .because("Architecture must follow a hexagonal (Ports & Adapters) layered structure");

  @ArchTest
  static final ArchRule layers_should_follow_hexagonal_architecture =
      Architectures.layeredArchitecture()
          .consideringAllDependencies()
          .layer("Domain").definedBy("..domain..")
          .layer("Application").definedBy("..application..")
          .layer("Infrastructure").definedBy("..infrastructure..")
          .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
          .whereLayer("Application").mayNotBeAccessedByAnyLayer()
          .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
          .because("Layers must respect hexagonal architecture: Infrastructure depends on Domain, "
              + "Application depends on Domain, no layer depends on Application or Infrastructure");
}
