'use strict';

require('source-map-support/register');
var generatorReactSdk = require('@asyncapi/generator-react-sdk');
var modelina = require('@asyncapi/modelina');
var jsxRuntime = require('/Users/dmosquera/JavaProyects/Hexagonal-BE/code/contract/inbound/messaging/kafka/node_modules/react/cjs/react-jsx-runtime.production.min.js');

const contractPackage = 'com.architecture.hexagonal.contract.inbound.messaging.kafka';
async function index ({
  asyncapi
}) {
  const javaGenerator = new modelina.JavaFileGenerator({
    presets: [modelina.JAVA_COMMON_PRESET, modelina.JAVA_JACKSON_PRESET]
  });
  const models = await javaGenerator.generateCompleteModels(asyncapi, {
    packageName: `${contractPackage}.model`
  });
  const operations = Object.values(asyncapi.channels()).map(channel => channel.subscribe()).filter(Boolean);
  if (operations.length === 0) {
    throw new Error('The Kafka AsyncAPI document must define at least one subscribe operation.');
  }
  const messages = operations.map(operation => {
    var _operation$json;
    const operationId = operation.json('operationId');
    const messageName = (_operation$json = operation.json('message')) === null || _operation$json === void 0 ? void 0 : _operation$json.name;
    if (!operationId || !messageName) {
      throw new Error('Each Kafka subscribe operation must define an operationId and a named message.');
    }
    return {
      operationId,
      messageName
    };
  });
  const packagePath = contractPackage.replaceAll('.', '/');
  const serviceSource = [`package ${contractPackage};`, '', ...[...new Set(messages.map(({
    messageName
  }) => messageName))].sort().map(messageName => `import ${contractPackage}.model.${messageName};`), 'import java.util.Map;', '', 'public interface UserEventsConsumerService {', '', ...messages.map(({
    operationId,
    messageName
  }) => `  void ${operationId}(${messageName} payload, Map<String, Object> headers);`), '}', ''].join('\n');
  return [...models.map(model => /*#__PURE__*/jsxRuntime.jsx(generatorReactSdk.File, {
    name: `${packagePath}/model/${model.modelName}.java`,
    children: /*#__PURE__*/jsxRuntime.jsx(generatorReactSdk.Text, {
      children: model.result
    })
  })), /*#__PURE__*/jsxRuntime.jsx(generatorReactSdk.File, {
    name: `${packagePath}/UserEventsConsumerService.java`,
    children: /*#__PURE__*/jsxRuntime.jsx(generatorReactSdk.Text, {
      children: serviceSource
    })
  })];
}

module.exports = index;
//# sourceMappingURL=index.js.map
