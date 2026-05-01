Feature: Purge all messages from a RabbitMQ test queue

  Scenario: Purge queue
    * def queueName = __arg.queueName
    * def mgmtUrl = __arg.rabbitMqManagementUrl

    Given url mgmtUrl + '/api/queues/%2F/' + queueName + '/contents'
    And header Authorization = rabbitMqAuth
    When method DELETE
    Then status 204
