Feature: Patch User Partially - PATCH /users/{uuid}

  Background:
    * url baseUrl

  Scenario: Patch only name returns 200 with updated name and original email
    * def testEmail = 'patch-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Patch Test', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    Given url baseUrl + '/users/' + createdUserId
    And header Content-Type = 'application/json'
    And request { name: 'Patched Name' }
    When method PATCH
    Then status 200
    And match response.status == 200
    And match response.data.userId == createdUserId
    And match response.data.name == 'Patched Name'
    And match response.data.email == testEmail

  Scenario: Patch only email returns 200 with updated email and original name
    * def testEmail = 'patch-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Patch Test', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    * def patchedEmail = 'patched-user@test.com'
    Given url baseUrl + '/users/' + createdUserId
    And header Content-Type = 'application/json'
    And request { email: '#(patchedEmail)' }
    When method PATCH
    Then status 200
    And match response.status == 200
    And match response.data.userId == createdUserId
    And match response.data.name == 'Patch Test'
    And match response.data.email == patchedEmail
