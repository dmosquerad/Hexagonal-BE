package com.architecture.hexagonal.domain;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal.domain", importOptions = ImportOption.DoNotIncludeTests.class)
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
  static final ArchRule domain_should_not_depend_on_project_modules =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(
              "..application..",
              "..infrastructure..",
              "..boot..")
          .because("Domain must not depend on any other project module");

  @ArchTest
  static final ArchRule domain_classes_should_not_have_public_mutable_fields =
      ArchRuleDefinition.noFields()
              .that()
              .areDeclaredInClassesThat()
              .resideInAPackage("..domain..")
              .and()
              .areNotStatic()
              .and()
              .areNotFinal()
              .should()
              .bePublic()
              .because("Domain must not expose public mutable state");

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
  static final ArchRule domain_data_should_reside_in_aggregate_entity_or_vo_packages =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..domain.model..")
          .should()
          .resideInAnyPackage(
              "..domain.model.aggregate..",
              "..domain.model.entity..",
              "..domain.model.pagination..",
              "..domain.model.vo..")
          .because("Domain model classes must reside in aggregate, entity, pagination or VO packages");

  @ArchTest
  static final ArchRule value_objects_should_reside_in_domain_data_vo_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Vo")
          .should()
          .resideInAPackage("..domain.model.vo..")
          .because("Value objects must reside in domain.model.vo");

  @ArchTest
  static final ArchRule factories_should_reside_in_domain_data__factory_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Factory")
          .should()
          .resideInAPackage("..domain.model..factory..")
          .because("Value object factories must reside in domain.model..factory");

  @ArchTest
  static final ArchRule entities_should_reside_in_domain_model_entity_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Do")
          .should()
          .resideInAPackage("..domain.model.entity..")
          .because("Domain entity classes (*Do) must reside in domain.model.entity;"
              + " mixing entities with aggregates or VOs blurs the domain model structure");


  @ArchTest
  static final ArchRule aggregates_should_reside_in_domain_model_aggregate_package =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..domain.model.aggregate..")
          .should()
          .resideInAPackage("..domain.model.aggregate..")
          .andShould()
          .notBeInterfaces()
          .because("Aggregate roots must be concrete classes confined to domain.model.aggregate");

  @ArchTest
  static final ArchRule pagination_classes_should_reside_in_domain_model_pagination_package =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..domain.model.pagination..")
          .should()
          .resideInAPackage("..domain.model.pagination..")
          .because("Pagination value types must be isolated in domain.model.pagination");

  @ArchTest
  static final ArchRule predicates_should_reside_in_domain_model_predicate_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Predicate")
          .should()
          .resideInAPackage("..domain.model..predicate..")
          .because("Domain predicates must reside in domain.model..predicate;"
              + " they validate value object state and belong alongside the VOs they guard");

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
  static final ArchRule domain_service_utility_classes_should_have_private_constructor =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..domain.service..")
          .and()
          .areNotInterfaces()
          .should()
          .haveOnlyPrivateConstructors()
          .because("Domain service utility classes must not be instantiable");

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
          .because("Domain exception fields must be encapsulated")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule no_impl_suffix_in_domain =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .haveSimpleNameEndingWith("Impl")
          .because("Domain classes must be named after what they represent in the business");

}

