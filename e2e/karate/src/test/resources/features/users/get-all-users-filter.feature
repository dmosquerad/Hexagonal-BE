Feature: Get All Users with Filters - GET /users

  Background:
    * url baseUrl

  Scenario: Filter by host returns only users matching that email host
    * def testEmail = 'get-users-host@gmail.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Host Filter Test', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdId = response.data.userId
    * def cleanupUserId = createdId

    Given url baseUrl + '/users'
    And param host = 'gmail'
    When method GET
    Then status 200
    And match response.status == 200
    And match response.data == '#array'
    And match response.data contains { userId: '#(createdId)', name: 'Host Filter Test', email: '#(testEmail)' }

  Scenario: Filter blockEmail=true returns users with blocked emails
    * def blockedEmail = 'get-users-blocked@banned.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Blocked User', email: '#(blockedEmail)' }
    When method POST
    Then status 200
    * def blockedUserId = response.data.userId
    * def cleanupUserId = blockedUserId

    Given url baseUrl + '/users'
    And param blockEmail = 'true'
    When method GET
    Then status 200
    And match response.status == 200
    And match response.data == '#array'
    And match response.data contains { userId: '#(blockedUserId)', name: 'Blocked User', email: '#(blockedEmail)' }

  Scenario: Filter blockEmail=false returns users with allowed emails
    * def allowedEmail = 'get-users-allowed@allowed.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Allowed User', email: '#(allowedEmail)' }
    When method POST
    Then status 200
    * def allowedUserId = response.data.userId
    * def cleanupUserId = allowedUserId

    Given url baseUrl + '/users'
    And param blockEmail = 'false'
    When method GET
    Then status 200
    And match response.status == 200
    And match response.data == '#array'
    And match response.data contains { userId: '#(allowedUserId)', name: 'Allowed User', email: '#(allowedEmail)' }
