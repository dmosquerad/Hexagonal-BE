Feature: Get Email Block Rules - GET /emails/blocks

  Background:
    * url baseUrl

  Scenario: GET /emails/blocks returns 200 with valid schema
    Given url baseUrl + '/emails/blocks'
    When method GET
    Then status 200
    And match response.status == 200
    And match response.date == '#string'
    And match response == { date: '#string', status: 200, data: { email: '#array', host: '#array', tld: '#array', domain: '#array', username: '#array' } }

  Scenario: Block rules contain the configured values
    Given url baseUrl + '/emails/blocks'
    When method GET
    Then status 200
    And match response.data.host contains 'banned'
    And match response.data.host contains 'malicious'
    And match response.data.host contains 'spam-host'
    And match response.data.domain contains 'blocked.org'
    And match response.data.domain contains 'malware.com'
    And match response.data.username contains 'test'
    And match response.data.username contains 'spam'
    And match response.data.username contains 'fraud'
