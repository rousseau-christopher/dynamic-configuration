package net.lamad.spring.sdc.exception;

public class NotAnArrayException extends RuntimeException {
  private static final long serialVersionUID = 2798054639403973742L;

  public NotAnArrayException(String message) {
    super(message);
  }

}
