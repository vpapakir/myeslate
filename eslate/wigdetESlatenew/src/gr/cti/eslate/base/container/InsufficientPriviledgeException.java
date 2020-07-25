package gr.cti.eslate.base.container;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 1999
 * Company:
 * @author George Tsironis
 */

public class InsufficientPriviledgeException extends RuntimeException {

  public InsufficientPriviledgeException(String msg) {
      super(msg);
  }
}
