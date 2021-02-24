package com.mailsender.settings;

import java.util.List;

public class MailToTask {


    private String fromEmail;

    private List<String> sendListToEmail;
    private List<String> attachedFilesInDirectory;
    private String subject;
    private String text;
    private Boolean taskEnabled;


    public MailToTask(String fromEmail, List<String> sendListToEmail, List<String> attachedFilesInDirectory, String subject, String text, Boolean taskEnabled) {
        this.fromEmail = fromEmail;
        this.sendListToEmail = sendListToEmail;
        this.attachedFilesInDirectory = attachedFilesInDirectory;
        this.subject = subject;
        this.text = text;
        this.taskEnabled = taskEnabled;
    }

    public MailToTask() {
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public List<String> getSendListToEmail() {
        return sendListToEmail;
    }

    public void setSendListToEmail(List<String> sendListToEmail) {
        this.sendListToEmail = sendListToEmail;
    }

    public List<String> getAttachedFilesInDirectory() {
        return attachedFilesInDirectory;
    }

    public void setAttachedFilesInDirectory(List<String> attachedFilesInDirectory) {
        this.attachedFilesInDirectory = attachedFilesInDirectory;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getTaskEnabled() {
        return taskEnabled;
    }

    public void setTaskEnabled(Boolean taskEnabled) {
        this.taskEnabled = taskEnabled;
    }

    @Override
    public String toString() {
        return "MailToTask{" +
                "fromEmail='" + fromEmail + '\'' +
                ", sendListToEmail=" + sendListToEmail +
                ", attachedFilesInDirectory=" + attachedFilesInDirectory +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", taskEnabled=" + taskEnabled +
                '}';
    }
}
