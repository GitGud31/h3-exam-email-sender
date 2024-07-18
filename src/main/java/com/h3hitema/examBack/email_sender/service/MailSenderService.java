package com.h3hitema.examBack.email_sender.service;


import com.h3hitema.examBack.email_sender.dto.MailDataDto;
import com.h3hitema.examBack.email_sender.dto.MailTemplate;
import com.h3hitema.examBack.email_sender.provider.MailModel;
import com.h3hitema.examBack.email_sender.provider.SendMail;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSenderService {
    private static String FORGET_PWD_MAIL_SUBJECT = "Mot de passe oublié";

    private final SendMail sendMail;

    public SendMailStatus sendMailForgetPwd(String from, MailDataDto data) {
        String templateName = "mail/template-email-forget-pwd.html";
        return effectiveSendMail(from, data, templateName,  FORGET_PWD_MAIL_SUBJECT);
    }

    private SendMailStatus effectiveSendMail(String from, MailDataDto data, String templateName, String mailSubject) {
        MailTemplate template = null;
        try {
            template = new MailTemplate(templateName);
        } catch (IOException e) {
            log.error(e.getMessage());
            return SendMailStatus.Failed;
        }
        Map<String, String> replacements = new HashMap<>();
        replacements.put("code", data.getModel().get("code").toString());
        formatDate(replacements, data);
        String message = template.getTemplate(replacements);

        var mailModelData = MailModel.builder().from(from).to(Collections.singletonList(data.getTo())).subject(mailSubject).message(message).isHtml(true).build();
        try {
            sendMail.sendHtmlMail(mailModelData);
            return SendMailStatus.OK;
        } catch (MessagingException e) {
            log.error(e.getMessage());
            return SendMailStatus.Failed;
        }
    }

    private void formatDate(Map<String, String> replacements, MailDataDto data) {
        final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        try {
            replacements.put("expirationDate", ((LocalDateTime) data.getModel().get("expirationDate")).format(CUSTOM_FORMATTER));
        } catch (Exception e) {
            replacements.put("expirationDate", " ''' error ''' ");
        }

    }
}
