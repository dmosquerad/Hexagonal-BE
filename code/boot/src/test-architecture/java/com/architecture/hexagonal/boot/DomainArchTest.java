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
  static final ArchRule domain_data_should_reside_in_entity_vo_or_vo_factory_packages =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..domain.data..")
          .should()
          .resideInAnyPackage(
              "..domain.data.entity..",
              "..domain.data.vo..")
          .because("Domain data must follow the entity/vo taxonomy");

  @ArchTest
  static final ArchRule value_objects_should_reside_in_domain_data_vo_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Vo")
          .should()
          .resideInAPackage("..domain.data.vo..")
          .because("Value objects must reside in domain.data.vo");

  @ArchTest
  static final ArchRule value_object_factories_should_reside_in_domain_data_vo_factory_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("VoFactory")
          .should()
          .resideInAPackage("..domain.service.factory.vo..")
          .because("Value object factories must reside in domain.service.factory.vo");
  
}

