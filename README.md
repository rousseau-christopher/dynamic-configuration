## Dynamic Spring-framework configuration

The purpose of this project is to create a library for dynamic configuration of a java spring project. 
It use the same idea behind @Value annotation to inject and update configuration data in spring Component every time the configuration file change

### Exemple

#### Spring Configuration
```java
@Configuration
public class Myconfiguration {

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
```

#### Usage
```java
@Service
public class MyService {


  public String               completionSubject;

  public String[]             directoryNames;

  public String               mailTo;

  public Map<String, String>  teamName;

  @DynamicConfiguration("mrl.team")
  public void setTeam(String[] teams) {
    directoryNames = teams;
  }

  @DynamicConfiguration("mrl.completion.subject")
  public void setCompletionSubject(String value) {
    completionSubject = value;
  }

  @DynamicConfiguration("mrl.teamName")
  public void setTeamName(Map<String, String> value) {
    teamName = value;
  }
}
```
