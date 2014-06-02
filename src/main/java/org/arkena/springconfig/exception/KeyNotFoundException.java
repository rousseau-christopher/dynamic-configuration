package org.arkena.springconfig.exception;

public class KeyNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 6543261797194032457L;

  public KeyNotFoundException(String message) {
    super(message);
  }

}
