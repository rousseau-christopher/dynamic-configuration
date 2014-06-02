package org.arkena.springconfig.watcher;

import java.util.Map;

import org.arkena.springconfig.exception.NotAnArrayException;

public interface ConfigNode {
  String getKey();

  /**
   * convert the node as String
   * 
   * @return
   */
  String asString();

  /**
   * convert the node as String[] if possible.
   * 
   * @throws NotAnArrayException
   *           if the node is not convertible
   * @return
   */
  String[] asArrayString();

  /**
   * Convert the node as Map<String,String> if possible.
   * 
   * @throws NotADictionaryException
   *           if the node is not convertible
   * @return
   */
  Map<String, String> asMapStrings();
}
