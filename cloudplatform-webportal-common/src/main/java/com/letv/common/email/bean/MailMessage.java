package com.letv.common.email.bean;

import java.io.File;
import java.util.Map;

public class MailMessage extends Message {
    private static final long serialVersionUID = -6736130484983127583L;
    private String replyTo;
    private String cc;
    private String bcc;
    private boolean isHtml = false;
    private Map<String, File> inlines;
    private Map<String, File> attachments;

    public MailMessage(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    public MailMessage(String from, String to, String subject, String text, Map<String, Object> model) {
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.model = model;
    }

    /**
     * @return the replyTo
     */
    public String getReplyTo() {
        return replyTo;
    }

    /**
     * @param replyTo the replyTo to set
     */
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    /**
     * @return the cc
     */
    public String getCc() {
        return cc;
    }

    /**
     * @param cc the cc to set
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

    /**
     * @return the bcc
     */
    public String getBcc() {
        return bcc;
    }

    /**
     * @param bcc the bcc to set
     */
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    /**
     * @return the isHtml
     */
    public boolean isHtml() {
        return isHtml;
    }

    /**
     * @param isHtml the isHtml to set
     */
    public void setHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }

    /**
     * @return the inlines
     */
    public Map<String, File> getInlines() {
        return inlines;
    }

    /**
     * @param inlines the inlines to set
     */
    public void setInlines(Map<String, File> inlines) {
        this.inlines = inlines;
    }

    /**
     * @return the attachments
     */
    public Map<String, File> getAttachments() {
        return attachments;
    }

    /**
     * @param attachments the attachments to set
     */
    public void setAttachments(Map<String, File> attachments) {
        this.attachments = attachments;
    }

}
