package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureLayersTest {

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
              "..infrastructure.inbound.rest..",
              "..infrastructure.inbound.orchestration..")
          .because("Infrastructure may contain only declared adapters;"
              + " undeclared adapter packages break the explicit port-adapter mapping");

  @ArchTest
  static final ArchRule layers_should_follow_hexagonal_architecture =
      Architectures.layeredArchitecture()
          .consideringAllDependencies()
          .layer("Domain").definedBy("..domain..")
          .layer("Application").definedBy("..application..")
          .layer("Infrastructure").definedBy("..infrastructure..")
          .layer("Boot").definedBy("..boot..")
          .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
          .whereLayer("Application").mayOnlyBeAccessedByLayers( "Infrastructure", "Boot")
          .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Boot")
          .whereLayer("Boot").mayNotBeAccessedByAnyLayer()
          .because("Dependencies must always point inward:"
              + " Boot -> Infrastructure -> Application -> Domain;"
              + " no outer layer may be accessed from a layer inside it");

  @ArchTest
  static final ArchRule onion_architecture_layers_check =
      Architectures.onionArchitecture()
          .domainModels("..domain.model..")
          .domainServices("..domain.service..")
          .applicationServices("..application..")
          .adapter("inbound", "..infrastructure.inbound..")
          .adapter("outbound", "..infrastructure.outbound..")
          .adapter("boot", "..boot..")
          .because("Domain model and domain services must not depend on application or adapters;"
              + " application services must not depend on adapters;"
              + " adapters may only depend inward toward the application core");

}
