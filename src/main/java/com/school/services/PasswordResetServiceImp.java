package com.school.services;

import com.school.entities.PasswordResetToken;
import com.school.entities.Teacher;
import com.school.repository.PasswordResetRepository;
import com.school.repository.TeacherRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetServiceImp implements PassworsResetService{


    @Autowired
   private JavaMailSender mailSender;
    @Autowired
    private PasswordResetRepository passwordResetRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Override
    public void sendPasswordResetEmail(String email) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetToken.setEmail(email);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        passwordResetRepository.save(passwordResetToken);
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("rajunerenika@gmail.com");
//        message.setTo(email);
//        message.setSubject("Password Reset");
//        message.setText("Please click here to reset password " +"http://localhost:8080/password-reset");
//        mailSender.send(message);
        String resetUrl = "http://localhost:8080/password-reset?token=" + passwordResetToken.getToken();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("rajunerenika@example.com");
            helper.setTo(email);
            helper.setSubject("Password Reset Request");

            String content = "<html>" +
                    "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0;\">" +
                    "<div style=\"background-color: #f8f9fa; padding: 20px;\">" +
                    "<h2 style=\"color: #343a40;\">Password Reset Request</h2>" +
                    "<p style=\"color: #6c757d;\">Dear user,</p>" +
                    "<p style=\"color: #6c757d;\">You requested a password reset. Please click the button below to reset your password:</p>" +
                    "<a href=\"" + resetUrl + "\" style=\"display: inline-block; padding: 10px 20px; color: #fff; background-color: #007bff; border-radius: 5px; text-decoration: none;\">Reset Password</a>" +
                    "<p style=\"color: #6c757d;\">If you did not request this, please ignore this email.</p>" +
                    "<p style=\"color: #6c757d;\">Thank you,<br>Your Company</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(content, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            // Handle exception
            e.printStackTrace();
        }
    }



    @Override
    public boolean validateToken(String token) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetRepository.findByToken(token);
        return passwordResetToken.isPresent() && passwordResetToken.get().getExpiryDate().isAfter(LocalDateTime.now());
    }

    @Override
    public void updatePassword(String token, String newPassword)
    {
        Optional<PasswordResetToken> passwordResetToken = passwordResetRepository.findByToken(token);
        Teacher teacher=teacherRepository.findByEmail(passwordResetToken.get().getEmail());
        teacher.setPassword(newPassword);
        teacherRepository.save(teacher);

    }
}
