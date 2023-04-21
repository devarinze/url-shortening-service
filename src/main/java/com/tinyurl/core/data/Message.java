package com.tinyurl.core.data;

import javax.persistence.Transient;

public class Message {
    protected String msg;
    @Transient
    protected MessageType msgType = MessageType.WARNING;


    public Message() {
    }

    public Message(String msg) {
        this.msg = msg;
    }

    public Message(String msg, MessageType type) {
        this(msg);
        this.msgType = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
    }
}
