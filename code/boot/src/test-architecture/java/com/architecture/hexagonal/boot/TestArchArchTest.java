package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.OnlyIncludeTests.class)
public class TestArchArchTest {

    @ArchTest
    static final ArchRule arch_tests_should_not_use_spring_boot_test =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("ArchTest")
            .should()
            .notBeAnnotatedWith("org.springframework.boot.test.context.SpringBootTest")
            .because("Arch tests must not spin up a Spring context");

    @ArchTest
    static final ArchRule arch_tests_should_not_use_extend_with =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("ArchTest")
            .should()
            .notBeAnnotatedWith("org.junit.jupiter.api.extension.ExtendWith")
            .because("Arch tests must not spin up a ExtendWith");

}
