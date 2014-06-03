package org.arkena.springconfig.watcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.arkena.springconfig.service.HbsService;
import com.arkena.springconfig.service.MrlService;
import com.fasterxml.jackson.core.JsonProcessingException;

import config.UnitTestConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { UnitTestConfiguration.class })
public class JsonWatcherTest {

  @Autowired
  private MrlService service;

  @Autowired
  private HbsService hbsService;

  @Test
  public void test() throws JsonProcessingException, IOException {
    assertEquals("Completion P2/XDCAM {filename}", service.completionSubject);
    String[] expectedArray = { "ITA", "ALJZ" };
    assertArrayEquals(expectedArray, service.directoryNames);
    Map<String, String> expectedMap = new HashMap<String, String>();
    expectedMap.put("ITA", "Italie");
    expectedMap.put("ALJZ", "Al Jazeera");
    
    assertEquals(expectedMap, service.teamName);

    assertArrayEquals(expectedArray, hbsService.teams);

    assertEquals(465, service.idUser);
    assertEquals(Integer.valueOf(465), service.idUserInteger);
  }

}
