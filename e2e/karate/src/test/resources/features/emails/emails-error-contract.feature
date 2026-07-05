Feature: Email Block Rules Contract - Response validation

  Background:
    * url baseUrl

  Scenario: GET /emails/blocks returns valid response with correct structure
    Given url baseUrl + '/emails/blocks'
    When method GET
    Then status 200
    And match response.status == 200
    And match response.date == '#string'
    And match response.data == { email: '#array', host: '#array', tld: '#array', domain: '#array', username: '#array' }
    And match response.pagination == '#null'

  Scenario: Block rules arrays contain only string elements
    Given url baseUrl + '/emails/blocks'
    When method GET
    Then status 200
    * def data = response.data
    And match data.email == '#array'
    And match data.host == '#array'
    And match data.tld == '#array'
    And match data.domain == '#array'
    And match data.username == '#array'

  Scenario: Block rules arrays are consistent across calls
    Given url baseUrl + '/emails/blocks'
    When method GET
    Then status 200
    * def firstCall = response.data

    Given url baseUrl + '/emails/blocks'
    When method GET
    Then status 200
    * def secondCall = response.data
    And match firstCall.host == secondCall.host
    And match firstCall.domain == secondCall.domain
    And match firstCall.tld == secondCall.tld
    And match firstCall.email == secondCall.email
    And match firstCall.username == secondCall.username

  Scenario: Block rules response contains configured host values
    Given url baseUrl + '/emails/blocks'
    When method GET
    Then status 200
    And match response.data.host contains 'banned'
    And match response.data.host contains 'malicious'
    And match response.data.host contains 'spam-host'

  Scenario: Block rules response contains configured domain values
    Given url baseUrl + '/emails/blocks'
    When method GET
    Then status 200
    And match response.data.domain contains 'blocked.org'
    And match response.data.domain contains 'malware.com'
