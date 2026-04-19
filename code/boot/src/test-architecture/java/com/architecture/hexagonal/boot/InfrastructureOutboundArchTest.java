package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.domain.JavaClass.Predicates;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class InfrastructureOutboundArchTest {

  @ArchTest
  static final ArchRule repositories_should_reside_in_outbound_adapter =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Repository")
          .should()
          .resideInAPackage("..infrastructure.outbound..")
          .because("Repositories must be in infrastructure.outbound");

  @ArchTest
  static final ArchRule adapters_should_reside_in_outbound_adapter =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Adapter")
          .should()
          .resideInAPackage("..infrastructure.outbound..")
          .because("Adapters must be in infrastructure.outbound");

  @ArchTest
  static final ArchRule daos_should_reside_in_outbound_adapter =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Dao")
          .should()
          .resideInAPackage("..infrastructure.outbound..")
          .because("DAOs must be in infrastructure.outbound");

  @ArchTest
  static final ArchRule jpa_entities_should_reside_in_infrastructure_outbound =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(Entity.class)
          .should()
          .resideInAPackage("..infrastructure.outbound..")
          .because("JPA entities must be in infrastructure.outbound");

  @ArchTest
  static final ArchRule outbound_adapters_should_depend_on_outbound_ports =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Adapter")
          .and()
          .resideInAPackage("..infrastructure.outbound..")
          .should().implement(Predicates.resideInAPackage("..application.port.out.."))
          .because("Outbound adapters must implement outbound ports");

  @ArchTest
  static final ArchRule outbound_adapters_should_not_depend_on_inbound_ports =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..infrastructure.outbound..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..application.port.in..")
          .because("Outbound adapters must not depend on inbound ports");

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
  static final ArchRule outbound_mappers_should_not_depend_on_inbound =
      ArchRuleDefinition.noClasses()
          .that()
          .haveSimpleNameEndingWith("Mapper")
          .and()
          .resideInAPackage("..infrastructure.outbound..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..infrastructure.inbound..")
          .because("Outbound mappers must not depend on inbound infrastructure");

  @ArchTest
  static final ArchRule mapper_util_classes_should_not_be_spring_beans =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..mapper.converter..")
          .should()
          .beAnnotatedWith("org.springframework.stereotype.Component")
          .orShould()
          .beAnnotatedWith("org.springframework.stereotype.Service")
          .because("Mapper converter classes are static helpers and must not be annotated as Spring beans");

  @ArchTest
  static final ArchRule dao_classes_should_reside_in_outbound_database_data_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Dao")
          .should()
          .resideInAPackage("..infrastructure.outbound.database.data..")
          .because("DAO (JPA entity) classes must be isolated in the database data package"
              + " to prevent JPA annotations from leaking into other layers");

  @ArchTest
  static final ArchRule outbound_adapters_should_be_annotated_with_repository =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..infrastructure.outbound.database.adapter..")
          .and()
          .haveSimpleNameEndingWith("Adapter")
          .should()
          .beAnnotatedWith("org.springframework.stereotype.Repository")
          .because("Outbound database adapters are the persistence gateway;"
              + " @Repository is the correct Spring stereotype, not @Component or @Service");

  @ArchTest
  static final ArchRule outbound_adapter_fields_should_be_private_final =
      ArchRuleDefinition.fields()
          .that()
          .areDeclaredInClassesThat()
          .resideInAPackage("..infrastructure.outbound..adapter..")
          .and()
          .areNotStatic()
          .should()
          .beFinal()
          .andShould()
          .bePrivate()
          .because("Outbound adapter fields must be private final to enforce constructor injection;"
              + " field injection and mutable state are forbidden");

}
