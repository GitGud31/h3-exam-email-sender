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

    public static String encrypt(String email) {
        try {
            String password = "secret_key";
            SecureRandom sr = new SecureRandom();
            byte[] salt = new byte[8];
            sr.nextBytes(salt);
            final byte[][] keyAndIV = generateKeyAndIV(32, 16, 1, salt, password.getBytes(StandardCharsets.UTF_8),
                    MessageDigest.getInstance("MD5"));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyAndIV[0], "AES"), new IvParameterSpec(keyAndIV[1]));
            byte[] encryptedData = cipher.doFinal(email.getBytes(StandardCharsets.UTF_8));
            byte[] prefixAndSaltAndEncryptedData = new byte[16 + encryptedData.length];
            // Copy prefix (0-th to 7-th bytes)
            System.arraycopy("Salted__".getBytes(StandardCharsets.UTF_8), 0, prefixAndSaltAndEncryptedData, 0, 8);
            // Copy salt (8-th to 15-th bytes)
            System.arraycopy(salt, 0, prefixAndSaltAndEncryptedData, 8, 8);
            // Copy encrypted data (16-th byte and onwards)
            System.arraycopy(encryptedData, 0, prefixAndSaltAndEncryptedData, 16, encryptedData.length);
            String s = Base64.getEncoder().encodeToString(prefixAndSaltAndEncryptedData);
            String urlEncodeddata = URLEncoder.encode(s, "UTF-8");

            return urlEncodeddata;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[][] generateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password, MessageDigest md) {

        int digestLength = md.getDigestLength();
        int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
        byte[] generatedData = new byte[requiredLength];
        int generatedLength = 0;

        try {
            md.reset();

            // Repeat process until sufficient data has been generated
            while (generatedLength < keyLength + ivLength) {

                // Digest data (last digest if available, password data, salt if available)
                if (generatedLength > 0)
                    md.update(generatedData, generatedLength - digestLength, digestLength);
                md.update(password);
                if (salt != null)
                    md.update(salt, 0, 8);
                md.digest(generatedData, generatedLength, digestLength);

                // additional rounds
                for (int i = 1; i < iterations; i++) {
                    md.update(generatedData, generatedLength, digestLength);
                    md.digest(generatedData, generatedLength, digestLength);
                }

                generatedLength += digestLength;
            }

            // Copy key and IV into separate byte arrays
            byte[][] result = new byte[2][];
            result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
            if (ivLength > 0)
                result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);

            return result;

        } catch (DigestException e) {
            throw new RuntimeException(e);

        } finally {
            // Clean out temporary data
            Arrays.fill(generatedData, (byte) 0);
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