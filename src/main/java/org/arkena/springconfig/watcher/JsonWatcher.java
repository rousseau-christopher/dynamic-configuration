package org.arkena.springconfig.watcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.arkena.springconfig.exception.KeyNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonWatcher {
  private static final Logger logger = LoggerFactory.getLogger(JsonWatcher.class);

  @Autowired
  ApplicationContext ctx;

  @PostConstruct
  public void loadFile() throws JsonProcessingException, IOException, KeyNotFoundException {
    logger.debug("Loading configuration file [{}]", configurationFile);
    ObjectMapper mapper = new ObjectMapper();
    Resource config = ctx.getResource(configurationFile);

    JsonNode root = mapper.readTree(config.getFile());

    for (String key : mapKeyComponents.keySet()) {
      JsonNode currentNode = root;
      ConfigComponent component = mapKeyComponents.get(key);
      // logger.debug("looking for [{}]", key);
      for (String token : key.split("\\.")) {
        if (currentNode.has(token)) {
          currentNode = currentNode.get(token);
        }
        else {
          throw new KeyNotFoundException("Cannot found key [" + key + "], [" + token + "] not found, in component :[" + component.getClass().getName() + "]");
        }
      }
      component.setNode(new JsonConfigNode(key, currentNode));
    }
  }

  private String configurationFile;

  public void setconfigurationFile(String fileName) {
    configurationFile = fileName;
  }

  private Map<String, ConfigComponent> mapKeyComponents = new HashMap<String, ConfigComponent>();

  @Autowired
  public void registerComponents(List<ConfigComponent> list) {
    for (ConfigComponent component : list) {
      for (String key : component.listNodes()) {
        logger.debug("Register configuration key [{}]", key);
        mapKeyComponents.put(key, component);
      }
    }
  }
}
