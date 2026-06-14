package com.architecture.hexagonal.infrastructure.inbound.rest;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal.infrastructure.inbound.rest", importOptions = ImportOption.DoNotIncludeTests.class)
class InfrastructureInboundRestArchTest {

    @ArchTest
    static final ArchRule exception_handlers_should_reside_in_exception_package =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("ExceptionHandler")
            .should()
            .resideInAPackage("..infrastructure.inbound.rest.exception..")
            .because("Exception handlers must be centralized under the exception package");

    @ArchTest
    static final ArchRule factories_should_reside_in_factory_package =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("Factory")
            .should()
            .resideInAPackage("..infrastructure.inbound.rest.factory..")
            .because("Inbound factory classes must be centralized in the factory package");

}
