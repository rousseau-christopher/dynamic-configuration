package config;

import org.arkena.springconfig.DynamicConfigurationBeanPostProcessor;
import org.arkena.springconfig.watcher.JsonWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.arkena.springconfig.service")
public class UnitTestConfiguration {

  @Bean
  public DynamicConfigurationBeanPostProcessor dynamicConfiguration() {
    return new DynamicConfigurationBeanPostProcessor();
  }

  @Bean
  public JsonWatcher getJsonWatcher() {
    JsonWatcher watcher = new JsonWatcher();
    watcher.setconfigurationFile("classpath:configuration.json");
    return watcher;
  }
}
