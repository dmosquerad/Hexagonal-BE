package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class OnionArchitectureTest {

  @ArchTest
  static final ArchRule onion_architecture_layers_check =
      Architectures.onionArchitecture()
          .domainModels("..domain.model..", "..domain.exception..")
          .domainServices("..domain.service..")
          .applicationServices("..application.usecase..",
              "..application.port..",
              "..application.cqrs..")
          .adapter("inbound", "..infrastructure.inbound..")
          .adapter("outbound", "..infrastructure.outbound..")
          .because("Domain model and domain services must not depend on application or adapters;"
              + " application services must not depend on adapters;"
              + " adapters may only depend inward toward the application core");

}
