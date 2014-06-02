package com.arkena.springconfig.service;

import java.util.Map;

import org.arkena.springconfig.annotation.DynamicConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MrlService {
  private static final Logger logger = LoggerFactory.getLogger(MrlService.class);

  public String               completionSubject;
  public String               creationSubject;

  public String[]             directoryNames;

  public String               mailTo;

  public Map<String, String>  teamName;


  @DynamicConfiguration("mrl.team")
  public void setTeam(String[] teams) {
    logger.debug(teams.toString());
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
