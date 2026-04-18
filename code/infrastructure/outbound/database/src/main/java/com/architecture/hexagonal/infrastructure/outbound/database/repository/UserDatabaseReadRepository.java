package com.architecture.hexagonal.infrastructure.outbound.database.repository;

import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDatabaseReadRepository extends JpaRepository<UserDao, UUID> {

  Optional<UserDao> findByUserId(UUID uuid);
}
