package org.arkena.springconfig.watcher;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;

import config.UnitTestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { UnitTestConfiguration.class })
public class JsonWatcherTest {

  @Autowired
  private JsonWatcher watcher;

  @Test
  public void test() throws JsonProcessingException, IOException {
    // watcher.loadFile();
  }

}
