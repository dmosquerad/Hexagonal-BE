@smoke
Feature: Full E2E Flow - User CRUD

  Scenario: Full flow Create List Get HEAD PUT PATCH DELETE Verify deletion
    * url baseUrl

    # ── 1. CREATE USER ────────────────────────────────────────────────────────
    * def createEmail = 'e2e-flow-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'E2E Flow User', email: '#(createEmail)' }
    When method POST
    Then status 200
    And match response.data.name == 'E2E Flow User'
    And match response.data.email == createEmail
    * def userId = response.data.userId
    * def cleanupUserId = userId
    * print 'Created user - ID:', userId

    # ── 2. LIST ALL - the new user should appear in the list ─────────────────
    Given url baseUrl + '/users'
    When method GET
    Then status 200
    And match response.data == '#array'
    And match response.data contains { userId: '#(userId)', name: 'E2E Flow User', email: '#(createEmail)' }

    # ── 3. GET BY UUID ────────────────────────────────────────────────────────
    Given url baseUrl + '/users/' + userId
    When method GET
    Then status 200
    And match response.data.userId == userId
    And match response.data.name == 'E2E Flow User'
    And match response.data.email == createEmail

    # ── 4. HEAD - verify existence ────────────────────────────────────────────
    Given url baseUrl + '/users/' + userId
    When method HEAD
    Then status 200

    # ── 5. PUT - full update ──────────────────────────────────────────────────
    * def updatedEmail = 'e2e-flow-updated@test.com'
    Given url baseUrl + '/users/' + userId
    And header Content-Type = 'application/json'
    And request { name: 'E2E Updated', email: '#(updatedEmail)' }
    When method PUT
    Then status 200
    And match response.data.userId == userId
    And match response.data.name == 'E2E Updated'
    And match response.data.email == updatedEmail

    # ── 6. PATCH - partial update (name only) ─────────────────────────────────
    Given url baseUrl + '/users/' + userId
    And header Content-Type = 'application/json'
    And request { name: 'E2E Patched' }
    When method PATCH
    Then status 200
    And match response.data.userId == userId
    And match response.data.name == 'E2E Patched'
    And match response.data.email == updatedEmail

    # ── 7. DELETE - remove user ───────────────────────────────────────────────
    Given url baseUrl + '/users/' + userId
    When method DELETE
    Then status 200
    And match response.data.userId == userId
    And match response.data.name == 'E2E Patched'

    # ── 8. GET - verify the user no longer exists (404) ──────────────────────
    Given url baseUrl + '/users/' + userId
    When method GET
    Then status 404
    And match response.status == 404
    And match response.title == 'Not Found'
    * print 'E2E flow completed successfully'
