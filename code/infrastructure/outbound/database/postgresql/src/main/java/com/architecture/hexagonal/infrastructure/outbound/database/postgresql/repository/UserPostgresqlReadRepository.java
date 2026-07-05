package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.repository;

import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.data.UserDao;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserPostgresqlReadRepository
    extends JpaRepository<UserDao, UUID>, JpaSpecificationExecutor<UserDao> {

  Optional<UserDao> findByUserId(UUID uuid);
}
