Feature: Get User By UUID - GET /users/{uuid}

  Background:
    * url baseUrl

  Scenario: Get existing user returns 200 with correct data
    * def testEmail = 'get-user-by-id@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'GetById Test', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    Given url baseUrl + '/users/' + createdUserId
    When method GET
    Then status 200
    And match response.status == 200
    And match response.date == '#string'
    And match response.data.userId == createdUserId
    And match response.data.name == 'GetById Test'
    And match response.data.email == testEmail
