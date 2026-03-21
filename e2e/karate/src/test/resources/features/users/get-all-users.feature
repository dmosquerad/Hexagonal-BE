Feature: Get All Users - GET /users

  Background:
    * url baseUrl

  Scenario: GET /users returns 200 with user array and valid schema
    # Create a user to guarantee there is at least one item
    * def testEmail = 'get-all-users@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'GetAll Test', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdId = response.data.userId
    * def cleanupUserId = createdId

    # Retrieve all users
    Given url baseUrl + '/users'
    When method GET
    Then status 200
    And match response.status == 200
    And match response.date == '#string'
    And match response.data == '#array'
    And match each response.data == { userId: '#string', name: '#string', email: '#string' }

    # Verify the newly created user is present in the list
    And match response.data contains { userId: '#(createdId)', name: 'GetAll Test', email: '#(testEmail)' }
