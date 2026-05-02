package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
class BootArchTest {

    @ArchTest
    static final ArchRule boot_application_should_reside_in_boot_package =
            ArchRuleDefinition.classes()
                    .that()
                    .areAnnotatedWith(SpringBootApplication.class)
                    .should()
                    .resideInAPackage("..boot..")
                    .andShould()
                    .haveSimpleName("Application")
                    .because("@SpringBootApplication must be in boot package and named Application");

    @ArchTest
    static final ArchRule boot_should_not_depend_on_use_cases =
            ArchRuleDefinition.noClasses()
                    .that()
                    .resideInAPackage("..boot..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("..application.*.usecase..")
                    .because("Boot must only wire modules, not execute use case logic");

}
