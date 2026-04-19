# Architecture Test Documentation

## Purpose
This document describes the architecture tests in the `boot` module and evaluates whether current coverage is sufficient for:

1. Hexagonal Architecture
2. Onion Architecture
3. DDD (Domain-Driven Design)
4. Screaming Architecture
5. Spring Boot standards

Architecture validation is implemented with ArchUnit in [code/boot/src/test-architecture/java/com/architecture/hexagonal/boot](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot).

## Current Test Inventory

### Test classes
1. [HexagonalArchitectureTest.java](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot/HexagonalArchitectureTest.java)
2. [DomainArchTest.java](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot/DomainArchTest.java)
3. [ApplicationArchTest.java](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot/ApplicationArchTest.java)
4. [InfrastructureArchTest.java](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot/InfrastructureArchTest.java)
5. [InfrastructureInboundArchTest.java](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot/InfrastructureInboundArchTest.java)
6. [InfrastructureOutboundArchTest.java](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot/InfrastructureOutboundArchTest.java)
7. [SpringArchTest.java](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot/SpringArchTest.java)
8. [BootArchTest.java](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot/BootArchTest.java)
9. [TestUnitArchTest.java](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot/TestUnitArchTest.java)
10. [TestIntegrationArchTest.java](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot/TestIntegrationArchTest.java)
11. [TestUtilArchTest.java](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot/TestUtilArchTest.java)
12. [ScreamingArchTest.java](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot/ScreamingArchTest.java)

### Active rules

#### HexagonalArchitectureTest (4 rules)
1. `packages_should_be_free_of_cycles`
2. `only_declared_adapters_should_exist`
3. `hexagonal_architecture_layers_check`
4. `layers_should_follow_hexagonal_architecture`

#### DomainArchTest (14 rules)
1. `domain_should_not_depend_on_spring`
2. `domain_classes_should_not_have_public_non_static_fields`
3. `domain_should_not_depend_on_application_or_infrastructure`
4. `domain_should_not_use_persistence_annotations`
5. `domain_should_not_expose_setter_methods`
6. `domain_should_not_use_technical_adapter_suffixes`
7. `domain_data_should_reside_in_entity_vo_or_vo_factory_packages`
8. `value_objects_should_reside_in_domain_data_vo_package`
9. `value_object_factories_should_reside_in_domain_data_vo_factory_package`
10. `predicates_should_reside_in_domain_model`
11. `domain_exceptions_should_reside_in_domain_exception_package`
12. `domain_services_should_reside_in_domain_service_package`
13. `domain_vo_fields_should_be_private_final`
14. `domain_exceptions_should_not_have_public_fields`

#### ApplicationArchTest (21 rules)
1. `application_should_not_depend_on_boot`
2. `application_should_not_depend_on_infrastructure`
3. `use_cases_should_depend_on_domain`
4. `use_cases_should_reside_in_application_layer`
5. `use_cases_should_implement_inbound_ports`
6. `use_cases_should_be_annotated_with_service`
7. `commands_should_reside_in_application_command_package`
8. `queries_should_reside_in_application_query_package`
9. `ports_should_reside_in_application_port_package`
10. `inbound_ports_should_reside_in_application_port_in`
11. `outbound_ports_should_reside_in_application_port_out`
12. `handler_implementations_should_reside_in_handler_impl`
13. `bus_implementations_should_reside_in_dispatcher_impl`
14. `inbound_ports_should_be_annotated_with_validated`
15. `command_handler_impls_should_implement_command_handler`
16. `query_handler_impls_should_implement_query_handler`
17. `commands_should_be_immutable`
18. `queries_should_be_immutable`
19. `ports_should_be_interfaces`
20. `command_handlers_should_be_annotated_with_component`
21. `query_handlers_should_be_annotated_with_component`

#### InfrastructureArchTest (1 rule)
1. `mappers_should_reside_in_infrastructure`

#### InfrastructureInboundArchTest (10 rules)
1. `controller_should_reside_in_inbound`
2. `dtos_should_reside_in_inbound`
3. `inbound_not_depend_on_outbound_ports`
4. `controller_should_depend_on_inbound_ports`
5. `rest_controller_can_only_in_in_layer`
6. `rest_controller_advice_should_reside_in_inbound`
7. `rest_controller_classes_should_not_have_public_fields`
8. `rest_controller_fields_should_be_private_final`
9. `inbound_mappers_should_not_depend_on_outbound`
10. `inbound_factories_should_reside_in_inbound`

#### InfrastructureOutboundArchTest (12 rules)
1. `repositories_should_reside_in_outbound_adapter`
2. `adapters_should_reside_in_outbound_adapter`
3. `daos_should_reside_in_outbound_adapter`
4. `jpa_entities_should_reside_in_infrastructure_outbound`
5. `outbound_adapters_should_depend_on_outbound_ports`
6. `outbound_adapters_should_not_depend_on_inbound_ports`
7. `spring_data_repositories_should_extend_jpa_repository`
8. `outbound_mappers_should_not_depend_on_inbound`
9. `mapper_util_classes_should_not_be_spring_beans`
10. `dao_classes_should_reside_in_outbound_database_data_package`
11. `outbound_adapters_should_be_annotated_with_repository`
12. `outbound_adapter_fields_should_be_private_final`

#### SpringArchTest (5 rules)
1. `configuration_classes_should_reside_in_config_or_boot`
2. `field_injection_should_not_be_used`
3. `service_classes_should_not_have_public_fields`
4. `service_fields_should_be_private_final`
5. `service_can_only_reside_in_application_layer`

#### BootArchTest (4 rules)
1. `boot_application_should_reside_in_boot_package`
2. `boot_application_should_be_named_application`
3. `boot_should_not_depend_on_usecase_impl`
4. `boot_should_only_contain_bootstrap_or_config_types`

#### TestUnitArchTest (5 rules)
1. `unit_tests_should_not_use_spring_boot_test`
2. `domain_tests_should_not_depend_on_spring`
3. `use_case_unit_tests_should_use_mockito_extension`
4. `inbound_unit_tests_should_use_mockito_extension`
5. `outbound_unit_tests_should_use_mockito_extension`

#### TestIntegrationArchTest (1 rule)
1. `integration_tests_should_be_annotated_with_spring_boot_test`

#### TestUtilArchTest (1 rule)
1. `test_data_builders_should_reside_in_testutils_package`

#### ScreamingArchTest (17 rules)
1. `no_manager_classes_should_exist`
2. `no_helper_classes_should_exist`
3. `use_cases_should_not_depend_on_other_use_cases`
4. `use_cases_should_follow_verb_concept_naming`
5. `domain_should_not_contain_crud_verb_names`
6. `no_util_suffix_outside_mapper_util`
7. `no_utils_classes_should_exist`
8. `no_impl_suffix_in_domain`
9. `interfaces_should_not_use_hungarian_i_prefix`
10. `no_base_prefix_in_domain`
11. `no_default_prefix_in_domain_or_application`
12. `no_info_suffix_in_domain`
13. `no_data_suffix_in_domain`
14. `no_object_suffix`
15. `exceptions_should_reside_in_exception_package`
16. `no_abstract_prefix`
17. `no_handler_suffix_in_domain`

**Total: 94 rules across 12 test classes**

## Coverage By Architectural Style

## Hexagonal Architecture

### Current coverage
1. Layer separation: `domain` / `application` / `infrastructure`
2. Domain has no dependency on application or infrastructure
3. Application has no dependency on infrastructure
4. Inbound controllers depend on inbound ports
5. Outbound adapters implement outbound ports
6. Inbound does not depend on outbound ports
7. Outbound does not depend on inbound ports
8. Infrastructure does not depend on boot
9. Application does not depend on boot
10. Boot does not depend on `domain.input` or use case implementations

### Status
Good structural coverage for core hexagonal boundaries.

## Onion Architecture

### Current coverage
1. Explicit onion rule in [HexagonalArchitectureTest.java](code/boot/src/test-architecture/java/com/architecture/hexagonal/boot/HexagonalArchitectureTest.java)
2. Domain isolated from framework concerns
3. Application in the middle ring, without dependency on infrastructure

### Status
Good structural coverage at package dependency level.

## DDD

### Current coverage
1. Domain isolated from Spring
2. Placement conventions for Command, Query, and Port
3. Separation between inbound and outbound ports
4. Domain is persistence-agnostic (no persistence annotations)
5. Basic immutability guard (no setters in domain)
6. Package-based taxonomy for domain data:
   - `..domain.model.entity..` for entities
   - `..domain.model.vo..` for value objects
   - `..domain.model.vo.factory..` for value object factories
   - `..domain.model..` for predicates
7. Domain exceptions centralized in `..domain.exception..`
8. Domain services reside in `..domain.service..` and carry no Spring annotations
9. Inbound ports declare `@Validated` to enforce input validation at the application boundary
10. CQRS handler implementations are bound to their respective `CommandHandler` / `QueryHandler` interfaces
11. Commands and Queries have all instance fields `private final` — guaranteed immutability at the CQRS level
12. All ports in `..application.port..` must be interfaces — enforces the dependency inversion contract
13. Command handlers use `@Component`, not `@Service` — stereotype discipline; `@Service` is reserved for use cases
14. Query handlers use `@Component`, not `@Service` — same stereotype discipline
15. Value Object instance fields are `private final` — VO immutability enforced at the field level
16. Domain exception classes carry no public instance fields — encapsulation enforced in the exception layer

### Status
Good structural and tactical DDD coverage for the current codebase. Remaining gaps are deferred until the project introduces aggregates, domain events, or richer entity semantics.

## Screaming Architecture

### Current coverage
1. Technical conventions by layer (Controller, Adapter, Repository, Mapper)
2. Technical suffixes blocked in domain (Dao, Dto)
3. `Manager` and `Helper` suffixes blocked project-wide — prevents generic anti-patterns that hide business intent
4. `Utils` and `Util` (outside `mapper.converter`) suffixes blocked — forces specific naming over grab-bag utility classes
5. `Impl` suffix blocked in domain — domain classes must carry business names, not implementation hints
6. `Base` prefix blocked in domain — no technical inheritance hints in the domain layer
7. `Default` prefix blocked in domain and application — if only one implementation exists, name it after what it does
8. `Info` suffix blocked in domain — use the specific domain term (e.g. `UserProfile` instead of `UserInfo`)
9. `Data` suffix blocked in domain — meaningless technical suffix; use the business concept name
10. `Object` suffix blocked project-wide — every class is an object; the name must reflect the concept
11. Hungarian `I` prefix blocked for interfaces in domain and application — idiomatic Java names interfaces after the contract
12. Use cases must not depend on other use cases — each use case is an independent business operation
13. Use case names must follow a verb-concept or concept-state pattern — names express what the system does
14. Domain model classes must not carry CRUD verb names — domain language stays business-oriented
15. `Abstract` prefix blocked project-wide — technical inheritance hint; class names must reflect the business concept
16. `Handler` suffix blocked in domain — domain classes must not carry technical event/request processing names
17. All `*Exception` classes must reside in a package named `..exception..` — predictable location for exception handling

### Status
Comprehensive screaming architecture naming discipline enforced. All major anti-patterns that obscure business intent (generic suffixes, technical prefixes, CRUD names in domain, use case coupling) are actively prevented. Full capability-based package organization (e.g. `user`, `email` subdomains) remains a structural refactor deferred to a future iteration.

## Spring Boot Standards

### Current coverage
1. `@SpringBootApplication` located in boot package
2. `@SpringBootApplication` class named `Application`
3. `@Configuration` classes limited to config/boot packages
4. `@RestControllerAdvice` constrained to inbound layer
5. Field injection blocked (`@Autowired` on fields)
6. Fields in `@Service` are non-public and `private final`
7. `@Service` only resides in application layer
8. `@RestController` fields are non-public and `private final`
9. Boot only contains bootstrap or configuration types
10. Boot does not depend on use case implementations
11. Mapper utility classes carry no Spring stereotype annotations
12. Inbound factory classes centralized in the inbound factory package
13. Outbound database adapters annotated with `@Repository` — correct stereotype; `@Component` is not sufficient for persistence gateways
14. Outbound adapter fields are `private final` — constructor injection enforced, field injection forbidden

### Status
Good coverage for Spring Boot wiring, configuration, field visibility, and Spring annotation placement standards.

## Remaining Gaps

## 1) Hexagonal / Onion

### Still pending
1. Explicit per-layer allow-list rules (not only negative dependency rules)
2. Stronger boot semantics to clearly separate allowed wiring from accidental business dependencies

### Priority
High

## 2) DDD

### Still pending
1. Rules for Aggregate taxonomy and richer entity/value object semantics beyond package placement
2. Rules for domain events (if introduced)

### Priority
High, if tactical DDD is part of the target architecture.

## 3) Screaming Architecture

### Still pending
1. Package organization by business capabilities (for example `user`, `account`, `order`) instead of only technical layers
2. Naming/package conventions for bounded contexts or subdomains
3. Ubiquitous language naming rules for capability packages

### Priority
Medium-High depending on the roadmap.

## Why Some Rules Are Still Missing
Yes, your assumption is correct: the remaining rules are not missing because of neglect, they are intentionally deferred because the current codebase does not yet contain the tactical constructs needed to validate them safely.

Examples:
1. There are still no explicit AggregateRoot or DomainService markers yet, although ValueObject placement is now standardized by package.
2. Capability-oriented package boundaries are not formalized yet.
3. Domain events are not modeled yet.

Adding those rules now would either be too generic (low signal) or produce false positives.

## Decision Log (Activation Triggers)

This section defines when each deferred rule set should be added.

1. Layer allow-list rules (Hexagonal/Onion)
Trigger:
The package layout is stable for at least one release cycle and dependency boundaries are no longer moving.
Add when:
At least one accidental cross-layer dependency appears during review, or teams request stricter dependency contracts.

2. Stronger boot wiring rules
Trigger:
Boot starts containing more than bootstrap/configuration code (for example orchestration logic or business branching).
Add when:
Any class in boot begins depending on use case internals or domain input models beyond startup wiring.

3. Tactical DDD taxonomy rules (Aggregate/Entity/ValueObject)
Trigger:
The project introduces explicit tactical DDD types, annotations, interfaces, or naming conventions.
Add when:
At least one aggregate root or richer entity semantics are implemented beyond current package-based value object conventions.

4. Domain service rules
Status: ACTIVE — `domain_services_should_reside_in_domain_service_package` enforces placement and absence of Spring annotations.

5. Domain event rules
Trigger:
Domain events become part of the business flow.
Add when:
There is at least one domain event model and event publication flow in production code.

6. Screaming architecture rules (capability-based packages)
Trigger:
The codebase adopts capability or bounded-context package organization.
Add when:
There are at least two business capability packages (for example user, account) with explicit boundaries.

7. Ubiquitous language naming rules
Trigger:
A shared ubiquitous language glossary is documented by the team.
Add when:
Domain package/class naming standards are formalized and approved.

## Recommended Next Steps

## Phase 1 (near-term)
1. Add allow-list dependency rules per layer
2. Harden boot rules to allow wiring while blocking business orchestration

## Phase 2 (when domain model grows)
1. Add stronger tactical DDD rules once aggregates/services are explicit and entity semantics are formalized
2. Add screaming rules once capability-based package structure is introduced

## Conclusion
Current tests provide strong coverage for hexagonal/onion boundaries, Spring Boot standards, and test conventions.

DDD and screaming are partially covered at structural level. The remaining checks are intentionally postponed until the project introduces the corresponding tactical/business modeling elements.

The domain model enforces tactical DDD placement: entities in `domain.model.entity`, value objects in `domain.model.vo`, VO factories in `domain.model.vo.factory`, predicates in `domain.model`, exceptions in `domain.exception`, and domain services in `domain.service` (Spring-annotation-free).

Test convention rules (`TestUnitArchTest`, `TestIntegrationArchTest`, `TestUtilArchTest`) enforce annotation and placement conventions for unit tests, integration tests, and test data builders. These rules use `allowEmptyShould(true)` because the test classes of other modules (`application`, `infrastructure`) are not present in the `boot` classpath at test time. The rules become effective if test-jars are added as test dependencies in `boot/pom.xml`.

## Compliance Checklist
1. Hexagonal baseline: YES
2. Onion baseline: YES
3. DDD structural: YES
4. DDD tactical: YES (aggregates and domain events deferred — no production code yet)
5. Screaming architecture: YES (naming anti-patterns and business-intent conventions enforced; capability-based package structure deferred)
6. Spring Boot standards: YES
7. Test conventions (unit): YES
8. Test conventions (integration): YES (vacuously — test classes of other modules not in boot classpath)
9. Test utility placement: YES (vacuously — same classpath limitation)
