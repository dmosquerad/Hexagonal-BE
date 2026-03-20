# HEAD Endpoint Implementation - User Existence Check

## Overview
Implemented a HEAD HTTP method endpoint to verify if a user exists without retrieving the full user details. This is a common REST pattern for efficiency and checking resource availability.

## What Was Done

### 1. Domain Layer (Input)
- **File**: `UserExistsQuery.java`
  - Query object to encapsulate the user UUID for the HEAD operation
  - Follows the Hexagonal Architecture pattern with @Value and @Builder annotations

- **File**: `UserExistsUseCasePort.java`
  - Input port interface defining the contract for user existence verification
  - Method: `void execute(UserExistsQuery userExistsQuery) throws ResourceNotFoundException`
  - Returns void (no data, just verifies existence)

### 2. Application Layer (Use Case)
- **File**: `UserExistsUseCase.java`
  - Implements `UserExistsUseCasePort`
  - Uses `UserRepositoryReadPort.findUserById(uuid)` to verify existence
  - Throws `ResourceNotFoundException` if user not found
  - Transactional read-only operation for efficiency

### 3. Infrastructure Layer (REST Controller)
- **File**: `UserController.java`
  - Added new method: `ResponseEntity<Void> headUserByUuid(UUID userUuid)`
  - Added field: `UserExistsUseCasePort userExistsUseCasePort`
  - Catches `ResourceNotFoundException` and throws `ErrorResponseException` with HTTP 404
  - Returns `ResponseEntity.ok().build()` on success (200 OK with no body)

### 4. OpenAPI Contract
- **File**: `users-path.yml`
  - Added HEAD method definition under the `user` path parameter
  - ```yaml
    head:
      tags:
        - users
      summary: Check if an User exists by Uuid
      operationId: headUserByUuid
      responses:
        '200':
          description: User exists
        '404':
          description: Not Found
        '500':
          description: Error
    ```

### 5. Testing

#### Unit Tests (UserControllerTest.java)
- Added `@Mock UserExistsUseCasePort userExistsUseCasePort`
- **Test 1**: `headUserByUuid()` - Success case (user exists)
  - Verifies 200 OK response with empty body
  - Confirms use case port is called with correct query
  
- **Test 2**: `headUserByUuidUserNotFound()` - Not found case
  - Verifies `ErrorResponseException` is thrown with 404 status
  - Uses `usingRecursiveComparison()` for exception verification
  - Uses `HttpStatus.NOT_FOUND` enum instead of magic numbers

#### Integration Tests (UserControllerTestIT.java)
- Added `@MockitoBean UserExistsUseCasePort userExistsUseCasePort`
- Added imports for `ResourceNotFoundException` and `HttpStatus`
- **Test 1**: `headUserByUuid()` - HTTP 200 OK response
- **Test 2**: `headUserByUuidUserNotFound()` - HTTP 404 Not Found response

## HTTP Examples

### Success (User Exists)
```
HEAD /users/{userUuid}

Response: 200 OK
(empty body)
```

### Not Found
```
HEAD /users/{userUuid}

Response: 404 Not Found
Content-Type: application/json

{
  "status": 404,
  "title": "Not Found",
  "detail": "Not Found Data Message: {uuid}"
}
```

## Design Decisions

1. **Return Type**: `ResponseEntity<Void>` - HEAD requests should not include a response body by convention
2. **Error Handling**: Consistent with PUT/PATCH endpoints - throws `ErrorResponseException` with 404
3. **Transaction**: Read-only transaction for optimal database performance
4. **Query Object**: `UserExistsQuery` follows the same pattern as `FindUserByUserIdQuery`
5. **Port Pattern**: Dedicated port `UserExistsUseCasePort` maintains separation of concerns

## Files Modified
1. `code/domain/src/main/java/.../input/query/UserExistsQuery.java` - NEW
2. `code/domain/src/main/java/.../port/in/UserExistsUseCasePort.java` - NEW
3. `code/application/src/main/java/.../usecase/UserExistsUseCase.java` - NEW
4. `code/infrastructure/inbound/rest/src/main/java/.../controller/UserController.java` - MODIFIED
5. `code/infrastructure/inbound/rest/src/main/resources/contract/users/users-path.yml` - MODIFIED
6. `code/infrastructure/inbound/rest/src/test/.../UserControllerTest.java` - MODIFIED
7. `code/infrastructure/inbound/rest/src/test-integration/.../UserControllerTestIT.java` - MODIFIED

## Compilation Status
✅ All modules compile successfully with no errors
✅ Domain module compiles with new Query and Port
✅ Application module compiles with new UseCase
✅ Infrastructure module compiles with controller changes and tests

## Testing Status
- Unit tests added for both success and not-found cases
- Integration tests added for HTTP-level verification
- Test assertions use recursive comparison for exception objects
- Tests use Spring's HttpStatus enums for semantic correctness
