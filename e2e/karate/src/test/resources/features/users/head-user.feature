Feature: Check User Existence - HEAD /users/{uuid}

  Background:
    * url baseUrl

  Scenario: HEAD existing user returns 200 without body
    * def testEmail = 'head-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Head Test', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    Given url baseUrl + '/users/' + createdUserId
    When method HEAD
    Then status 200
