package com.architecture.hexagonal.application.usecase;

import com.architecture.hexagonal.application.cqrs.query.request.GetAllUserQuery;
import com.architecture.hexagonal.application.port.in.GetAllUsersUseCasePort;
import com.architecture.hexagonal.application.port.out.EmailConfigurationPort;
import com.architecture.hexagonal.application.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.domain.model.entity.predicate.UserPredicate;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.domain.service.EmailBlockPolicy;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetAllUsersUseCase implements GetAllUsersUseCasePort {

  private final UserRepositoryReadPort userRepositoryReadPort;
  private final EmailConfigurationPort emailConfigurationPort;

  @Override
  @Transactional(readOnly = true)
  public Set<User> execute(final GetAllUserQuery getAllUserQuery) {
    final Set<User> users = userRepositoryReadPort.getAllUsers();
    final String host = getAllUserQuery.getHost();
    final Boolean blockEmail = getAllUserQuery.getBlockEmail();

    final Set<User> usersByHost = StringUtils.isBlank(host)
        ? users
        : users.stream()
            .filter(UserPredicate.hasEmailHost(host))
            .collect(Collectors.toSet());

    if (Objects.isNull(blockEmail)) {
      return usersByHost;
    }

    final Set<EmailVo> emailVos = usersByHost.stream()
        .map(User::getEmail)
        .collect(Collectors.toSet());

    final EmailBlockRulesVo rules = emailConfigurationPort.getBlockedRules();
    final Set<EmailVo> filteredEmailVos = Boolean.TRUE.equals(blockEmail)
        ? emailVos.stream()
            .filter(email -> EmailBlockPolicy.isBlocked(email, rules))
            .collect(Collectors.toUnmodifiableSet())
        : emailVos.stream()
            .filter(email -> !EmailBlockPolicy.isBlocked(email, rules))
            .collect(Collectors.toUnmodifiableSet());

    return usersByHost.stream()
        .filter(user -> filteredEmailVos.contains(user.getEmail()))
        .collect(Collectors.toSet());
  }
}
