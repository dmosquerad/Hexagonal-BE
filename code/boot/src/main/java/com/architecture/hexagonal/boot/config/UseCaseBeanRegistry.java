package com.architecture.hexagonal.boot.config;

import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

@Slf4j
public class UseCaseBeanRegistry implements ImportBeanDefinitionRegistrar {

  @Override
  public void registerBeanDefinitions(
      final @NonNull AnnotationMetadata importingClassMetadata,
      final @NonNull BeanDefinitionRegistry registry) {

    ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(
        new RegexPatternTypeFilter(Pattern.compile(".*\\.usecase\\.impl\\..*")));

    scanner
        .findCandidateComponents("com.architecture.hexagonal.application.usecase")
        .forEach(
            beanDefinition -> {
              try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                String beanName = clazz.getSimpleName();

                if (!registry.containsBeanDefinition(beanName)) {
                  registry.registerBeanDefinition(beanName, new RootBeanDefinition(clazz));
                }
              } catch (ClassNotFoundException e) {
                log.error("Failed to load UseCase class: {}", beanDefinition.getBeanClassName(), e);
                throw new RuntimeException(e);
              }
            });
  }
}
