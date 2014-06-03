package org.arkena.springconfig.watcher;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.arkena.springconfig.DynamicConfigurationBeanPostProcessor;
import org.arkena.springconfig.exception.KeyNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO add directory/file watcher to update data then file change
 * 
 * @author Christopher Rousseau
 * 
 */
public class JsonWatcher {
  private static final Logger logger = LoggerFactory.getLogger(JsonWatcher.class);

  @Autowired
  ApplicationContext          ctx;

  @Autowired
  DynamicConfigurationBeanPostProcessor dynamicConfiguration;

  @PostConstruct
  public void loadFile() throws JsonProcessingException, IOException, KeyNotFoundException {
    logger.debug("Loading configuration file [{}]", configurationFile);
    ObjectMapper mapper = new ObjectMapper();
    Resource config = ctx.getResource(configurationFile);

    JsonNode root = mapper.readTree(config.getFile());

    for (String key : dynamicConfiguration.listKeys()) {
      JsonNode currentNode = root;
      // logger.debug("looking for [{}]", key);
      for (String token : key.split("\\.")) {
        if (currentNode.has(token)) {
          currentNode = currentNode.get(token);
        }
        else {
          throw new KeyNotFoundException("Cannot find key [" + key + "], [" + token + "] not found");
        }
      }
      dynamicConfiguration.setNode(new JsonConfigNode(key, currentNode));
    }
  }

  private String configurationFile;

  public void setconfigurationFile(String fileName) {
    configurationFile = fileName;
  }
}
