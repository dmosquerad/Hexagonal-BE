package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.OnlyIncludeTests.class)
class TestUtilArchTest {

    @ArchTest
    static final ArchRule test_data_builders_should_reside_in_testutils_package =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("TestDataBuilder")
            .should()
            .resideInAPackage("..testutils..")
            .because("Test data builders must be in testutils packages")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule test_data_builders_should_not_use_spring_boot_test =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("TestDataBuilder")
            .should()
            .notBeAnnotatedWith("org.springframework.boot.test.context.SpringBootTest")
            .because("Test data builders are utilities and must not spin up a Spring context")
            .allowEmptyShould(true);
}
