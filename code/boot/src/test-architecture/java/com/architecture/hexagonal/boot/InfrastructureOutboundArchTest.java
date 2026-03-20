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
          .should().implement(Predicates.resideInAPackage("..domain.port.out.."))
          .because("Outbound adapters must implement outbound domain ports");

  @ArchTest
  static final ArchRule outbound_adapters_should_not_depend_on_inbound_ports =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..infrastructure.outbound..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..domain.port.in..")
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
}

