package net.lamad.spring.sdc;

import net.lamad.spring.sdc.watcher.ConfigNode;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

public interface DynamicConfigurationBeanPostProcessor extends BeanPostProcessor, Ordered {

  String[] listKeys();

  void setNode(ConfigNode node);

}