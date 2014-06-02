package com.arkena.springconfig.service;

import java.util.Map;

import org.arkena.springconfig.watcher.ConfigComponent;
import org.arkena.springconfig.watcher.ConfigNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MrlService implements ConfigComponent {
  private static final Logger logger = LoggerFactory.getLogger(MrlService.class);

  private String completionSubject;
  private String creationSubject;

  private String[]            directoryNames;

  private String   mailTo;

  private Map<String, String> teamName;

  public void setConfigurationValue(String node, Object value) {

  }

  private final static String   nodeTeam       = "mrl.team";
  private final static String   nodeTeamNames  = "mrl.teamName";
  private final static String   completionNode = "mrl.completion.subject";
  private final static String   creationNode   = "mrl.creation.subject";
  private final static String   toNode         = "mrl.to";
  private final static String[] listNodes      = { nodeTeam, nodeTeamNames, "mrl.type", completionNode, creationNode, toNode };
  @Override
  public String[] listNodes() {
    return listNodes;
  }

  @Override
  public void setNode(ConfigNode node) {
    if (nodeTeam.equals(node.getKey())) {
      directoryNames = node.asArrayString();
    }
    else if (completionNode.equals(node.getKey())) {
      completionSubject = node.asString();
    }
    else if (creationNode.equals(node.getKey())) {
      creationSubject = node.asString();
    }
    else if (toNode.equals(node.getKey())) {
      mailTo = node.asString();
    }
    if (nodeTeamNames.equals(node.getKey())) {
      teamName = node.asMapStrings();
    }
    else {
      logger.error("Unknow key [{}]", node.getKey());

    }
  }
}
