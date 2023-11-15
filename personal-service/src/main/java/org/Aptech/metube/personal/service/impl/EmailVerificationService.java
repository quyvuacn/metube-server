package org.aptech.metube.personal.service.impl;

import org.aptech.metube.personal.entity.User;
import org.aptech.metube.personal.repository.EmailVerificationTokenRepository;
import org.aptech.metube.personal.entity.EmailVerificationToken;
import org.aptech.metube.personal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@EnableScheduling
public class EmailVerificationService {
    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserRepository userRepository;

    public void sendVerificationEmail(User user) {
        // Tạo token xác nhận email
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken(token, user);
        emailVerificationTokenRepository.save(verificationToken);

        // Gửi email xác nhận
        String subject = "Verify Your Email Address";
        String body = "Please click the following link to verify your email address: "
                + "http://localhost:8085/api/v1/auth/verify-email?token=" + token;
        sendEmail(user.getEmail(), subject, body);
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredTokens() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<EmailVerificationToken> expiredTokens = emailVerificationTokenRepository.findAllByExpiryDateBefore(currentDateTime);

        try {
            for (EmailVerificationToken token : expiredTokens) {
                User user = token.getUser();
                emailVerificationTokenRepository.delete(token);
                userRepository.delete(user);
            }
        } catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }

    }

}
