package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.application.input.query.GetAllUserQuery;
import com.architecture.hexagonal.application.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.application.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.domain.model.entity.predicate.UserPredicate;
import com.architecture.hexagonal.domain.service.EmailPolicy;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetAllUsersUseCase implements GetAllUsersUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;

  @Override
  @Transactional(readOnly = true)
  public Set<User> execute(final GetAllUserQuery getAllUserQuery) {
    final Set<User> users = userRepositoryReadPort.getAllUsers();
    final String host = getAllUserQuery.getHost();
    final Boolean blockHost = getAllUserQuery.getBlockHost();

    if (StringUtils.isBlank(host)) {
      return users;
    }

    final Predicate<User> predicate = Boolean.TRUE.equals(blockHost)
        ? UserPredicate.hasEmailHost(host).and(EmailPolicy::filterAllowedEmailHost)
        : UserPredicate.hasEmailHost(host);
        
    return users.stream()
        .filter(predicate)
        .collect(Collectors.toSet());
  }
}
