function fn() {
  var configuredBaseUrl = karate.properties['baseUrl'];
  var configuredRabbitMqManagementUrl = karate.properties['rabbitMqManagementUrl'];
  var configuredRabbitMqUser = karate.properties['rabbitMqUser'];
  var configuredRabbitMqPass = karate.properties['rabbitMqPass'];

  var rabbitMqUser = configuredRabbitMqUser || 'guest';
  var rabbitMqPass = configuredRabbitMqPass || 'guest';
  var authPlain = new java.lang.String(rabbitMqUser + ':' + rabbitMqPass);
  
  var config = {
    baseUrl: configuredBaseUrl || 'http://localhost:8080/api',
    rabbitMqManagementUrl: configuredRabbitMqManagementUrl || 'http://localhost:15672',
    rabbitMqAuth: 'Basic ' + java.util.Base64.getEncoder().encodeToString(authPlain.getBytes())
  };

  karate.callSingle('classpath:helpers/users/setup.feature', { baseUrl: config.baseUrl });
  karate.callSingle('classpath:helpers/rabbitmq/setup-queues.feature', { rabbitMqManagementUrl: config.rabbitMqManagementUrl, rabbitMqAuth: config.rabbitMqAuth });

  karate.configure('afterScenario', function() {
    var cleanupUserId = karate.get('cleanupUserId');
    if (cleanupUserId) {
      karate.call('classpath:helpers/users/cleanup.feature', {
        baseUrl: config.baseUrl,
        userIdToDelete: cleanupUserId
      });
    }

    // Safety-net rollback for mid-scenario failures: remove any constant test emails left behind
    karate.call('classpath:helpers/users/setup.feature', { baseUrl: config.baseUrl });
  });

  return config;
}
