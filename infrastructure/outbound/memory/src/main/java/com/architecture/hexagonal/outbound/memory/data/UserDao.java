package com.architecture.hexagonal.outbound.memory.data;

import java.util.UUID;
import lombok.Data;

@Data
public class UserDao {
  UUID userId;
  String name;
  String email;
}
