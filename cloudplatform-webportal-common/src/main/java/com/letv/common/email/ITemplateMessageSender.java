package com.letv.common.email;

public interface ITemplateMessageSender {
    public void sendMessage(Object msg);

    public void sendMessage(Object[] msgArray);
}
