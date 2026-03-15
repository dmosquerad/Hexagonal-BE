package com.architecture.hexagonal.infrastructure.outbound.database.repository;

import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserDatabaseWriteRepository extends JpaRepository<UserDao, UUID>
{
  UserDao save(UserDao userDao);
}
