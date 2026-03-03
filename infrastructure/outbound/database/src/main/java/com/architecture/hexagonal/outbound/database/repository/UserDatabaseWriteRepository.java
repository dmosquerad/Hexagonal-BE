package com.architecture.hexagonal.outbound.database.repository;

import com.architecture.hexagonal.outbound.database.data.UserDao;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDatabaseWriteRepository extends JpaRepository<UserDao, UUID>
{
  UserDao save(UserDao userDao);
}
