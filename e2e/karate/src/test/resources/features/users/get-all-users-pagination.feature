Feature: Get All Users with Pagination - GET /users?page=&size=

  Background:
    * url baseUrl

  Scenario: Default pagination returns page 0 with size 100
    Given url baseUrl + '/users'
    When method GET
    Then status 200
    And match response.pagination.page == 0
    And match response.pagination.size == 100

  Scenario: Custom page and size are reflected in pagination response
    * def testEmail = 'pagination-custom@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Pagination Test', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdId = response.data.userId
    * def cleanupUserId = createdId

    Given url baseUrl + '/users'
    And param page = 0
    And param size = 5
    When method GET
    Then status 200
    And match response.status == 200
    And match response.pagination.page == 0
    And match response.pagination.size == 5
    * assert response.pagination.totalElements >= 0
    * assert response.pagination.totalPages >= 1

  Scenario: Page beyond total pages returns empty data array
    Given url baseUrl + '/users'
    And param page = 9999
    And param size = 100
    When method GET
    Then status 200
    And match response.status == 200
    And match response.data == '#[]'
    And match response.pagination.page == 9999
    * assert response.pagination.totalElements >= 0
