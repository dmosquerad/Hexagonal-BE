package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
public class SpringArchTest {

  @ArchTest
  static final ArchRule spring_boot_application_should_reside_in_boot_package =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(SpringBootApplication.class)
          .should()
          .resideInAPackage("..boot..")
          .because("@SpringBootApplication must be in boot package");

  @ArchTest
  static final ArchRule spring_boot_application_should_be_named_application =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(SpringBootApplication.class)
          .should()
          .haveSimpleName("Application")
          .because("@SpringBootApplication class must be named Application");

  @ArchTest
  static final ArchRule configuration_classes_should_reside_in_config_or_boot =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(Configuration.class)
          .should()
          .resideInAnyPackage("..boot..", "..infrastructure..config..")
          .because("@Configuration classes must be in config or boot packages");

  @ArchTest
  static final ArchRule rest_controller_advice_should_reside_in_inbound =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(RestControllerAdvice.class)
          .should()
          .resideInAPackage("..infrastructure.inbound..")
          .because("@RestControllerAdvice classes must be in inbound layer");

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
  static final ArchRule component_classes_should_not_have_public_fields =
      ArchRuleDefinition.noFields()
          .that()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(Component.class)
          .should()
          .bePublic()
          .because("@Component fields must not be public");

  @ArchTest
  static final ArchRule component_fields_should_be_private_final =
      ArchRuleDefinition.fields()
          .that()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(Component.class)
          .and()
          .areNotStatic()
          .should()
          .bePrivate()
          .andShould()
          .beFinal()
          .because("@Component instance fields must be private final");

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
  static final ArchRule rest_controller_classes_should_not_have_public_fields =
      ArchRuleDefinition.noFields()
          .that()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(RestController.class)
          .should()
          .bePublic()
          .because("@RestController fields must not be public");

  @ArchTest
  static final ArchRule rest_controller_fields_should_be_private_final =
      ArchRuleDefinition.fields()
          .that()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(RestController.class)
          .and()
          .areNotStatic()
          .should()
          .bePrivate()
          .andShould()
          .beFinal()
          .because("@RestController instance fields must be private final");
}
