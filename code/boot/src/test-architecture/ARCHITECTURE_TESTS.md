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

### Active rules
1. HexagonalArchitectureTest: 6 rules
2. DomainArchTest: 11 rules
3. ApplicationArchTest: 7 rules
4. InfrastructureArchTest: 3 rules
5. InfrastructureInboundArchTest: 5 rules
6. InfrastructureOutboundArchTest: 7 rules
7. SpringArchTest: 11 rules

Total: 50 rules

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

### Status
Partial coverage. Structural DDD is covered, tactical DDD is still pending.

## Screaming Architecture

### Current coverage
1. Technical conventions by layer (Controller, Adapter, Repository, Mapper)
2. Technical suffixes blocked in domain (Dao, Dto)

### Status
Partial coverage. The project still screams technical layers more than business capabilities.

## Spring Boot Standards

### Current coverage
1. `@SpringBootApplication` located in boot package
2. `@SpringBootApplication` class named `Application`
3. `@Configuration` classes limited to config/boot packages
4. `@RestControllerAdvice` constrained to inbound layer
5. Field injection blocked (`@Autowired` on fields)
6. Fields in `@Component` / `@Service` / `@RestController` are non-public and `private final`

### Status
Good coverage for Spring Boot wiring and configuration standards.

## Remaining Gaps

## 1) Hexagonal / Onion

### Still pending
1. Explicit per-layer allow-list rules (not only negative dependency rules)
2. Stronger boot semantics to clearly separate allowed wiring from accidental business dependencies

### Priority
High

## 2) DDD

### Still pending
1. Rules for Aggregate / Entity / ValueObject taxonomy (if adopted)
2. Rules for Domain Services (if introduced)
3. Rules for domain events (if introduced)

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
1. There are no explicit AggregateRoot/ValueObject/DomainService markers yet.
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
At least one aggregate root or value object is implemented with a documented convention.

4. Domain service rules
Trigger:
Domain services are introduced as first-class domain concepts.
Add when:
There is at least one production domain service plus an agreed naming/placement convention.

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
1. Add tactical DDD rules once aggregates/value objects/services are explicit
2. Add screaming rules once capability-based package structure is introduced

## Conclusion
Current tests provide strong coverage for hexagonal/onion boundaries and Spring Boot standards.

DDD and screaming are partially covered at structural level. The remaining checks are intentionally postponed until the project introduces the corresponding tactical/business modeling elements.

## Compliance Checklist
1. Hexagonal baseline: YES
2. Onion baseline: YES
3. DDD structural: YES
4. DDD tactical: NO (pending by design)
5. Screaming architecture: PARTIAL
6. Spring Boot standards: YES
