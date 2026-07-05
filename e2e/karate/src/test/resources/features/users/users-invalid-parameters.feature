Feature: User Endpoints With Invalid Parameters - Parameter validation

  Background:
    * url baseUrl
    * def nonExistentId = 'not-a-uuid'

  Scenario: GET /users/{uuid} with invalid UUID format returns 400
    Given url baseUrl + '/users/not-a-uuid'
    When method GET
    Then status 400
    And match response.status == 400
    And match response.title == '#string'

  Scenario: HEAD /users/{uuid} with invalid UUID format returns 400
    Given url baseUrl + '/users/not-a-uuid'
    When method HEAD
    Then status 400
    And match response.status == 400
    And match response.title == '#string'

  Scenario: PUT /users/{uuid} with invalid UUID format returns 400
    Given url baseUrl + '/users/invalid-uuid'
    And header Content-Type = 'application/json'
    And request { name: 'Test', email: 'test@test.com' }
    When method PUT
    Then status 400
    And match response.status == 400
    And match response.title == '#string'

  Scenario: PATCH /users/{uuid} with invalid UUID format returns 400
    Given url baseUrl + '/users/not-valid-uuid'
    And header Content-Type = 'application/json'
    And request { name: 'Test' }
    When method PATCH
    Then status 400
    And match response.status == 400
    And match response.title == '#string'

  Scenario: DELETE /users/{uuid} with invalid UUID format returns 400
    Given url baseUrl + '/users/bad-uuid'
    When method DELETE
    Then status 400
    And match response.status == 400
    And match response.title == '#string'

  Scenario: GET /users with pagination page negative returns 400
    Given url baseUrl + '/users'
    And param page = -1
    And param size = 100
    When method GET
    Then status 400
    And match response.status == 400
    And match response.title == '#string'

  Scenario: GET /users with pagination size zero returns 400
    Given url baseUrl + '/users'
    And param page = 0
    And param size = 0
    When method GET
    Then status 400
    And match response.status == 400
    And match response.title == '#string'

  Scenario: GET /users with pagination size negative returns 400
    Given url baseUrl + '/users'
    And param page = 0
    And param size = -1
    When method GET
    Then status 400
    And match response.status == 400
    And match response.title == '#string'

  Scenario: GET /users with very large page returns 200 with empty data
    Given url baseUrl + '/users'
    And param page = 999999
    And param size = 100
    When method GET
    Then status 200
    And match response.status == 200
    And match response.data == '#[]'

  Scenario: GET /users with size equals 1 returns 200 with max 1 result
    * def testEmail = 'param-test-size-1@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Size Test', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    Given url baseUrl + '/users'
    And param size = 1
    When method GET
    Then status 200
    And match response.pagination.size == 1
