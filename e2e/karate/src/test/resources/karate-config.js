function fn() {
  var configuredBaseUrl = karate.properties['baseUrl'];
  var config = {
    baseUrl: configuredBaseUrl || 'http://localhost:8080/api'
  };

  karate.callSingle('classpath:helpers/users/setup.feature', { baseUrl: config.baseUrl });

  karate.configure('afterScenario', function() {
    var cleanupUserId = karate.get('cleanupUserId');
    if (cleanupUserId) {
      karate.call('classpath:helpers/users/cleanup-user.feature', {
        baseUrl: config.baseUrl,
        userIdToDelete: cleanupUserId
      });
    }

    // Safety-net rollback for mid-scenario failures: remove any constant test emails left behind
    karate.call('classpath:helpers/users/setup.feature', { baseUrl: config.baseUrl });
  });

  return config;
}
