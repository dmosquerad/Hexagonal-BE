package com.architecture.hexagonal.infrastructure.outbound.database.mongodb;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@AnalyzeClasses(packages = "com.architecture.hexagonal.infrastructure.outbound.database.mongodb", importOptions = ImportOption.DoNotIncludeTests.class)
class InfrastructureOutboundOutboxArchTest {

    @ArchTest
    static final ArchRule daos_should_reside_in_data_package =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("Dao")
            .should()
            .resideInAPackage("..infrastructure.outbound.database.mongodb.data..")
            .because("JPA entity classes (Dao) must be confined to the data package");

    @ArchTest
    static final ArchRule document_models_should_reside_in_infrastructure_outbox =
        ArchRuleDefinition.classes()
            .that()
            .areAnnotatedWith(Document.class)
            .should()
            .resideInAPackage("..infrastructure.outbound.database.mongodb..")
            .because("MongoDB documents must be in infrastructure.outbound.outbox");

    @ArchTest
    static final ArchRule spring_data_repositories_should_extend_mongo_repository =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("Repository")
            .and()
            .resideInAPackage("..infrastructure.outbound.database.mongodb..")
            .should()
            .beAssignableTo(MongoRepository.class)
            .because("Outbound outbox repositories must extend MongoRepository");

    @ArchTest
    static final ArchRule repositories_should_reside_in_repository_package =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("Repository")
            .should()
            .resideInAPackage("..infrastructure.outbound.database.mongodb.repository..")
            .because("Spring Data repositories must be grouped under the repository package");
}
