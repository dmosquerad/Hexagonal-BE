Feature: Consume a user message from RabbitMQ

  Scenario: Consume the event for a user exactly once
    * def queueName = __arg.queueName
    * def mgmtUrl = __arg.rabbitMqManagementUrl
    * def expectedUserId = __arg.userId
    * configure retry = { count: 30, interval: 1000 }

    * retry until response.length > 0 && JSON.parse(response[0].payload).data.userId == expectedUserId
      Given url mgmtUrl + '/api/queues/%2F/' + queueName + '/get'
      And header Authorization = rabbitMqAuth
      And request { count: 1, ackmode: 'ack_requeue_false', encoding: 'auto' }
      When method POST
      Then status 200

    * json payload = response[0].payload

      Given url mgmtUrl + '/api/queues/%2F/' + queueName + '/get'
      And header Authorization = rabbitMqAuth
      And request { count: 100, ackmode: 'ack_requeue_false', encoding: 'auto' }
      When method POST
      Then status 200
      * def duplicateMessages = response.filter(function(item) { return JSON.parse(item.payload).data.userId == expectedUserId; })
      * match duplicateMessages == '#[0]'