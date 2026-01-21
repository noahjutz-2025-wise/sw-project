package com.swdev.springbootproject.component;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Locator;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtKeyLocator implements Locator<Key> {
  @Value("${jwt.secret#{null}")
  private String key;

  @Override
  public Key locate(Header header) {
    return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
  }
}
