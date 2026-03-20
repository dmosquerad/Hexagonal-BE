package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

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
          .should()
          .beFreeOfCycles()
          .because("Layers must be free of package cycles");

  @ArchTest
  static final ArchRule only_declared_adapters_should_exist =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..infrastructure..")
          .should()
          .resideInAnyPackage(ALLOWED_ADAPTER_PACKAGES)
          .because("Infrastructure may contain only declared adapters");

  @ArchTest
  static final ArchRule hexagonal_architecture_layers_check =
      Architectures.onionArchitecture()
          .domainModels("..domain.data..")
          .domainServices("..domain..")
          .applicationServices("..application..")
          .adapter("adapters", ALLOWED_ADAPTER_PACKAGES)
          .because("Architecture must follow onion/hexagonal layering");

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
          .because("Dependencies must follow the defined layer boundaries");
}
