package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class DomainArchTest {

  @ArchTest
  static final ArchRule domain_should_not_depend_on_spring =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("org.springframework..")
          .because("Domain must not depend on Spring");

  @ArchTest
  static final ArchRule domain_classes_should_not_have_public_non_static_fields =
      ArchRuleDefinition.noFields()
          .that()
          .areDeclaredInClassesThat()
          .resideInAPackage("..domain..")
          .and()
          .areNotStatic()
          .should()
          .bePublic()
          .andShould()
          .beFinal()
          .because("Domain must not expose public mutable state");

  @ArchTest
  static final ArchRule domain_should_not_depend_on_application_or_infrastructure =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..application..", "..infrastructure..")
          .because("Domain must not depend on application or infrastructure");

  @ArchTest
  static final ArchRule domain_should_not_use_persistence_annotations =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .beAnnotatedWith(Entity.class)
          .orShould()
          .beAnnotatedWith(Table.class)
          .orShould()
          .beAnnotatedWith(Id.class)
          .orShould()
          .beAnnotatedWith(Column.class)
          .because("Domain must be persistence-agnostic");

  @ArchTest
  static final ArchRule domain_should_not_expose_setter_methods =
      ArchRuleDefinition.noMethods()
          .that()
          .areDeclaredInClassesThat()
          .resideInAPackage("..domain..")
          .should()
          .haveNameMatching("set[A-Z].*")
          .because("Domain must not expose setter methods");

  @ArchTest
  static final ArchRule domain_should_not_use_technical_adapter_suffixes =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .haveSimpleNameEndingWith("Dao")
          .orShould()
          .haveSimpleNameEndingWith("Dto")
          .because("Domain names must avoid technical adapter suffixes");

  @ArchTest
  static final ArchRule commands_should_reside_in_domain_command_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Command")
          .should()
          .resideInAPackage("..domain.input.command..")
          .because("Commands must be in domain.input.command");

  @ArchTest
  static final ArchRule queries_should_reside_in_domain_query_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Query")
          .should()
          .resideInAPackage("..domain.input.query..")
          .because("Queries must be in domain.input.query");

  @ArchTest
  static final ArchRule ports_should_reside_in_domain_port_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Port")
          .should()
          .resideInAPackage("..domain.port..")
          .because("Ports must be in domain.port");

  @ArchTest
  static final ArchRule inbound_ports_should_reside_in_domain_port_in =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("UseCasePort")
          .should()
          .resideInAPackage("..domain.port.in..")
          .because("Inbound use case ports must be in domain.port.in");

  @ArchTest
  static final ArchRule outbound_ports_should_reside_in_domain_port_out =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Port")
          .and()
          .haveSimpleNameNotEndingWith("UseCasePort")
          .should()
          .resideInAPackage("..domain.port.out..")
          .because("Outbound ports must be in domain.port.out");
}

