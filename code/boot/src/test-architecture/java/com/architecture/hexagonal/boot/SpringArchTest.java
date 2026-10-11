package com.architecture.hexagonal.boot;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AnalyzeClasses(packages = "com.architecture.hexagonal", importOptions = ImportOption.DoNotIncludeTests.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class SpringArchTest {

  @ArchTest
  static final ArchRule spring_should_only_be_used_in_boot_and_infrastructure =
      ArchRuleDefinition.noClasses()
          .that()
          .resideOutsideOfPackages("..boot..", "..infrastructure..", "..contract..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("org.springframework..")
          .because("Spring may only be used in boot, infrastructure, and generated contract packages");

  @ArchTest
  static final ArchRule application_and_domain_should_not_depend_on_spring_boot =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAnyPackage("..application..", "..domain..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("org.springframework.boot..")
          .because("Application and Domain must remain independent of Spring Boot");

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
  static final ArchRule stereotype_fields_should_be_private_final =
      ArchRuleDefinition.fields()
          .that()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(Service.class)
          .or()
          .areAnnotatedWith(Component.class)
          .or()
          .areAnnotatedWith(Repository.class)
          .or()
          .areAnnotatedWith(Controller.class)
          .or()
          .areAnnotatedWith(RestController.class)
          .or()
          .areAnnotatedWith(RestControllerAdvice.class)
          .or()
          .areAnnotatedWith(KafkaListener.class)
          .should()
          .bePrivate()
          .andShould()
          .beFinal()
          .because("Spring stereotype bean fields must be private and final to enforce encapsulation and constructor injection")
          .allowEmptyShould(true);

  @ArchTest
  static final ArchRule controller_annotation_can_only_reside_in_inbound =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(RestController.class)
          .or()
          .areAnnotatedWith(Controller.class)
          .or()
          .areAnnotatedWith(RestControllerAdvice.class)
          .or()
          .areAnnotatedWith(KafkaListener.class)
          .or()
          .areAssignableTo(SchedulingConfigurer.class)
          .should()
          .resideInAPackage("..infrastructure.inbound..")
          .because("@RestController, @Controller classes must be confined to the inbound package")
          .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repository_can_only_reside_in_outbound =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(Repository.class)
          .should()
          .resideInAPackage("..infrastructure.outbound..")
          .because("@Repository classes must be confined to the outbound  package")
          .allowEmptyShould(true);

}
