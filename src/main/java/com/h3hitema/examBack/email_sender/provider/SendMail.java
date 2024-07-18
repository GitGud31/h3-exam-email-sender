package com.h3hitema.examBack.email_sender.provider;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class SendMail {
    private final JavaMailSender javaMailSender;

    @Autowired
    public SendMail(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendHtmlMail(MailModel eParams) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(eParams.getTo().get(0));
        helper.setReplyTo(eParams.getFrom());
        helper.setFrom(eParams.getFrom());
        helper.setSubject(eParams.getSubject());
        helper.setText(eParams.getMessage(), eParams.isHtml());
        if (!eParams.getCc().isEmpty()) {
            helper.setCc(eParams.getCc().toArray(new String[eParams.getCc().size()]));
        }
        javaMailSender.send(message);
    }
}
