package com.architecture.hexagonal.boot;

import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ScreamingArchTest {

  @ArchTest
  static final ArchRule no_manager_classes_should_exist =
      ArchRuleDefinition.noClasses()
                    .should()
                    .haveSimpleNameEndingWith("Manager")
                    .because("'Manager' is a generic technical name; prefer a role-specific name");

  @ArchTest
  static final ArchRule no_helper_classes_should_exist =
      ArchRuleDefinition.noClasses()
          .should()
          .haveSimpleNameEndingWith("Helper")
          .because("'Helper' is a generic technical name; prefer a purpose-specific name");

  @ArchTest
  static final ArchRule no_utils_classes_should_exist =
      ArchRuleDefinition.noClasses()
          .should()
          .haveSimpleNameEndingWith("Utils")
          .because("'Utils' is a generic technical name; prefer a cohesive, specific type");

    @ArchTest
    static final ArchRule feature_use_cases_should_follow_verb_concept_naming =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(ApplicationUseCase.class)
          .should()
          .haveNameMatching(".*\\.(Persist|Create|Update|Patch|Delete|Find|Get|Exist|Validate|Process|Retry|Send|Notify|Block)"
             + "[A-Z].*UseCaseImpl|.*\\.[A-Z][a-zA-Z]+(Exists|Available|Valid)UseCaseImpl")
          .because("Application use cases must use an action-first or status-concept name and end in UseCaseImpl");

    @ArchTest
    static final ArchRule domain_should_not_contain_crud_verb_names =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..domain..")
          .should()
          .haveNameMatching(".*\\.(Create|Read|Update|Delete)[A-Z].*")
          .because("Domain types must use business-language names instead of CRUD operation names");
}
