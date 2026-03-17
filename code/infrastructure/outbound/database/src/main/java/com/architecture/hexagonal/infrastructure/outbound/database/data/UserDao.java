package com.architecture.hexagonal.infrastructure.outbound.database.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "users")
public class UserDao {

  @Id
  @GeneratedValue
  @UuidGenerator
  UUID userId;

  @Column(nullable = false)
  String name;

  @Column(nullable = false, unique = true)
  String email;
  
}
