package com.company;

/**
 * Created by Troy on 10/11/16.
 */
public class Message {
    int id;
    int replyId;
    String author;
    String text;

    public Message(int id, int replyId, String author, String text) {
        this.id = id;
        this.replyId = replyId;
        this.author = author;
        this.text = text;
    }
}
