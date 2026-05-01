Feature: Consume a message from a RabbitMQ test queue

  Scenario: Consume message
    * def queueName = __arg.queueName
    * def mgmtUrl = __arg.rabbitMqManagementUrl

    * retry until response.length > 0
      Given url mgmtUrl + '/api/queues/%2F/' + queueName + '/get'
      And header Authorization = rabbitMqAuth
      And request { count: 1, ackmode: 'ack_requeue_false', encoding: 'auto' }
      When method POST
      Then status 200

    * match response[0] != null
    * def payloadRaw = response[0].payload
    * json payload = payloadRaw
