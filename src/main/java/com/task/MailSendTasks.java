package com.task;

import java.util.List;

public class MailSendTasks {

    private List<MailToTask> mailToTaskList;


    public MailSendTasks(List<MailToTask> mailToTaskList) {
        this.mailToTaskList = mailToTaskList;
    }

    public List<MailToTask> getMailToTaskList() {
        return mailToTaskList;
    }

    public void setMailToTaskList(List<MailToTask> mailToTaskList) {
        this.mailToTaskList = mailToTaskList;
    }

    @Override
    public String toString() {
        return "MailSendTasks{" +
                "mailToTaskList=" + mailToTaskList +
                '}';
    }
}
