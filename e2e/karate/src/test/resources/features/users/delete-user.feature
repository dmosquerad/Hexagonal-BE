Feature: Delete User - DELETE /users/{uuid}

  Background:
    * url baseUrl

  Scenario: Delete existing user returns 200 with deleted user data
    * def testEmail = 'delete-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Delete Test', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    Given url baseUrl + '/users/' + createdUserId
    When method DELETE
    Then status 200
    And match response.status == 200
    And match response.date == '#string'
    And match response.data.userId == createdUserId
    And match response.data.name == 'Delete Test'
    And match response.data.email == testEmail

  Scenario: Deleted user no longer exists and GET returns 404
    * def testEmail = 'delete-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Delete Test', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    # Delete the user
    Given url baseUrl + '/users/' + createdUserId
    When method DELETE
    Then status 200

    # Verify the user no longer exists
    Given url baseUrl + '/users/' + createdUserId
    When method GET
    Then status 404
    And match response.status == 404
    And match response.title == 'Not Found'
