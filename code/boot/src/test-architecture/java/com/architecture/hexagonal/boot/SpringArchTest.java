package com.architecture.hexagonal.boot;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
class SpringArchTest {

  @ArchTest
  static final ArchRule spring_should_only_be_used_in_boot_and_infrastructure =
      ArchRuleDefinition.noClasses()
          .that()
          .resideOutsideOfPackages("..boot..", "..infrastructure..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("org.springframework..")
          .because("Spring may only be used in boot and infrastructure packages");

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
          .should()
          .beAnnotatedWith(Autowired.class)
          .because("Use constructor injection instead of field injection");

  @ArchTest
  static final ArchRule stereotype_classes_should_not_have_public_fields =
      ArchRuleDefinition.noFields()
          .that()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(Service.class)
          .or()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(Component.class)
          .or()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(Repository.class)
          .or()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(Controller.class)
          .or()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(RestController.class)
          .should()
          .bePublic()
          .because("Spring stereotype bean fields must not be public");

  @ArchTest
  static final ArchRule stereotype_fields_should_be_private_final =
      ArchRuleDefinition.fields()
          .that()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(Service.class)
          .or()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(Component.class)
          .or()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(Repository.class)
          .or()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(Controller.class)
          .or()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(RestController.class)
          .and()
          .areNotStatic()
          .should()
          .bePrivate()
          .andShould()
          .beFinal()
          .because("Spring stereotype bean instance fields must be private final to enforce constructor injection")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule repository_can_only_reside_in_outbound_database_adapter =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(Repository.class)
          .should()
          .resideInAPackage("..infrastructure.outbound..")
          .because("@Repository is the persistence gateway stereotype")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule rest_controller_and_controller_can_only_reside_in_inbound_rest_controller =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(RestController.class)
          .and()
          .areAnnotatedWith(Controller.class)
          .should()
          .resideInAPackage("..infrastructure.inbound..")
          .because("@RestController, @Controller classes must be confined to the inbound controller package")
          .allowEmptyShould(true);
}
