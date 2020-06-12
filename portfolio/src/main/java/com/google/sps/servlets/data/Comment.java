
package com.google.sps.data;

/** An item on a todo list. */
public final class Comment {

  private final String userName;
  public final String text;
  private final String  readableDate;

  public Comment(String text, String userName, String readableDate) {
    this.text = text;
    this.userName = userName;
    this. readableDate = readableDate;
  }
}