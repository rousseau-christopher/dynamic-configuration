package net.lamad.spring.sdc.watcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.lamad.spring.sdc.DynamicConfigurationBeanPostProcessor;
import net.lamad.spring.sdc.exception.KeyNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json file configuration watcher.
 * 
 * This watcher will call the DynamicConfigurationBeanPostProcessor with updates node then the file change
 * 
 * @author Christopher Rousseau
 * 
 */
public class JsonWatcher implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(JsonWatcher.class);

  @Autowired
  ApplicationContext                    ctx;

  @Autowired
  DynamicConfigurationBeanPostProcessor dynamicConfiguration;

  private String                        configurationFile;

  public void setconfigurationFile(String fileName) {
    configurationFile = fileName;
  }

  @PostConstruct
  public void init() {
    try {
      loadFile();
      startWatcher();
    } catch (Exception e) {
      logger.error("Error initializing watcher", e);
      throw new RuntimeException(e);
    }
  }

  private Map<String, String> cachedConfigValues = new HashMap<>();

  private boolean haveChange(String key, JsonNode node) {
    if (cachedConfigValues.containsKey(key)) {
      if (cachedConfigValues.get(key).equals(node.toString())) {
        return false;
      }
    }
    return true;
  }

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
      if (haveChange(key, currentNode)) {
        dynamicConfiguration.setNode(new JsonConfigNode(key, currentNode));
        cachedConfigValues.put(key, currentNode.toString());
      }
    }
  }

  public void startWatcher() throws IOException {
    Thread t = new Thread(this);
    t.setName("JsonWatcher");
    t.start();
  }



  /**
   * Used to run a new thread for the direcotry watching of the config file !
   */
  @Override
  public void run() {
    try {
      WatchService watcher = FileSystems.getDefault().newWatchService();
      Resource config = ctx.getResource(configurationFile);
      logger.info("Start watching [{}]", config.getFile().getAbsoluteFile());
      String fileName = config.getFilename();
      Path dir = FileSystems.getDefault().getPath(config.getFile().getParent());
      dir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
      WatchKey key = null;
      do {
        try {
          key = watcher.take();
          List<WatchEvent<?>> events = key.pollEvents();
          for (WatchEvent<?> event : events) {
            if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
              Path propFile = (Path) event.context();
              if (propFile.endsWith(fileName)) {

                BasicFileAttributes bfa = Files.readAttributes(
                    dir.resolve(propFile),
                    BasicFileAttributes.class);
                logger.info("Config file [{}] changed, last modification date [{}]", config.getFile().getAbsoluteFile(), bfa.lastModifiedTime());
                loadFile();
              }
            }
          }
        } catch (InterruptedException | IOException e) {
          logger.error("Error in json Watcher", e);
        }

      } while (key.reset());
    } catch (IOException e) {
      logger.error("Exception while watching directory");
    }

  }
}
