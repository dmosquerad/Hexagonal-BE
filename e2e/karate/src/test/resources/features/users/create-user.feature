Feature: Create User - POST /users

  Background:
    * url baseUrl

  Scenario: Create user with valid data returns 200 with created user payload
    * def testEmail = 'create-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Test CreateUser', email: '#(testEmail)' }
    When method POST
    Then status 200
    And match response.status == 200
    And match response.date == '#string'
    And match response.data.userId == '#string'
    * def cleanupUserId = response.data.userId
    And match response.data.name == 'Test CreateUser'
    And match response.data.email == testEmail

  Scenario: Response structure contains all required fields
    * def testEmail = 'schema-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Schema Test User', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def cleanupUserId = response.data.userId
    And match response == { date: '#string', status: 200, data: { userId: '#string', name: '#string', email: '#string' } }
