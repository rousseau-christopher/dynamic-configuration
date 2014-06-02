package org.arkena.springconfig.watcher;

public interface ConfigComponent {
  String[] listNodes();

  void setNode(ConfigNode node);
}
