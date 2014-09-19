package com.letv.common.email.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Message implements Serializable {
    private static final long serialVersionUID = 7584681699486258367L;
    public String from;
    public String to;
    public String subject;
    public String text;
    public String encoding;
    public Date createDate;
    public Map<String, Object> model;

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the model
     */
    public Map<String, Object> getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

}
