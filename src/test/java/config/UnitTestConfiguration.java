package config;

import net.lamad.spring.sdc.DynamicConfigurationBeanPostProcessor;
import net.lamad.spring.sdc.DynamicConfigurationSetterBeanPostProcessor;
import net.lamad.spring.sdc.watcher.JsonWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.arkena.springconfig.service")
public class UnitTestConfiguration {

  @Bean
  public DynamicConfigurationBeanPostProcessor dynamicConfiguration() {
    return new DynamicConfigurationSetterBeanPostProcessor();
  }

  @Bean
  public JsonWatcher getJsonWatcher() {
    JsonWatcher watcher = new JsonWatcher();
    watcher.setconfigurationFile("classpath:configuration.json");
    return watcher;
  }
}
