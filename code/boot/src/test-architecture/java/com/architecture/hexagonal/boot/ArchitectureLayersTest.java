package com.architecture.hexagonal.boot;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ArchitectureLayersTest {

  @ArchTest
  static final ArchRule packages_should_be_free_of_cycles =
      SlicesRuleDefinition.slices()
          .matching("com.architecture.hexagonal.(*)..")
          .should()
          .beFreeOfCycles()
          .because("Package slices must not form dependency cycles");

  @ArchTest
  static final ArchRule application_ports_should_be_grouped_by_direction =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..application.port..")
          .should()
          .resideInAnyPackage(
              "..application.port.in..",
              "..application.port.out..")
          .because("Application ports must be classified by direction: inbound or outbound");

  @ArchTest
  static final ArchRule only_declared_adapters_should_exist =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..infrastructure..")
          .should()
          .resideInAnyPackage(
              "..infrastructure.outbound..",
              "..infrastructure.inbound..")
          .because("Infrastructure classes must be grouped under inbound or outbound adapters;"
              + " the bus belongs to inbound and outbox-related adapters belong to outbound");

  @ArchTest
  static final ArchRule layers_should_follow_hexagonal_architecture =
      Architectures.layeredArchitecture()
          .consideringAllDependencies()
          .layer("Domain").definedBy("com.architecture.hexagonal.domain..")
          .layer("Application").definedBy("com.architecture.hexagonal.application..")
          .layer("Infrastructure").definedBy("com.architecture.hexagonal.infrastructure..")
          .layer("Boot").definedBy("com.architecture.hexagonal.boot..")
          .layer("Contract").definedBy("com.architecture.hexagonal.contract..")
          .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
          .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure", "Boot")
          .whereLayer("Contract").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
          .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Boot")
          .whereLayer("Boot").mayNotBeAccessedByAnyLayer()
          .because("Dependencies point inward from Boot to Infrastructure, Application, and Domain;"
              + " Application and Infrastructure may also depend on Contract");

  @ArchTest
  static final ArchRule onion_architecture_layers_check =
      Architectures.onionArchitecture()
          .domainModels("com.architecture.hexagonal.domain..")
          .domainServices("com.architecture.hexagonal.domain.service..")
          .applicationServices("com.architecture.hexagonal.application..")
          .adapter(
              "inbound",
              "com.architecture.hexagonal.infrastructure.inbound..",
              "com.architecture.hexagonal.contract.inbound..")
          .adapter(
              "outbound",
              "com.architecture.hexagonal.infrastructure.outbound..",
              "com.architecture.hexagonal.contract.outbound..")
          .adapter("boot", "com.architecture.hexagonal.boot..")
          .because("Domain code must remain independent of application services and adapters;"
              + " application services may consume generated orchestration contracts;"
              + " inbound, outbound, and boot adapters must depend toward the application core");
}
