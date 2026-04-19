package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class TestUtilArchTest {

    @ArchTest
    static final ArchRule test_data_builders_should_reside_in_testutils_package =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("TestDataBuilder")
            .should()
            .resideInAPackage("..testutils..")
            .because("Test data builders must be in testutils packages"
                    + " to be discoverable and reusable as shared test fixtures across test types")
            .allowEmptyShould(true);
}
