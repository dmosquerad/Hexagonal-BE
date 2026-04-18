package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class SpringArchTest {

  @ArchTest
  static final ArchRule configuration_classes_should_reside_in_config_or_boot =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(Configuration.class)
          .should()
          .resideInAnyPackage("..boot..", "..infrastructure..config..")
          .because("@Configuration classes must be in config or boot packages");

  @ArchTest
  static final ArchRule field_injection_should_not_be_used =
      ArchRuleDefinition.noFields()
          .that()
          .areDeclaredInClassesThat()
          .resideInAnyPackage(
              "..application..", "..infrastructure..", "..boot..")
          .should()
          .beAnnotatedWith(Autowired.class)
          .because("Use constructor injection instead of field injection");

  @ArchTest
  static final ArchRule service_classes_should_not_have_public_fields =
      ArchRuleDefinition.noFields()
          .that()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(Service.class)
          .should()
          .bePublic()
          .because("@Service fields must not be public");

  @ArchTest
  static final ArchRule service_fields_should_be_private_final =
      ArchRuleDefinition.fields()
          .that()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(Service.class)
          .and()
          .areNotStatic()
          .should()
          .bePrivate()
          .andShould()
          .beFinal()
          .because("@Service instance fields must be private final");

  @ArchTest
  static final ArchRule service_can_only_reside_in_application_layer =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(Service.class)
          .should()
          .resideInAPackage("..application.usecase..")
          .because("Only application use cases may be @Service");
}
