Feature: Declare RabbitMQ test queues and bind to exchanges

  Scenario: Setup messaging test queues
    * def mgmtUrl = __arg.rabbitMqManagementUrl
    * def auth = __arg.rabbitMqAuth

    # ── user.created ───────────────────────────────────────────────────────────
    Given url mgmtUrl + '/api/queues/%2F/e2e-user-created-queue'
    And header Authorization = auth
    And request { auto_delete: false, durable: false }
    When method PUT
    Then assert responseStatus == 201 || responseStatus == 204

    Given url mgmtUrl + '/api/bindings/%2F/e/user.created/q/e2e-user-created-queue'
    And header Authorization = auth
    And request { routing_key: '#' }
    When method POST
    Then assert responseStatus == 201 || responseStatus == 204

    # ── user.updated ───────────────────────────────────────────────────────────
    Given url mgmtUrl + '/api/queues/%2F/e2e-user-updated-queue'
    And header Authorization = auth
    And request { auto_delete: false, durable: false }
    When method PUT
    Then assert responseStatus == 201 || responseStatus == 204

    Given url mgmtUrl + '/api/bindings/%2F/e/user.updated/q/e2e-user-updated-queue'
    And header Authorization = auth
    And request { routing_key: '#' }
    When method POST
    Then assert responseStatus == 201 || responseStatus == 204

    # ── user.deleted ───────────────────────────────────────────────────────────
    Given url mgmtUrl + '/api/queues/%2F/e2e-user-deleted-queue'
    And header Authorization = auth
    And request { auto_delete: false, durable: false }
    When method PUT
    Then assert responseStatus == 201 || responseStatus == 204

    Given url mgmtUrl + '/api/bindings/%2F/e/user.deleted/q/e2e-user-deleted-queue'
    And header Authorization = auth
    And request { routing_key: '#' }
    When method POST
    Then assert responseStatus == 201 || responseStatus == 204
