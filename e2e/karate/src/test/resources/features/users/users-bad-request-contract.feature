@error
Feature: Users Bad Request Contract - 400 responses

  Background:
    * url baseUrl

  Scenario: POST without email returns 400
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'No Email User' }
    When method POST
    Then status 400
    And match response.status == 400
    And match response.date == '#string'
    And match response.title == 'Bad Request'

  Scenario: POST without name returns 400
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { email: 'no-name@test.com' }
    When method POST
    Then status 400
    And match response.status == 400
    And match response.date == '#string'
    And match response.title == 'Bad Request'

  Scenario: POST with invalid email format returns 400
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Bad Email User', email: 'not-an-email' }
    When method POST
    Then status 400
    And match response.status == 400
    And match response.date == '#string'
    And match response.title == 'Bad Request'

  Scenario: PUT without email returns 400
    * def randomId = java.util.UUID.randomUUID() + ''
    Given url baseUrl + '/users/' + randomId
    And header Content-Type = 'application/json'
    And request { name: 'Missing Email' }
    When method PUT
    Then status 400
    And match response.status == 400
    And match response.date == '#string'
    And match response.title == 'Bad Request'

  Scenario: PUT without name returns 400
    * def randomId = java.util.UUID.randomUUID() + ''
    Given url baseUrl + '/users/' + randomId
    And header Content-Type = 'application/json'
    And request { email: 'missing-name@test.com' }
    When method PUT
    Then status 400
    And match response.status == 400
    And match response.date == '#string'
    And match response.title == 'Bad Request'

  Scenario: PUT with invalid email format returns 400
    * def randomId = java.util.UUID.randomUUID() + ''
    Given url baseUrl + '/users/' + randomId
    And header Content-Type = 'application/json'
    And request { name: 'Test', email: 'not-an-email' }
    When method PUT
    Then status 400
    And match response.status == 400
    And match response.date == '#string'
    And match response.title == 'Bad Request'

  Scenario: PATCH with invalid email format returns 400
    * def randomId = java.util.UUID.randomUUID() + ''
    Given url baseUrl + '/users/' + randomId
    And header Content-Type = 'application/json'
    And request { email: 'not-an-email' }
    When method PATCH
    Then status 400
    And match response.status == 400
    And match response.date == '#string'
    And match response.title == 'Bad Request'
