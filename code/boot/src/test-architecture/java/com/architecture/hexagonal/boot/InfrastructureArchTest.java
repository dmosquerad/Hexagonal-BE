package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
class InfrastructureArchTest {

  @ArchTest
  static final ArchRule mappers_should_reside_in_infrastructure =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Mapper")
          .should()
          .resideInAPackage("..infrastructure..")
          .andShould()
          .beAnnotatedWith("org.mapstruct.Mapper")
          .because("Mappers must stay in infrastructure and be annotated with @Mapper");

  @ArchTest
  static final ArchRule mapper_util_classes_should_not_be_spring_beans =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..mapper.converter..")
          .should()
          .beAnnotatedWith("org.springframework.stereotype.Component")
          .orShould()
          .beAnnotatedWith("org.springframework.stereotype.Service")
          .because("Mapper converter classes are static helpers and must not be annotated as Spring beans");
}