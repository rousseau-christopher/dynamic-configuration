package com.arkena.springconfig.service;

import org.springframework.stereotype.Service;

@Service
public class MrlService {
  private String completionSubject;
  private String creationSubject;

  private String[] directoryTypes;

  private String   mailTo;

  public void setConfigurationValue(String node, Object value) {

  }
}
