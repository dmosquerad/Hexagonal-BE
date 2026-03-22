package com.architecture.hexagonal.domain.service.factory.vo;

import com.architecture.hexagonal.domain.data.vo.EmailVo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVoFactory {

  private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.*)@([^.]+)\\.(.*)$");

  public static EmailVo from(final String email) {
      final Matcher matcher = EMAIL_PATTERN.matcher(email);
      if (!matcher.matches()) {
        throw new IllegalArgumentException("Invalid email format");
      }
      return EmailVo.builder()
              .username(matcher.group(1))
              .host(matcher.group(2))
              .tld(matcher.group(3))
              .build();
  }

}
