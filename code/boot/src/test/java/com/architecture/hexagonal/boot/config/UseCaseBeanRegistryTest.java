package com.architecture.hexagonal.boot.config;

import com.architecture.hexagonal.application.usecase.business.user.create.usecase.CreateUserUseCase;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.AnnotationMetadata;

@ExtendWith(MockitoExtension.class)
class UseCaseBeanRegistryTest {

  private static final String USE_CASE_BASE_PACKAGE =
      "com.architecture.hexagonal.application.usecase";

  private static final String CREATE_USER_USE_CASE = "CreateUserUseCase";

  @Mock BeanDefinitionRegistry registry;

  @Mock AnnotationMetadata importingClassMetadata;

  @Mock BeanDefinition beanDefinition;

  private MockedConstruction<ClassPathScanningCandidateComponentProvider> mockedConstruction;

  @BeforeEach
  void setUp() {
    mockedConstruction =
        Mockito.mockConstruction(
            ClassPathScanningCandidateComponentProvider.class,
            (mock, context) ->
                Mockito.when(mock.findCandidateComponents(USE_CASE_BASE_PACKAGE))
                    .thenReturn(Set.of(beanDefinition)));
  }

  @AfterEach
  void tearDown() {
    mockedConstruction.close();
  }

  @Test
  void registerBeanDefinitions_shouldRegisterUseCaseBeans_whenCandidatesAreFound() {
    Mockito.when(beanDefinition.getBeanClassName()).thenReturn(CreateUserUseCase.class.getName());
    Mockito.when(registry.containsBeanDefinition(CREATE_USER_USE_CASE)).thenReturn(false);

    final UseCaseBeanRegistry useCaseBeanRegistry = new UseCaseBeanRegistry();
    useCaseBeanRegistry.registerBeanDefinitions(importingClassMetadata, registry);

    final ArgumentCaptor<RootBeanDefinition> beanDefinitionCaptor =
        ArgumentCaptor.forClass(RootBeanDefinition.class);

    Mockito.verify(registry).containsBeanDefinition(CREATE_USER_USE_CASE);
    Mockito.verify(registry)
        .registerBeanDefinition(Mockito.eq(CREATE_USER_USE_CASE), beanDefinitionCaptor.capture());

    AssertionsForClassTypes.assertThat(beanDefinitionCaptor.getValue().getBeanClass())
        .isEqualTo(CreateUserUseCase.class);
  }

  @Test
  void registerBeanDefinitions_shouldNotRegisterDuplicateBeans_whenBeanAlreadyExists() {
    Mockito.when(beanDefinition.getBeanClassName()).thenReturn(CreateUserUseCase.class.getName());
    Mockito.when(registry.containsBeanDefinition(CREATE_USER_USE_CASE)).thenReturn(true);

    final UseCaseBeanRegistry useCaseBeanRegistry = new UseCaseBeanRegistry();
    useCaseBeanRegistry.registerBeanDefinitions(importingClassMetadata, registry);

    Mockito.verify(registry).containsBeanDefinition(CREATE_USER_USE_CASE);
    Mockito.verify(registry, Mockito.never())
        .registerBeanDefinition(Mockito.anyString(), Mockito.any(RootBeanDefinition.class));
  }

  @Test
  void registerBeanDefinitions_shouldThrowRuntimeException_whenClassCannotBeLoaded() {
    Mockito.when(beanDefinition.getBeanClassName()).thenReturn("test");

    final UseCaseBeanRegistry useCaseBeanRegistry = new UseCaseBeanRegistry();

    Mockito.verify(registry, Mockito.never())
        .registerBeanDefinition(Mockito.anyString(), Mockito.any(RootBeanDefinition.class));

    AssertionsForClassTypes.assertThatThrownBy(
            () -> useCaseBeanRegistry.registerBeanDefinitions(importingClassMetadata, registry))
        .isInstanceOf(RuntimeException.class)
        .hasCauseInstanceOf(ClassNotFoundException.class);
  }
}
