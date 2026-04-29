package com.architecture.hexagonal.boot;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AnalyzeClasses(packages = "com.architecture.hexagonal")
class InfrastructureInboundArchTest {

  @ArchTest
  static final ArchRule controller_should_reside_in_inbound =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Controller")
          .should()
          .resideInAPackage("..infrastructure.inbound..")
          .because("Controllers must be in infrastructure.inbound");

  @ArchTest
  static final ArchRule inbound_not_depend_on_outbound_ports =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..infrastructure.inbound..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..application.port.out..")
          .because("Inbound adapters must not depend on outbound ports");

  @ArchTest
  static final ArchRule controller_should_depend_on_inbound_ports =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Controller")
          .and()
          .resideInAPackage("..infrastructure.inbound..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..application.cqrs..", "..application.port.in..")
          .because("Controllers must depend on inbound ports or CQRS request types");

  @ArchTest
  static final ArchRule rest_controller_should_reside_in_inbound_layer =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(RestController.class)
          .should()
          .resideInAPackage("..infrastructure.inbound..")
          .because("@RestController types must reside in infrastructure.inbound");

  @ArchTest
  static final ArchRule rest_controller_advice_should_reside_in_inbound =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(RestControllerAdvice.class)
          .should()
          .resideInAPackage("..infrastructure.inbound..")
          .because("@RestControllerAdvice classes must be in inbound layer");

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

  @ArchTest
  static final ArchRule inbound_mappers_should_not_depend_on_outbound =
      ArchRuleDefinition.noClasses()
          .that()
          .haveSimpleNameEndingWith("Mapper")
          .and()
          .resideInAPackage("..infrastructure.inbound..")
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..infrastructure.outbound..")
          .because("Inbound mappers must not depend on outbound infrastructure");

  @ArchTest
  static final ArchRule inbound_factories_should_reside_in_inbound =
      ArchRuleDefinition.classes()
          .that()
          .haveSimpleNameEndingWith("Factory")
          .and()
          .resideInAPackage("..infrastructure.inbound..")
          .should()
          .resideInAPackage("..infrastructure.inbound.rest.factory..")
          .because("Inbound factory classes must be centralized in the inbound factory package"
              + " to keep response-building logic separate from controllers");

  @ArchTest
  static final ArchRule controllers_should_not_bypass_ports_to_access_use_cases =
      ArchRuleDefinition.noClasses()
          .that()
          .areAnnotatedWith(RestController.class)
          .should()
          .dependOnClassesThat()
          .resideInAPackage("..application.usecase..")
          .because("Controllers must not bypass inbound ports when accessing use cases;"
              + " the port boundary is the canonical application boundary in Hexagonal Architecture");
}
