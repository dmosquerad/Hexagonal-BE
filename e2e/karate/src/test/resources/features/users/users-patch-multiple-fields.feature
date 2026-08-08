Feature: Patch User With Multiple Fields - PATCH /users/{uuid}

  Background:
    * url baseUrl

  Scenario: Patch both name and email returns 200 with both fields updated
    * def testEmail = 'patch-multi-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Original Name', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    * def patchedEmail = 'patch-multi-updated@test.com'
    Given url baseUrl + '/users/' + createdUserId
    And header Content-Type = 'application/json'
    And request { name: 'Patched Name', email: '#(patchedEmail)' }
    When method PATCH
    Then status 200
    And match response.status == 200
    And match response.data.userId == createdUserId
    And match response.data.name == 'Patched Name'
    And match response.data.email == patchedEmail

    Given url baseUrl + '/users/' + createdUserId
    When method GET
    Then status 200
    And match response.data.name == 'Patched Name'
    And match response.data.email == patchedEmail

  Scenario: Patch only email keeps original name unchanged
    * def testEmail = 'patch-multi-null-name@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Original Name', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    * def patchedEmail = 'patch-multi-null-email@test.com'
    Given url baseUrl + '/users/' + createdUserId
    And header Content-Type = 'application/json'
    And request { email: '#(patchedEmail)' }
    When method PATCH
    Then status 200
    And match response.data.name == 'Original Name'
    And match response.data.email == patchedEmail

  Scenario: Patch with empty object keeps both original values
    * def testEmail = 'patch-multi-empty@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Original Name', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    Given url baseUrl + '/users/' + createdUserId
    And header Content-Type = 'application/json'
    And request {}
    When method PATCH
    Then status 200
    And match response.data.name == 'Original Name'
    And match response.data.email == testEmail
