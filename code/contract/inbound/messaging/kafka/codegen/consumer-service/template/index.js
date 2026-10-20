import { File, Text } from '@asyncapi/generator-react-sdk';
import {
  JavaFileGenerator,
  JAVA_COMMON_PRESET,
  JAVA_JACKSON_PRESET,
} from '@asyncapi/modelina';

const contractPackage = 'com.architecture.hexagonal.contract.inbound.messaging.kafka';

export default async function ({ asyncapi }) {
  const javaGenerator = new JavaFileGenerator({
    presets: [JAVA_COMMON_PRESET, JAVA_JACKSON_PRESET],
  });
  const models = await javaGenerator.generateCompleteModels(asyncapi, {
    packageName: `${contractPackage}.model`,
  });
  const operations = Object.values(asyncapi.channels())
    .map((channel) => channel.subscribe())
    .filter(Boolean);
  if (operations.length === 0) {
    throw new Error('The Kafka AsyncAPI document must define at least one subscribe operation.');
  }

  const messages = operations.map((operation) => {
    const operationId = operation.json('operationId');
    const messageName = operation.json('message')?.name;

    if (!operationId || !messageName) {
      throw new Error(
        'Each Kafka subscribe operation must define an operationId and a named message.',
      );
    }

    return { operationId, messageName };
  });

  const packagePath = contractPackage.replaceAll('.', '/');
  const serviceSource = [
    `package ${contractPackage};`,
    '',
    ...[...new Set(messages.map(({ messageName }) => messageName))]
      .sort()
      .map((messageName) => `import ${contractPackage}.model.${messageName};`),
    'import java.util.Map;',
    '',
    'public interface UserEventsConsumerService {',
    '',
    ...messages.map(({ operationId, messageName }) =>
      `  void ${operationId}(${messageName} payload, Map<String, Object> headers);`),
    '}',
    '',
  ].join('\n');

  return [
    ...models.map((model) => (
      <File name={`${packagePath}/model/${model.modelName}.java`}>
        <Text>{model.result}</Text>
      </File>
    )),
    <File name={`${packagePath}/UserEventsConsumerService.java`}>
      <Text>{serviceSource}</Text>
    </File>,
  ];
}
