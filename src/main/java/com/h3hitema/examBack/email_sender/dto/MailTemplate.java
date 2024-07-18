package com.h3hitema.examBack.email_sender.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailTemplate {

    static final Logger log = LoggerFactory.getLogger(MailTemplate.class);
    private String templateId;

    private String template;

    private Map<String, String> replacementParams;

    public MailTemplate(String templateId) throws IOException {
        this.templateId = templateId;
        try {
            this.template = loadTemplate(templateId);
        } catch (Exception e) {
            this.template = "";
            throw new IOException("Could not read template with ID = " + templateId);
        }
    }
    private String loadTemplate(String templateId) throws IOException {
        URL url = Objects.requireNonNull(getClass().getClassLoader().getResource(templateId));
        String content = "";
        try (InputStream inputStream = url.openConnection().getInputStream()) {
            content = new String(Objects.requireNonNull(inputStream).readAllBytes());
        } catch (IOException e) {
            throw new IOException("Could not read template with ID = " + templateId);
        }
        return content;
    }

    public String getTemplate(Map<String, String> replacements) {
        String cTemplate = this.getTemplate();
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return cTemplate;
    }

}