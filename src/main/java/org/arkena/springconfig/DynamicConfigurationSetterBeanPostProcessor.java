package org.arkena.springconfig;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arkena.springconfig.annotation.DynamicConfiguration;
import org.arkena.springconfig.watcher.ConfigNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.Ordered;

/**
 * Implementation that support only setter method for injection of configuration value
 * 
 * @author Christopher Rousseau
 * 
 */
public class DynamicConfigurationSetterBeanPostProcessor implements DynamicConfigurationBeanPostProcessor {

  private static final Logger logger = LoggerFactory.getLogger(DynamicConfigurationSetterBeanPostProcessor.class);

  /**
   * TODO : rewrite this code to support any method (not only setter) with one arguments only and properties.
   */
  @Override
  public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
    logger.debug("-------------- Before init of [{}]", beanName);

    for (Method method : bean.getClass().getDeclaredMethods()) {
      if (method.isAnnotationPresent(DynamicConfiguration.class)) {
        logger.debug("Method [{}] of bean [{}] found", method.getName(), beanName);
        try {
          // Type[] types = method.getGenericParameterTypes();
          PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
          if (pd == null) {
            throw new IllegalArgumentException("@DynamicConfiguration need a setter to work, [" + method.getName() + "()] found instead !!");
          }
          DynamicConfiguration annotation = method.getAnnotation(DynamicConfiguration.class);

          logger.debug("Register setter [" + method.getName() + "()] with key [" + annotation.value() + "] for bean [" + beanName + "]");
          PropertyData data = new PropertyData(annotation.value(), beanName, bean, pd);
          registerSetter(data);
        } catch (Throwable e) {
          throw new BeanInitializationException("Cannot initialise bean [" + beanName + "]", e);
        }
      }
    }

    return bean;
  }
  
  /**
   * Multiple setter can be defined for a configuration key (usually on different bean)
   */
  Map<String, List<PropertyData>> mapSetter = new HashMap<String, List<PropertyData>>();


  private void registerSetter(PropertyData data) {
    List<PropertyData> list = mapSetter.get(data.key);
    if (list == null) {
      list = new ArrayList<PropertyData>();
      mapSetter.put(data.key, list);
    }
    list.add(data);
  }

  /* (non-Javadoc)
   * @see org.arkena.springconfig.DynamicConfigurationBeanPostProcessorI#listKeys()
   */
  @Override
  public String[] listKeys() {
    return mapSetter.keySet().toArray(new String[0]);
  }

  /* (non-Javadoc)
   * @see org.arkena.springconfig.DynamicConfigurationBeanPostProcessorI#setNode(org.arkena.springconfig.watcher.ConfigNode)
   */
  @Override
  public void setNode(ConfigNode node) {
    if (mapSetter.containsKey(node.getKey())) {
      logger.debug("Setting key [{}] with value [{}]", node.getKey(), node);
      for (PropertyData prop : mapSetter.get(node.getKey())) {
        logger.debug("  -> bean [{}] updated using method [{}]", prop.beanName, prop.pd.getWriteMethod().getName() + "()");
        try {
          if (prop.pd.getPropertyType() == String.class) {
            prop.pd.getWriteMethod().invoke(prop.bean, node.asString());
          }
          else if (prop.pd.getPropertyType() == String[].class) {
            prop.pd.getWriteMethod().invoke(prop.bean, (Object) node.asArrayString());
          }
          else if (prop.pd.getPropertyType() == Map.class) {
            prop.pd.getWriteMethod().invoke(prop.bean, node.asMapStrings());
          }
          else if (prop.pd.getPropertyType() == int.class || prop.pd.getPropertyType() == Integer.class) {
            prop.pd.getWriteMethod().invoke(prop.bean, Integer.parseInt(node.asString()));
          }
          else {
            logger.error("unknown Type [{}]", prop.pd.getPropertyType());
          }
        } catch (Throwable e) {
          logger.error(e.getMessage(), e);
        }
      }
    }
    else {
      logger.error("Cannot find key [{}]", node.getKey());
    }
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    // logger.debug("--------------- After init of [{}]", beanName);
    return bean;
  }

  @Override
  public int getOrder() {
    return Ordered.LOWEST_PRECEDENCE;
  }

  public static class PropertyData {
    private String             key;
    private String             beanName;
    private Object             bean;
    private PropertyDescriptor pd;

    public PropertyData(String key, String beanName, Object bean, PropertyDescriptor pd) {
      this.key = key;
      this.bean = bean;
      this.beanName = beanName;
      this.pd = pd;
    }

  }

}
