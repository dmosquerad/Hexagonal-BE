import { File, Text } from '@asyncapi/generator-react-sdk';
import {
  JavaFileGenerator,
  JAVA_COMMON_PRESET,
  JAVA_JACKSON_PRESET,
} from '@asyncapi/modelina';

const contractPackage = 'com.architecture.hexagonal.contract.outbound.rabbitmq';

export default async function ({ asyncapi }) {
  const javaGenerator = new JavaFileGenerator({
    presets: [JAVA_COMMON_PRESET, JAVA_JACKSON_PRESET],
  });
  const models = await javaGenerator.generateCompleteModels(asyncapi, {
    packageName: `${contractPackage}.model`,
  });
  const operations = Object.values(asyncapi.channels())
    .map((channel) => channel.publish())
    .filter(Boolean);
  if (operations.length === 0) {
    throw new Error('The RabbitMQ AsyncAPI document must define at least one publish operation.');
  }

  const messages = operations.map((operation) => {
    const operationId = operation.json('operationId');
    const messageName = operation.json('message')?.name;

    if (!operationId || !messageName) {
      throw new Error(
        'Each RabbitMQ publish operation must define an operationId and a named message.',
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
    '',
    'public interface UserEventsProducer {',
    '',
    ...messages.map(({ operationId, messageName }) =>
      `  boolean ${operationId}(${messageName} payload);`),
    '}',
    '',
  ].join('\n');

  return [
    ...models.map((model) => (
      <File name={`${packagePath}/model/${model.modelName}.java`}>
        <Text>{model.result}</Text>
      </File>
    )),
    <File name={`${packagePath}/UserEventsProducer.java`}>
      <Text>{serviceSource}</Text>
    </File>,
  ];
}
