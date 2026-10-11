package com.architecture.hexagonal.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal.domain", importOptions = ImportOption.DoNotIncludeTests.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class DomainArchTest {

  @ArchTest
  static final ArchRule no_impl_suffix_in_domain =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .haveSimpleNameEndingWith("Impl")
          .because("Domain classes must be named after what they represent in the business");


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
            "..domain.model.vo..")
          .because("Domain model classes must reside in aggregate, entity or VO packages");

  @ArchTest
  static final ArchRule value_objects_should_reside_in_domain_data_vo_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Vo")
          .should()
          .resideInAPackage("..domain.model.vo..")
          .because("Value objects must reside in domain.model.vo");

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
  static final ArchRule factories_should_reside_under_their_domain_model_concept =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Factory")
          .should()
          .resideInAPackage("..domain.model.*.*..factory..")
          .because(
              "Domain factories must reside in a factory package nested under the model concept they create");

  @ArchTest
  static final ArchRule predicates_should_reside_under_their_domain_model_concept =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Predicate")
          .should()
          .resideInAPackage("..domain.model.*.*..predicate..")
          .because(
              "Domain predicates must reside in a predicate package nested under the model concepts they validate");

}
