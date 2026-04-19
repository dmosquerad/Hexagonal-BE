Feature: Test User Cleanup

  Scenario: Delete test user if it exists
    * configure afterScenario = null
    * def targetUserId = __arg.userIdToDelete
    * if (!targetUserId) karate.abort()
    Given url __arg.baseUrl + '/users/' + targetUserId
    When method DELETE
    Then assert responseStatus == 200 || responseStatus == 404
