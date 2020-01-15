package com.cashfree.lib.config;

import lombok.Data;
import javax.validation.constraints.NotNull;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ConfigParams {
  @NotNull
  private String testEndpoint;

  @NotNull
  private String prodEndpoint;
}
