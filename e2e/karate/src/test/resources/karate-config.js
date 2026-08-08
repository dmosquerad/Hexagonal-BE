function fn() {

  function cleanupOutboxByAggregateId(mongoUri, databaseName, aggregateId) {
    var ConnectionString = Java.type('com.mongodb.ConnectionString');
    var MongoClientSettings = Java.type('com.mongodb.MongoClientSettings');
    var MongoClients = Java.type('com.mongodb.client.MongoClients');
    var Document = Java.type('org.bson.Document');
    var UuidRepresentation = Java.type('org.bson.UuidRepresentation');
    var UUID = Java.type('java.util.UUID');

    var settings = MongoClientSettings.builder()
      .applyConnectionString(new ConnectionString(mongoUri))
      .uuidRepresentation(UuidRepresentation.STANDARD)
      .build();
    var mongoClient = MongoClients.create(settings);
    try {
      mongoClient
        .getDatabase(databaseName)
        .getCollection('outbox')
        .deleteMany(new Document('aggregateId', UUID.fromString(aggregateId)));
    } finally {
      mongoClient.close();
    }
  }

  var configuredBaseUrl = karate.properties['baseUrl'];
  var configuredRabbitMqManagementUrl = karate.properties['rabbitMqManagementUrl'];
  var configuredRabbitMqUser = karate.properties['rabbitMqUser'];
  var configuredRabbitMqPass = karate.properties['rabbitMqPass'];
  var configuredMongoUri = karate.properties['mongoUri'];
  var configuredMongoDatabase = karate.properties['mongoDatabase'];

  var rabbitMqUser = configuredRabbitMqUser || 'guest';
  var rabbitMqPass = configuredRabbitMqPass || 'guest';
  var authPlain = new java.lang.String(rabbitMqUser + ':' + rabbitMqPass);
  
  var HashMap = Java.type('java.util.HashMap');
  var config = new HashMap();
  config.put('baseUrl', configuredBaseUrl || 'http://localhost:8080/api');
  config.put('rabbitMqManagementUrl', configuredRabbitMqManagementUrl || 'http://localhost:15672');
  config.put('rabbitMqAuth', 'Basic ' + java.util.Base64.getEncoder().encodeToString(authPlain.getBytes()));
  config.put('mongoUri', configuredMongoUri || 'mongodb://root:example@localhost:27017/?authSource=admin');
  config.put('mongoDatabase', configuredMongoDatabase || 'mydatabase');

  karate.callSingle('classpath:helpers/users/setup.feature', { baseUrl: config.get('baseUrl') });
  karate.callSingle('classpath:helpers/rabbitmq/setup-queues.feature', { rabbitMqManagementUrl: config.get('rabbitMqManagementUrl'), rabbitMqAuth: config.get('rabbitMqAuth') });

  karate.configure('afterScenario', function() {
    var cleanupUserId = karate.get('cleanupUserId');
    if (cleanupUserId) {
      karate.call('classpath:helpers/users/cleanup.feature', {
        baseUrl: config.get('baseUrl'),
        userIdToDelete: cleanupUserId
      });
      cleanupOutboxByAggregateId(config.get('mongoUri'), config.get('mongoDatabase'), cleanupUserId);
    }

    // Safety-net rollback for mid-scenario failures: remove any constant test emails left behind
    karate.call('classpath:helpers/users/setup.feature', { baseUrl: config.get('baseUrl') });
  });

  return config;
}
