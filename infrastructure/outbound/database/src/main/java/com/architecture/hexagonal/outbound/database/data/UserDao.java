package com.architecture.hexagonal.outbound.database.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserDao {

  @Id
  UUID userId = UUID.randomUUID();

  @Column(nullable = false)
  String name;

  @Column(nullable = false, unique = true)
  String email;
  
}
