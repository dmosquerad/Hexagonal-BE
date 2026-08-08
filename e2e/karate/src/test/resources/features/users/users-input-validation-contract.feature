@validation
Feature: User Input Validation Contract - Empty strings and invalid data

  Background:
    * url baseUrl
    * def nonExistentId = '00000000-0000-0000-0000-000000000001'

  Scenario: POST with empty name string returns 400 Bad Request
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: '', email: 'empty-name@test.com' }
    When method POST
    Then status 400
    And match response.status == 400
    And match response.title == 'Bad Request'

  Scenario: POST with empty email string returns 400
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Valid Name', email: '' }
    When method POST
    Then status 400
    And match response.status == 400
    And match response.title == 'Bad Request'

  Scenario: POST with email missing @ symbol returns 400
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Test User', email: 'invalid.domain.com' }
    When method POST
    Then status 400
    And match response.status == 400
    And match response.title == 'Bad Request'

  Scenario: POST with email missing domain returns 400
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Test User', email: 'user@' }
    When method POST
    Then status 400
    And match response.status == 400
    And match response.title == 'Bad Request'

  Scenario: POST with email missing local part returns 400
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Test User', email: '@domain.com' }
    When method POST
    Then status 400
    And match response.status == 400
    And match response.title == 'Bad Request'

  Scenario: PUT with email missing domain returns 400
    Given url baseUrl + '/users/' + nonExistentId
    And header Content-Type = 'application/json'
    And request { name: 'Test', email: 'user@' }
    When method PUT
    Then status 400
    And match response.status == 400
    And match response.title == 'Bad Request'

  Scenario: PATCH with email missing domain returns 400
    Given url baseUrl + '/users/' + nonExistentId
    And header Content-Type = 'application/json'
    And request { email: 'user@' }
    When method PATCH
    Then status 400
    And match response.status == 400
    And match response.title == 'Bad Request'

  Scenario: Create user with valid email then patch with invalid format returns 400
    * def validEmail = 'validation-patch@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Valid User', email: '#(validEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    Given url baseUrl + '/users/' + createdUserId
    And header Content-Type = 'application/json'
    And request { email: 'invalid@format@email.com' }
    When method PATCH
    Then status 400
    And match response.status == 400
    And match response.title == 'Bad Request'
