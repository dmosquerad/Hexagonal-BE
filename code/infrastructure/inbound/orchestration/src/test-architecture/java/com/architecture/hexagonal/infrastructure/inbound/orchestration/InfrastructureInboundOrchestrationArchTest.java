package com.architecture.hexagonal.infrastructure.inbound.orchestration;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal.infrastructure.inbound.orchestration", importOptions = ImportOption.DoNotIncludeTests.class)
public class InfrastructureInboundOrchestrationArchTest {

    @ArchTest
    static final ArchRule handler_implementations_should_reside_in_handler_impl =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("HandlerImpl")
            .and()
            .areNotInterfaces()
            .should()
            .resideInAPackage("..infrastructure.inbound.orchestration.orchestrator..")
            .because("CQRS handler implementations must be organized under infrastructure.inbound.orchestration.orchestrator..handler");

    @ArchTest
    static final ArchRule bus_implementations_should_reside_in_dispatcher_impl =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("BusImpl")
            .should()
            .resideInAPackage("..infrastructure.inbound.orchestration.dispatcher..impl..")
            .because("Bus implementations must be in infrastructure.inbound.orchestration.dispatcher..impl.. where the Orchestration bus is implemented");

    @ArchTest
    static final ArchRule handlers_should_be_annotated_with_component =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("CommandHandlerImpl")
            .and()
            .haveSimpleNameNotEndingWith("QueryHandlerImpl")
            .and()
            .areNotInterfaces()
            .should()
            .beAnnotatedWith("org.springframework.stereotype.Component")
            .because("Command handlers are infrastructure wiring components");

}
