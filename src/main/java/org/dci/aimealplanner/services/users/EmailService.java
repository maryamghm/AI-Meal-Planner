package org.dci.aimealplanner.services.users;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.url}")
    private String appUrl;

    public void sendVerificationEmail(String toEmail, String token) {
        String verificationUrl = appUrl + "/auth/verify?token=" + token;

        try {
            String htmlContent = loadHtmlTemplate("emails/verification-email.html");
            htmlContent = htmlContent.replace("{{VERIFICATION_URL}}",verificationUrl);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject("Please verify your email");
            mimeMessageHelper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String loadHtmlTemplate(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
