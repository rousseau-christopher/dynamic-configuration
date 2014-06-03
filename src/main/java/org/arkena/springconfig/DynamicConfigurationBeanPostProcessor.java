package org.arkena.springconfig;

import org.arkena.springconfig.watcher.ConfigNode;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

public interface DynamicConfigurationBeanPostProcessor extends BeanPostProcessor, Ordered {

  String[] listKeys();

  void setNode(ConfigNode node);

}