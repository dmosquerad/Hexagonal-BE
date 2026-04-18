package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class InfrastructureArchTest {

  @ArchTest
  static final ArchRule mappers_should_reside_in_infrastructure =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Mapper")
          .should()
          .resideInAPackage("..infrastructure..")
          .because("Mappers must stay in infrastructure");

}

