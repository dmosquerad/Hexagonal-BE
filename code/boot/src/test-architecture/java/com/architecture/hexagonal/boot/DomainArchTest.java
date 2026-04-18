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
  static final ArchRule domain_data_should_reside_in_entity_vo_or_vo_factory_packages =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..domain.model..")
          .should()
          .resideInAnyPackage(
              "..domain.model.entity..",
              "..domain.model.vo..",
              "..domain.model.vo.factory..")
          .because("Domain model classes must reside in entity, VO, or VO factory packages");

  @ArchTest
  static final ArchRule value_objects_should_reside_in_domain_data_vo_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Vo")
          .should()
          .resideInAPackage("..domain.model.vo..")
          .because("Value objects must reside in domain.model.vo");

  @ArchTest
  static final ArchRule value_object_factories_should_reside_in_domain_data_vo_factory_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("VoFactory")
          .should()
          .resideInAPackage("..domain.model.vo.factory..")
          .because("Value object factories must reside in domain.model.vo.factory");

  @ArchTest
  static final ArchRule predicates_should_reside_in_domain_model =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Predicate")
          .should()
          .resideInAPackage("..domain.model..")
          .because("Domain predicates must reside in domain model");

  @ArchTest
  static final ArchRule domain_exceptions_should_reside_in_domain_exception_package =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..domain..")
          .and()
          .haveSimpleNameEndingWith("Exception")
          .or()
          .haveSimpleNameEndingWith("ExceptionMessage")
          .should()
          .resideInAPackage("..domain.exception..")
          .because("Domain exceptions must be centralized in domain.exception"
              + " to keep error semantics within the domain boundary");

  @ArchTest
  static final ArchRule domain_services_should_reside_in_domain_service_package =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..domain.service..")
          .should()
          .resideInAPackage("..domain.service..")
          .andShould()
          .notBeAnnotatedWith("org.springframework.stereotype.Service")
          .because("Domain services must reside in domain.service and must not carry"
              + " Spring annotations — they are pure domain logic, not Spring beans");

  @ArchTest
  static final ArchRule domain_vo_fields_should_be_private_final =
      ArchRuleDefinition.fields()
          .that()
          .areDeclaredInClassesThat()
          .haveSimpleNameEndingWith("Vo")
          .and()
          .areNotStatic()
          .should()
          .beFinal()
          .andShould()
          .bePrivate()
          .because("Value Objects are immutable by definition;"
              + " all their instance fields must be private final to prevent external mutation");

  @ArchTest
  static final ArchRule domain_exceptions_should_not_have_public_fields =
      ArchRuleDefinition.noFields()
          .that()
          .areDeclaredInClassesThat()
          .resideInAPackage("..domain.exception..")
          .and()
          .areNotStatic()
          .should()
          .bePublic()
          .because("Domain exception fields must be encapsulated;"
              + " expose information via methods, not public state")
          .allowEmptyShould(true);

}

