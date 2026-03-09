package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
public class SpringArchTest {

  @ArchTest
  static final ArchRule component_classes_should_not_have_public_fields =
      ArchRuleDefinition.noFields()
          .that().areDeclaredInClassesThat().areAnnotatedWith(Component.class)
          .should().bePublic()
          .because("Injected dependencies in @Component classes must be private to preserve encapsulation");

  @ArchTest
  static final ArchRule component_fields_should_be_private_final =
      ArchRuleDefinition.fields()
          .that().areDeclaredInClassesThat().areAnnotatedWith(Component.class)
          .and().areNotStatic()
          .should().bePrivate()
          .andShould().beFinal()
          .because("All instance fields in @Component classes should be private final");

  @ArchTest
  static final ArchRule service_classes_should_not_have_public_fields =
      ArchRuleDefinition.noFields()
          .that().areDeclaredInClassesThat().areAnnotatedWith(Service.class)
          .should().bePublic()
          .because("Injected dependencies in @Service classes must be private to preserve encapsulation");

  @ArchTest
  static final ArchRule service_fields_should_be_private_final =
      ArchRuleDefinition.fields()
          .that().areDeclaredInClassesThat().areAnnotatedWith(Service.class)
          .and().areNotStatic()
          .should().bePrivate()
          .andShould().beFinal()
          .because("All instance fields in @Service classes should be private final");

  @ArchTest
  static final ArchRule rest_controller_classes_should_not_have_public_fields =
      ArchRuleDefinition.noFields()
          .that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
          .should().bePublic()
          .because("Injected dependencies in @RestController classes must be private to preserve encapsulation");

  @ArchTest
  static final ArchRule rest_controller_fields_should_be_private_final =
      ArchRuleDefinition.fields()
          .that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
          .and().areNotStatic()
          .should().bePrivate()
          .andShould().beFinal()
          .because("All instance fields in @RestController classes should be private final");
}
