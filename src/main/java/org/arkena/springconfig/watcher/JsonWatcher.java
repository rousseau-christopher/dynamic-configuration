package org.arkena.springconfig.watcher;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JsonWatcher {

  @Autowired
  ApplicationContext ctx;

  public void loadFile() throws JsonProcessingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    Resource config = ctx.getResource("classpath:configuration.json");

    JsonNode root = mapper.readTree(config.getFile());
    root.get("mrl");
  }
}
