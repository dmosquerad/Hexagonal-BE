const fs = require('fs');
const path = require('path');
const Module = require('module');

if (!process.env.npm_execpath) {
  throw new Error('Run the AsyncAPI generator through npm exec.');
}

const npmModuleDirectory = path.resolve(path.dirname(process.env.npm_execpath), '../..');
process.env.NODE_PATH = [process.env.NODE_PATH, npmModuleDirectory].filter(Boolean).join(path.delimiter);
Module._initPaths();

const Generator = require('@asyncapi/generator');
const [asyncapiFile, outputDirectory, templateDirectory, packageDirectory] =
  process.argv.slice(2);

if (!asyncapiFile || !outputDirectory || !templateDirectory || !packageDirectory) {
  throw new Error(
    'Expected the AsyncAPI file, output directory, template directory, and package directory.',
  );
}

fs.mkdirSync(path.join(outputDirectory, packageDirectory, 'model'), { recursive: true });

const generator = new Generator(templateDirectory, outputDirectory, { forceWrite: true });
generator.generateFromFile(asyncapiFile).catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
