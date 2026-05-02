package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
class ScreamingArchTest {

  @ArchTest
  static final ArchRule no_manager_classes_should_exist =
      ArchRuleDefinition.noClasses()
          .should()
          .haveSimpleNameEndingWith("Manager")
          .because("'Manager' is a generic technical and should be avoided;");

  @ArchTest
  static final ArchRule no_helper_classes_should_exist =
      ArchRuleDefinition.noClasses()
          .should()
          .haveSimpleNameEndingWith("Helper")
          .because("'Helper' is a generic technical and should be avoided;");

  @ArchTest
  static final ArchRule feature_use_cases_should_follow_verb_concept_naming =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..application..usecase..")
          .and()
          .areInterfaces()
          .and()
          .haveNameNotMatching(
              ".*\\.(Create|Update|Patch|Delete|Find|Get|Exists|Validate|Process|Send|Notify)[A-Z].*UseCase"
                  + "|.*\\.[A-Z][a-zA-Z]+(Exists|Available|Valid)[A-Z]?.*UseCase")
          .should()
          .resideInAPackage("..application..usecase..")
          .because("Feature use case names must reflect a business intent starting with a verb or status concept")
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
          .because("Domain model classes must use business-language names, not CRUD verbs")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule no_utils_classes_should_exist =
      ArchRuleDefinition.noClasses()
          .should()
          .haveSimpleNameEndingWith("Utils")
          .because("'Utils' is a generic technical and should be avoided");

  @ArchTest
  static final ArchRule interfaces_should_not_use_hungarian_i_prefix =
      ArchRuleDefinition.noClasses()
          .that()
          .areInterfaces()
          .and()
          .resideInAnyPackage("..domain..", "..application..")
          .should()
          .haveNameMatching(".*\\.I[A-Z].*")
          .because("Hungarian 'I' prefix notation (IUserService, IOrderPort) is not idiomatic Java");

}
