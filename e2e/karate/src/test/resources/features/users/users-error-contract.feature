@error
Feature: Users Error Contract - 404 responses

  Background:
    * url baseUrl
    * def nonExistentId = java.util.UUID.randomUUID() + ''

  Scenario: GET non-existing user returns 404 with error schema
    Given url baseUrl + '/users/' + nonExistentId
    When method GET
    Then status 404
    And match response.status == 404
    And match response.date == '#string'
    And match response.title == 'Not Found'
    And match response.detail == '#string'

  Scenario: HEAD non-existing user returns 404
    Given url baseUrl + '/users/' + nonExistentId
    When method HEAD
    Then status 404

  Scenario: PUT non-existing user returns 404 with error schema
    * def emailForUpdate = 'update-not-found-user@test.com'
    Given url baseUrl + '/users/' + nonExistentId
    And header Content-Type = 'application/json'
    And request { name: 'Not Found User', email: '#(emailForUpdate)' }
    When method PUT
    Then status 404
    And match response.status == 404
    And match response.date == '#string'
    And match response.title == 'Not Found'
    And match response.detail == '#string'

  Scenario: PATCH non-existing user returns 404 with error schema
    Given url baseUrl + '/users/' + nonExistentId
    And header Content-Type = 'application/json'
    And request { name: 'Ghost User' }
    When method PATCH
    Then status 404
    And match response.status == 404
    And match response.date == '#string'
    And match response.title == 'Not Found'
    And match response.detail == '#string'

  Scenario: DELETE non-existing user returns 404 with error schema
    Given url baseUrl + '/users/' + nonExistentId
    When method DELETE
    Then status 404
    And match response.status == 404
    And match response.date == '#string'
    And match response.title == 'Not Found'
    And match response.detail == '#string'