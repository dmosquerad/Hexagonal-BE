package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class DomainArchTest {

  @ArchTest
  static final ArchRule domain_should_not_depend_on_spring =
      ArchRuleDefinition.noClasses()
          .that().resideInAPackage("..domain..")
          .should().dependOnClassesThat()
          .resideInAPackage("org.springframework..")
          .because("Domain layer must be framework independent");

  @ArchTest
  static final ArchRule domain_classes_should_not_have_public_non_static_fields =
      ArchRuleDefinition.noFields()
          .that().areDeclaredInClassesThat().resideInAPackage("..domain..")
          .and().areNotStatic()
          .should().bePublic()
          .andShould().beFinal()
          .because("Domain classes must not expose mutable or injectable state publicly");

  @ArchTest
  static final ArchRule domain_should_not_depend_on_application_or_infrastructure =
      ArchRuleDefinition.noClasses()
          .that().resideInAPackage("..domain..")
          .should().dependOnClassesThat()
          .resideInAnyPackage("..application..", "..infrastructure..")
          .because("Domain must be independent from application and infrastructure layers");

  @ArchTest
  static final ArchRule commands_should_reside_in_domain_command_package =
      ArchRuleDefinition.classes()
          .that().haveSimpleNameEndingWith("Command")
          .should().resideInAPackage("..domain.input.command..")
          .because("Commands belong to the domain input boundary");

  @ArchTest
  static final ArchRule queries_should_reside_in_domain_query_package =
      ArchRuleDefinition.classes()
          .that().haveSimpleNameEndingWith("Query")
          .should().resideInAPackage("..domain.input.query..")
          .because("Queries belong to the domain input boundary");

  @ArchTest
  static final ArchRule ports_should_reside_in_domain_port_package =
      ArchRuleDefinition.classes()
          .that().haveSimpleNameEndingWith("Port")
          .should().resideInAPackage("..domain.port..")
          .because("Ports define domain boundaries");

  @ArchTest
  static final ArchRule inbound_ports_should_reside_in_domain_port_in =
      ArchRuleDefinition.classes()
          .that().haveSimpleNameEndingWith("UseCasePort")
          .should().resideInAPackage("..domain.port.in..")
          .because("Inbound ports define use case entry points and must reside in port.in");

  @ArchTest
  static final ArchRule outbound_ports_should_reside_in_domain_port_out =
      ArchRuleDefinition.classes()
          .that().haveSimpleNameEndingWith("Port")
          .and().haveSimpleNameNotEndingWith("UseCasePort")
          .should().resideInAPackage("..domain.port.out..")
          .because("Outbound ports define external dependencies and must reside in port.out");
}

