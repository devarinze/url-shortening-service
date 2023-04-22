package com.tinyurl.core.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Response {
    Object payload;
    List<Message> messages = new ArrayList();

    private Response(Object t) {
        payload = t;
    }

    private Response() {
        payload = null;
    }

    public Response with(Object payload) {
        this.payload = payload;
        return this;
    }

    /**
     * Add a warning message to the data's message list
     *
     * @param msg Full message
     */
    public Response warn(String msg) {
        Message message = new Message(msg, MessageType.WARNING);
        messages.add(message);
        return this;
    }

    /**
     * Add an error message to the data's message list
     *
     * @param msg message
     */
    public Response error(String msg) {
        Message message = new Message(msg, MessageType.ERROR);
        messages.add(message);
        return this;
    }

    /**
     * Add an info message to the data's message list
     *
     * @param msg message
     */
    public Response info(String msg) {
        Message message = new Message(msg, MessageType.INFO);
        messages.add(message);
        return this;
    }

    /**
     * Add a success message to the data's message list
     *
     * @param msg message
     */
    public Response success(String msg) {
        Message message = new Message(msg, MessageType.SUCCESS);
        messages.add(message);
        return this;
    }

    public static Response of(Object t) {
        return new Response(t);
    }

    public static Response of() {
        return new Response();
    }
}
