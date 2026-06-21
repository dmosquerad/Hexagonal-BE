package com.architecture.hexagonal.infrastructure.inbound.orchestration;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal.infrastructure.inbound.orchestration", importOptions = ImportOption.DoNotIncludeTests.class)
public class InfrastructureInboundCqrsArchTest {

    @ArchTest
    static final ArchRule handler_implementations_should_reside_in_handler_impl =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("Handler")
            .and()
            .areNotInterfaces()
            .and()
            .resideInAPackage("..infrastructure.inbound.orchestration.orchestrator..")
            .should()
            .resideInAPackage("..infrastructure.inbound.orchestration.orchestrator..handler..")
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
    static final ArchRule command_handler_impls_should_implement_command_handler =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("CommandHandler")
            .and()
            .areNotInterfaces()
            .should()
            .beAssignableTo("com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.command.CommandHandler")
            .because("Command handler implementations must implement the CommandHandler interface to guarantee a uniform Orchestration dispatch contract");

    @ArchTest
    static final ArchRule query_handler_impls_should_implement_query_handler =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("QueryHandler")
            .and()
            .areNotInterfaces()
            .should()
            .beAssignableTo("com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.QueryHandler")
            .because("Query handler implementations must implement the QueryHandler interface to guarantee a uniform Orchestration dispatch contract");

    @ArchTest
    static final ArchRule handlers_should_be_annotated_with_component =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("CommandHandler")
            .and()
            .haveSimpleNameNotEndingWith("QueryHandler")
            .and()
            .areNotInterfaces()
            .should()
            .beAnnotatedWith("org.springframework.stereotype.Component")
            .because("Command handlers are infrastructure wiring components");

}
