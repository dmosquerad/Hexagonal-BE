package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class ScreamingArchTest {

  @ArchTest
  static final ArchRule no_manager_classes_should_exist =
      ArchRuleDefinition.noClasses()
          .should()
          .haveSimpleNameEndingWith("Manager")
          .because("'Manager' is a generic technical name that hides business intent;"
              + " use a name that reflects the specific business responsibility");

  @ArchTest
  static final ArchRule no_helper_classes_should_exist =
      ArchRuleDefinition.noClasses()
          .should()
          .haveSimpleNameEndingWith("Helper")
          .because("'Helper' is a vague technical name that obscures what the class actually does;"
              + " use a name that reflects the specific business or mapping responsibility");

  @ArchTest
  static final ArchRule feature_use_cases_should_not_depend_on_other_feature_use_cases =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..application.feature..usecase..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..application.feature..usecase..")
          .because("Feature use cases must be independent business operations;"
              + " orchestration must go through ports and the CQRS bus, not direct use case coupling");

  @ArchTest
  static final ArchRule feature_use_cases_should_follow_verb_concept_naming =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..application.feature..usecase..")
          .and()
          .haveNameNotMatching(
              ".*\\.(Create|Update|Patch|Delete|Find|Get|Exists|Validate|Process|Send|Notify)[A-Z].*UseCase"
                  + "|.*\\.[A-Z][a-zA-Z]+(Exists|Available|Valid)[A-Z]?.*UseCase")
          .should()
          .resideInAPackage("..application.feature..usecase..")
          .because("Feature use case names must reflect a business intent starting with a verb or status concept"
              + " so that the codebase screams what the system does, not how it is built")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule domain_should_not_contain_crud_verb_names =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .and()
          .haveNameMatching(".*\\.(Create|Read|Update|Delete)[A-Z].*")
          .should()
          .resideInAPackage("..domain..")
          .because("Domain model classes must use business-language names, not CRUD verbs;"
              + " CRUD terminology belongs to the application layer, not the domain")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule no_utils_classes_should_exist =
      ArchRuleDefinition.noClasses()
          .should()
          .haveSimpleNameEndingWith("Utils")
          .because("'Utils' groups unrelated operations under a vague technical name;"
              + " split into focused classes whose names express their specific responsibility");

  @ArchTest
  static final ArchRule no_impl_suffix_in_domain =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .haveSimpleNameEndingWith("Impl")
          .because("Domain classes must be named after what they represent in the business;"
              + " 'Impl' is a technical suffix that carries no meaning in the domain language");

  @ArchTest
  static final ArchRule interfaces_should_not_use_hungarian_i_prefix =
      ArchRuleDefinition.noClasses()
          .that()
          .areInterfaces()
          .and()
          .resideInAnyPackage("..domain..", "..application..")
          .should()
          .haveNameMatching(".*\\.I[A-Z].*")
          .because("Hungarian 'I' prefix notation (IUserService, IOrderPort) is not idiomatic Java;"
              + " name interfaces after the business contract they represent");

  @ArchTest
  static final ArchRule no_base_prefix_in_domain =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .haveSimpleNameStartingWith("Base")
          .because("'Base' is a technical inheritance hint, not a business concept;"
              + " domain classes must be named after the concept they represent");

  @ArchTest
  static final ArchRule no_default_prefix_in_domain_or_application =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAnyPackage("..domain..", "..application..")
          .should()
          .haveSimpleNameStartingWith("Default")
          .because("'Default' is an implementation detail, not a business concept;"
              + " if there is only one implementation, name it after what it does");

  @ArchTest
  static final ArchRule no_info_suffix_in_domain =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .haveSimpleNameEndingWith("Info")
          .because("'Info' is a vague technical suffix; use the specific domain term"
              + " that describes the concept (e.g. UserProfile instead of UserInfo)");

  @ArchTest
  static final ArchRule no_data_suffix_in_domain =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .haveSimpleNameEndingWith("Data")
          .because("'Data' is a meaningless technical suffix in the domain;"
              + " use a name that expresses the business concept it models");

  @ArchTest
  static final ArchRule no_object_suffix =
      ArchRuleDefinition.noClasses()
          .should()
          .haveSimpleNameEndingWith("Object")
          .because("'Object' adds no meaning to a class name;"
              + " every class is an object — use the business concept as the name");

  @ArchTest
  static final ArchRule exceptions_should_reside_in_exception_package =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Exception")
          .should()
          .resideInAPackage("..exception..")
          .because("Exception classes must be grouped under an 'exception' package"
              + " so that callers can find and handle them in a predictable location");

  @ArchTest
  static final ArchRule no_abstract_prefix =
      ArchRuleDefinition.noClasses()
          .should()
          .haveSimpleNameStartingWith("Abstract")
          .because("'Abstract' is a technical inheritance detail, not a business concept;"
              + " name the class after what it models, not how it is implemented")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule no_handler_suffix_in_domain =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .haveSimpleNameEndingWith("Handler")
          .because("'Handler' is a technical event/request processing term;"
              + " domain classes must be named after business concepts, not technical patterns");

}
