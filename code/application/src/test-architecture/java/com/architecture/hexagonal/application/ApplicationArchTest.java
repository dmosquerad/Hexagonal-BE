package com.architecture.hexagonal.application;

import com.architecture.hexagonal.application.annotation.ApplicationService;
import com.architecture.hexagonal.application.annotation.ApplicationUseCase;
import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.port.in.bus.query.QueryHandler;
import com.tngtech.archunit.core.domain.JavaClass.Predicates;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import java.time.Clock;

@AnalyzeClasses(packages = "com.architecture.hexagonal.application", importOptions = ImportOption.DoNotIncludeTests.class)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ApplicationArchTest {

    @ArchTest
    static final ArchRule application_use_case_should_be_annotated =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..application.usecase..impl..")
          .should()
          .beAnnotatedWith(ApplicationUseCase.class)
          .because("Use case implementations must carry @ApplicationUseCase for component scanning");

    @ArchTest
    static final ArchRule application_services_should_be_annotated =
      ArchRuleDefinition.classes()
          .that()
          .resideInAPackage("..application.service..impl..")
          .should()
          .beAnnotatedWith(ApplicationService.class)
          .because("Application service implementations must carry @ApplicationService for component scanning");

    @ArchTest
    static final ArchRule use_case_implementations_should_handle_bus =
      ArchRuleDefinition.classes()
          .that()
          .areAnnotatedWith(ApplicationUseCase.class)
          .should()
          .implement(Predicates.assignableTo(CommandHandler.class).or(Predicates.assignableTo(QueryHandler.class)))
          .because("Classes marked with @ApplicationUseCase must handle a command or query from the application bus");

    @ArchTest
    static final ArchRule use_case_implementations_should_not_depend_on_outbound_ports =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage("..application.usecase..impl..")
          .should()
          .dependOnClassesThat(Predicates.resideInAPackage("..application.port.out..")
            .and(DescribedPredicate.not(Predicates.resideInAPackage("..application.port.out.transaction.."))))
          .because("Use cases must access outbound ports through application services, except for transaction boundaries");

  @ArchTest
  static final ArchRule use_case_dependencies_should_be_services_or_transaction_ports =
      ArchRuleDefinition.fields()
          .that()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(ApplicationUseCase.class)
            .should()
            .haveRawType(Predicates.resideInAPackage("..application.service..")
                .or(Predicates.resideInAPackage("..application.port.out.transaction..")))
            .because("Use case fields may reference application services or outbound transaction boundaries");

  @ArchTest
  static final ArchRule application_service_dependencies_should_be_ports =
      ArchRuleDefinition.fields()
            .that()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(ApplicationService.class)
            .should()
            .haveRawType(Predicates.resideInAPackage("..application.port.out..").or(Predicates.assignableTo(Clock.class)))
            .because("Application service fields may reference outbound ports or java.time.Clock");

}
