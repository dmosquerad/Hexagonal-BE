package com.architecture.hexagonal.infrastructure.inbound.job.scheduled.inbox;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(
    packages = "com.architecture.hexagonal.infrastructure.inbound.job",
    importOptions = ImportOption.DoNotIncludeTests.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SchedulerInboxConfigArchTest {

  @ArchTest
  static final ArchRule scheduler_configuration_should_be_in_config_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Config")
          .should()
          .resideInAPackage("..infrastructure.inbound.job.scheduled.inbox.config..")
          .because("Scheduler configuration classes must live under infrastructure.inbound.job.config");

  @ArchTest
  static final ArchRule scheduler_executors_should_be_annotated_with_component =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("SchedulerImpl")
          .should()
          .beAnnotatedWith("org.springframework.stereotype.Component")
          .because("Scheduler executors are Spring-managed components");
}
