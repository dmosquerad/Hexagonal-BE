package com.architecture.hexagonal.infrastructure.outbound.database;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

@AnalyzeClasses(packages = "com.architecture.hexagonal.infrastructure.outbound.database", importOptions = ImportOption.DoNotIncludeTests.class)
class InfrastructureOutboundDatabaseArchTest {

    @ArchTest
    static final ArchRule daos_should_reside_in_data_package =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("Dao")
            .should()
            .resideInAPackage("..infrastructure.outbound.database.data..")
            .because("JPA entity classes (Dao) must be confined to the data package");

    @ArchTest
    static final ArchRule jpa_entities_should_reside_in_infrastructure_database =
        ArchRuleDefinition.classes()
            .that()
            .areAnnotatedWith(Entity.class)
            .should()
            .resideInAPackage("..infrastructure.outbound..")
            .because("JPA entities must be in infrastructure.outbound");

    @ArchTest
    static final ArchRule spring_data_repositories_should_extend_jpa_repository =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("Repository")
            .and()
            .resideInAPackage("..infrastructure.outbound..")
            .should()
            .beAssignableTo(JpaRepository.class)
            .because("Outbound repositories must extend JpaRepository");

    @ArchTest
    static final ArchRule repositories_should_reside_in_repository_package =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("Repository")
            .should()
            .resideInAPackage("..infrastructure.outbound.database.repository..")
            .because("Spring Data repositories must be grouped under the repository package");

    @ArchTest
    static final ArchRule specifications_should_reside_in_specification_package =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("Specifications")
            .should()
            .resideInAPackage("..infrastructure.outbound.database.repository.specification..")
            .because("JPA Specification classes must live under repository.specification");

    @ArchTest
    static final ArchRule predicates_should_reside_in_predicate_package =
        ArchRuleDefinition.classes()
            .that()
            .haveSimpleNameEndingWith("Predicates")
            .should()
            .resideInAPackage("..infrastructure.outbound.database.repository.predicate..")
            .because("Predicate helper classes must live under repository.predicate");
}
