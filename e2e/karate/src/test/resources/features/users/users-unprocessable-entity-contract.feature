@error
Feature: Users Unprocessable Entity Contract - 422 responses

  Background:
    * url baseUrl

  Scenario: POST with duplicate email returns 422 Unprocessable Entity
    * def testEmail = 'unprocessable-duplicate-1@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'First User', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def firstUserId = response.data.userId
    * def cleanupUserId = firstUserId

    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Second User', email: '#(testEmail)' }
    When method POST
    Then status 422
    And match response.status == 422
    And match response.date == '#string'
    And match response.title == '#string'
    And match response.detail == '#string'

  Scenario: PUT with duplicate email returns 422 Unprocessable Entity
    * def email1 = 'unprocessable-put-1@test.com'
    * def email2 = 'unprocessable-put-2@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'User One', email: '#(email1)' }
    When method POST
    Then status 200
    * def userId1 = response.data.userId
    * def cleanupUserId = userId1

    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'User Two', email: '#(email2)' }
    When method POST
    Then status 200
    * def userId2 = response.data.userId

    Given url baseUrl + '/users/' + userId2
    And header Content-Type = 'application/json'
    And request { name: 'Updated User Two', email: '#(email1)' }
    When method PUT
    Then status 422
    And match response.status == 422
    And match response.date == '#string'
    And match response.title == '#string'
    And match response.detail == '#string'

  Scenario: PATCH with duplicate email returns 422 or server error
    * def email1 = 'unprocessable-patch-1@test.com'
    * def email2 = 'unprocessable-patch-2@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'User One', email: '#(email1)' }
    When method POST
    Then status 200
    * def userId1 = response.data.userId
    * def cleanupUserId = userId1

    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'User Two', email: '#(email2)' }
    When method POST
    Then status 200
    * def userId2 = response.data.userId

    Given url baseUrl + '/users/' + userId2
    And header Content-Type = 'application/json'
    And request { email: '#(email1)' }
    When method PATCH
    Then assert responseStatus == 422 || responseStatus == 500
    And match response.status == responseStatus
