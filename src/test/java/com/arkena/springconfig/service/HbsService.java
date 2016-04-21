package com.arkena.springconfig.service;

import net.lamad.spring.sdc.annotation.DynamicConfiguration;
import org.springframework.stereotype.Service;

@Service
public class HbsService {

  public String[] teams;

  @DynamicConfiguration("mrl.team")
  public void setTeam(String[] teams) {
    this.teams = teams;
  }
}
