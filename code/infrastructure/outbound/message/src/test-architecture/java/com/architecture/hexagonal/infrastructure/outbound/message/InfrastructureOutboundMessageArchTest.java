package com.architecture.hexagonal.infrastructure.outbound.message;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal.infrastructure.outbound.message", importOptions = ImportOption.DoNotIncludeTests.class)
class InfrastructureOutboundMessageArchTest {

    @ArchTest
    static final ArchRule senders_should_reside_in_sender_package =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("Sender")
            .should()
            .resideInAPackage("..infrastructure.outbound.message.sender..")
            .because("Message sender classes must be grouped under the sender package");
}
