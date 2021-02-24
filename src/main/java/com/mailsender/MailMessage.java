package com.mailsender;

import com.mailsender.settings.MailToTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MailMessage {

    private MailSession session;
    private List<MailToTask> tasks;
    private static final Logger logger = LogManager.getLogger(MailMessage.class.getName());

    public MailMessage(ConfigInit configInit) {
        session = configInit.readConfigFileAndGetSession();
        tasks = configInit.readConfigFileAndGetTask();
    }

    public void doAllTask() {
        logger.info("Count tasks: {}", tasks.size());
        for (MailToTask task : tasks
        ) {
            logger.info("task : {}", task.toString());
            if (task.getTaskEnabled()) {
                sendEmail(task.getFromEmail(), task.getSendListToEmail(), task.getSubject(), task.getText(), task.getAttachedFilesInDirectory());
            }
        }
    }

    private void sendEmail(String sendFromEmail, List<String> sendToEmail, String subject, String text, List<String> files) {

        Message message = new MimeMessage(session.getSession());
        Multipart multipart = new MimeMultipart();

        setSendFromEmail(sendFromEmail, message);

        setRecipientsOfEmail(sendFromEmail, sendToEmail, message);

        setSubjectOfEmail(subject, message);

        attachTextToMultipart(text, multipart);

        attachFilesToMultiPart(files, multipart);

        try {
            logger.info("Setting content email. Add all body's to Email Message");
            message.setContent(multipart);
        } catch (MessagingException e) {
            logger.warn("Cant set content of email", e);
        }

        try {
            logger.info("SendingMessage");
            Transport.send(message);
        } catch (MessagingException e) {
            logger.error("Cant send Email", e);
        }

    }

    private void setSendFromEmail(String sendFromEmail, Message message) {
        try {
            logger.info("Settings from Email :" + sendFromEmail);
            message.setFrom(new InternetAddress(sendFromEmail));
        } catch (MessagingException e) {
            logger.error("Cant set parameter: fromEmail", e);
        }
    }

    private void setRecipientsOfEmail(String sendFromEmail, List<String> sendToEmail, Message message) {
        logger.info("Recipients of Email: {}" + sendFromEmail);
        try {
            for (String s : sendToEmail
            ) {
                logger.info("Add addRecipient: {}", s);
                message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(s));
            }
        } catch (MessagingException e) {
            logger.warn("Cant add recipient of email", e);
        }
    }

    private void setSubjectOfEmail(String subject, Message message) {
        try {
            if (subject != null) {
                logger.info("Settings Subject of Mail");
                message.setSubject(subject);
            } else {
                message.setSubject("Subject");
            }
        } catch (MessagingException e) {
            logger.warn("Cant set subject of email", e);
        }
    }

    private void attachTextToMultipart(String text, Multipart multipart) {
        try {
            logger.info("set text message body. text: {}", text);
            multipart.addBodyPart(getMessagePart(text));
        } catch (MessagingException e) {
            logger.warn("Cant set text of email", e);
        }
    }

    private void attachFilesToMultiPart(List<String> files, Multipart multipart) {
        logger.info("Prepare to attach files {}", files);
        Set<String> attachedList = attachFilesList(files);
        logger.info("attacheListFile: {}", attachedList);

        try {
            for (String file : attachedList) {
                logger.info("attaching file {} to body email", file);
                multipart.addBodyPart(getAttachFilePart(file));
            }
        } catch (MessagingException e) {
            logger.warn("Cant attach files", e);
        }
    }

    private Set<String> attachFilesList(List<String> files) {
        logger.info("FileList: {}", files);
        Set<String> listFiles = new HashSet<>();
        for (String file : files
        ) {
            logger.info("File {}: isExist {},  is dir - {}", file, Files.exists(Path.of(file)), Files.isDirectory(Path.of(file)));
            if (Files.exists(Path.of(file))) {
                if (!Files.isDirectory(Path.of(file))) {
                    logger.info("Add file: {} to listFiles");
                    listFiles.add(file);
                } else if (Files.isDirectory(Path.of(file))) {
                    logger.info("Get Collection of file");
                    listFiles.addAll(Objects.requireNonNull(listFilesUsingFileWalk(file)));
                }
            } else {
                logger.info("File {} not found", file);
            }
        }
        return listFiles;
    }


    private Set<String> listFilesUsingFileWalk(String dir) {
        logger.info("Get file list from dir: {}", dir);
        try (Stream<Path> stream = Files.walk(Paths.get(dir))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        } catch (IOException exception) {
            logger.warn("Cant get collection from dir: {}; ", exception);
            return null;
        }
    }


    private BodyPart getMessagePart(String text) throws MessagingException {
        BodyPart part = new MimeBodyPart();
        if (text != null) {
            part.setText(text);
        } else {
            part.setFileName("Text");
        }
        return part;
    }

    private BodyPart getAttachFilePart(String file) {
        BodyPart part = new MimeBodyPart();
        try {
            logger.info("Attaching file to body: {}", file);
            part.setDataHandler(new DataHandler(new FileDataSource(file)));
        } catch (MessagingException e) {
            logger.error("Cant attach file", e);
        }
        try {
            logger.info("Setting filename: {}", file);
            part.setFileName(Path.of(file).getFileName().toString());
        } catch (MessagingException e) {
            logger.error(String.format("Cant set name in message body. File: %s", file), e);
        }
        return part;
    }


}
