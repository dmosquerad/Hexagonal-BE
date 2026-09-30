@outbox
Feature: Outbox publishes each user event once

  Background:
    * def queueName = 'e2e-outbox-created-queue'
    * call read('classpath:helpers/rabbitmq/purge-queue.feature') { queueName: '#(queueName)', rabbitMqManagementUrl: '#(rabbitMqManagementUrl)', rabbitMqAuth: '#(rabbitMqAuth)' }

  Scenario: User creation is persisted and published once
    * def testEmail = 'outbox-publishes-once@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Outbox Publishes Once', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    * def result = call read('classpath:helpers/rabbitmq/consume-user-message.feature') { queueName: '#(queueName)', rabbitMqManagementUrl: '#(rabbitMqManagementUrl)', rabbitMqAuth: '#(rabbitMqAuth)', userId: '#(createdUserId)' }
    * match result.payload.data contains { userId: '#(createdUserId)', name: 'Outbox Publishes Once' }
    * match result.payload.data.email.email == testEmail