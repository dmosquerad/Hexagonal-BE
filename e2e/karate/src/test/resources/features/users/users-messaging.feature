@messaging
Feature: User Messaging - Spring Cloud Stream events via RabbitMQ

  Background:
    * def mgmtUrl = rabbitMqManagementUrl
    * def createdQueue = 'e2e-user-created-queue'
    * def updatedQueue = 'e2e-user-updated-queue'
    * def deletedQueue = 'e2e-user-deleted-queue'

  Scenario: POST user publishes UserCreated event
    * call read('classpath:helpers/rabbitmq/purge-queue.feature') { queueName: '#(createdQueue)', rabbitMqManagementUrl: '#(mgmtUrl)', rabbitMqAuth: '#(rabbitMqAuth)' }

    * def testEmail = 'messaging-create-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Messaging Create User', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    * def result = call read('classpath:helpers/rabbitmq/consume-message.feature') { queueName: '#(createdQueue)', rabbitMqManagementUrl: '#(mgmtUrl)', rabbitMqAuth: '#(rabbitMqAuth)' }
    * match result.payload == { userId: '#(createdUserId)', name: 'Messaging Create User', email: '#(testEmail)' }

  Scenario: PUT user publishes UserUpdated event
    * call read('classpath:helpers/rabbitmq/purge-queue.feature') { queueName: '#(updatedQueue)', rabbitMqManagementUrl: '#(mgmtUrl)', rabbitMqAuth: '#(rabbitMqAuth)' }

    * def testEmail = 'messaging-update-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Messaging Update User', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    Given url baseUrl + '/users/' + createdUserId
    And header Content-Type = 'application/json'
    And request { name: 'Messaging Updated Name', email: '#(testEmail)' }
    When method PUT
    Then status 200

    * def result = call read('classpath:helpers/rabbitmq/consume-message.feature') { queueName: '#(updatedQueue)', rabbitMqManagementUrl: '#(mgmtUrl)', rabbitMqAuth: '#(rabbitMqAuth)' }
    * match result.payload == { userId: '#(createdUserId)', name: 'Messaging Updated Name', email: '#(testEmail)' }

  Scenario: PATCH user publishes UserUpdated event
    * call read('classpath:helpers/rabbitmq/purge-queue.feature') { queueName: '#(updatedQueue)', rabbitMqManagementUrl: '#(mgmtUrl)', rabbitMqAuth: '#(rabbitMqAuth)' }

    * def testEmail = 'messaging-patch-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Messaging Patch User', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    Given url baseUrl + '/users/' + createdUserId
    And header Content-Type = 'application/json'
    And request { name: 'Messaging Patched Name' }
    When method PATCH
    Then status 200

    * def result = call read('classpath:helpers/rabbitmq/consume-message.feature') { queueName: '#(updatedQueue)', rabbitMqManagementUrl: '#(mgmtUrl)', rabbitMqAuth: '#(rabbitMqAuth)' }
    * match result.payload == { userId: '#(createdUserId)', name: 'Messaging Patched Name', email: '#(testEmail)' }

  Scenario: DELETE user publishes UserDeleted event
    * call read('classpath:helpers/rabbitmq/purge-queue.feature') { queueName: '#(deletedQueue)', rabbitMqManagementUrl: '#(mgmtUrl)', rabbitMqAuth: '#(rabbitMqAuth)' }

    * def testEmail = 'messaging-delete-user@test.com'
    Given url baseUrl + '/users'
    And header Content-Type = 'application/json'
    And request { name: 'Messaging Delete User', email: '#(testEmail)' }
    When method POST
    Then status 200
    * def createdUserId = response.data.userId
    * def cleanupUserId = createdUserId

    Given url baseUrl + '/users/' + createdUserId
    When method DELETE
    Then status 200

    * def result = call read('classpath:helpers/rabbitmq/consume-message.feature') { queueName: '#(deletedQueue)', rabbitMqManagementUrl: '#(mgmtUrl)', rabbitMqAuth: '#(rabbitMqAuth)' }
    * match result.payload == { userId: '#(createdUserId)', name: 'Messaging Delete User', email: '#(testEmail)' }
