Feature: User Edge Cases - Boundary values and special characters

  Background:
    * url baseUrl

  Scenario: Create user with special characters in name (accents and umlauts)
    * def testEmail = 'edge-special-chars@test.com'
    * def specialName = 'José María García-López Müller'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: '#(specialName)', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId
    And match response.data.name == specialName
    And match response.data.email == testEmail

  Scenario: Create user with email containing special valid characters
    * def specialEmail = 'first.last+tag@sub.domain.com'
    * def testName = 'Special Email User'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: '#(testName)', email: '#(specialEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId
    And match response.data.email == specialEmail
    And match response.data.name == testName

  Scenario: Create user with name containing numbers and symbols
    * def testEmail = 'edge-alphanumeric@test.com'
    * def numericName = 'User123 (Test) #42-ABC_DEF'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: '#(numericName)', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId
    And match response.data.name == numericName

  Scenario: Create user with name containing multiple consecutive spaces
    * def testEmail = 'edge-whitespace@test.com'
    * def nameWithWhitespace = 'Name  With   Multiple    Spaces'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: '#(nameWithWhitespace)', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId
    And match response.data.name == nameWithWhitespace

  Scenario: Email with hyphenated domain parts
    * def hyphenatedEmail = 'user@sub-domain-name.com'
    * def testName = 'Hyphenated Email User'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: '#(testName)', email: '#(hyphenatedEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId
    And match response.data.email == hyphenatedEmail

  Scenario: Filter users by host with hyphenated domain
    * def testEmail = 'edge-filter-hyphen@test-domain.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Hyphen Domain User', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    Given url baseUrl + '/users'
    And param host = 'test-domain'
    When method GET
    Then status 200
    And match response.data[*].email contains testEmail
    And match response.data[*].userId contains createdUserId
