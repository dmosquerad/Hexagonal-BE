package com.architecture.hexagonal.boot.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
class MapStructArchTest {

  @ArchTest
  static final ArchRule mappers_should_reside_in_allowed_packages =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("Mapper")
            .should()
            .resideInAnyPackage("..infrastructure..", "..application.mapper..")
            .andShould()
            .beAnnotatedWith("org.mapstruct.Mapper")
            .because(
                "MapStruct mappers must reside in infrastructure or the dedicated application mapper package");

  @ArchTest
  static final ArchRule application_mappers_should_not_depend_on_spring =
        ArchRuleDefinition.noClasses()
            .that()
            .resideInAPackage("..application.mapper..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("org.springframework..")
            .because("Application mappers use MapStruct instances instead of Spring");
}
