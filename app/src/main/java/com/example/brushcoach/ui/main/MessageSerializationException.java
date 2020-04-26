package com.example.brushcoach.ui.main;

public class MessageSerializationException extends Exception {

    public MessageSerializationException (String msg, Throwable err) {
        super(msg,err);
    }

    public MessageSerializationException (String msg) {
        super(msg);
    }
}
