Feature: Update User Fully - PUT /users/{uuid}

  Background:
    * url baseUrl

  Scenario: Update existing user returns 200 with updated data
    * def testEmail = 'update-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Update Test', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    * def updatedEmail = 'updated-user@test.com'
    Given url baseUrl + '/users/' + createdUserId
    And header Content-Type = 'application/json'
    And request { name: 'Updated Name', email: '#(updatedEmail)' }
    When method PUT
    Then status 200
    And match response.status == 200
    And match response.date == '#string'
    And match response.data.userId == createdUserId
    And match response.data.name == 'Updated Name'
    And match response.data.email == updatedEmail
