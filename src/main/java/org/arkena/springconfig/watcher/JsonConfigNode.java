package org.arkena.springconfig.watcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.arkena.springconfig.exception.NotAnArrayException;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonConfigNode implements ConfigNode {

  private String key;
  private JsonNode jsonNode;

  public JsonConfigNode(String key, JsonNode jsonNode) {
    this.key = key;
    this.jsonNode = jsonNode;
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public String asString() {
    return jsonNode.asText();
  }

  @Override
  public String[] asArrayString() {
    if (jsonNode.isArray()) {
      String[] result = new String[jsonNode.size()];
      int i = 0;
      for (JsonNode subNode : jsonNode) {
        result[i++] = subNode.asText();
      }
      return result;
    }
    else {
      throw new NotAnArrayException("config value [" + key + "] is not an array");
    }
  }

  // @Override
  // public String[] asArrayString() {
  // String[] result = new String[jsonNode.size()];
  // int i = 0;
  // Iterator<Entry<String, JsonNode>> entries = jsonNode.fields();
  // while (entries.hasNext()) {
  // Entry<String, JsonNode> entry = entries.next();
  // result[i++] = entry.getKey();
  // }
  // return result;
  // }

  @Override
  public Map<String, String> asMapStrings() {
    Map<String, String> result = new HashMap<String, String>();
    Iterator<Entry<String, JsonNode>> entries = jsonNode.fields();
    while (entries.hasNext()) {
      Entry<String, JsonNode> entry = entries.next();
      result.put(entry.getKey(), entry.getValue().asText());
    }
    return result;
  }

}
