Feature: Global test data pre-cleanup

  Scenario: Cleanup all constant test emails
    * configure afterScenario = null
    Given url __arg.baseUrl + '/users'
    When method GET
    Then status 200

    * def allEmails =
      """
      [
        'create-user@test.com',
        'schema-user@test.com',
        'get-all-users@test.com',
        'get-user-by-id@test.com',
        'head-user@test.com',
        'update-user@test.com',
        'updated-user@test.com',
        'patch-user@test.com',
        'patched-user@test.com',
        'delete-user@test.com',
        'e2e-flow-user@test.com',
        'e2e-flow-updated@test.com'
      ]
      """

    * def users = response.data
    * def baseUrlArg = __arg.baseUrl

    * eval
      """
      allEmails.forEach(function(email) {
        var matched = users.filter(function(u) { return u.email == email; });
        if (matched.length > 0) {
          karate.call('classpath:helpers/users/cleanup-user.feature', {
            baseUrl: baseUrlArg,
            userIdToDelete: matched[0].userId
          });
        }
      });
      """
