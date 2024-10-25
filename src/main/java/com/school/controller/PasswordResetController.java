package com.school.controller;

import com.school.repository.PasswordResetRepository;
import com.school.services.PassworsResetService;
import com.school.services.TeacherServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PasswordResetController {
    @Autowired
  private PassworsResetService passwordResetService;
    @Autowired
    private TeacherServices teacherServices;
    @GetMapping("/forgot-password-form")
    public String showForgotPasswordForm() {
        return "forgotpassword";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, Model model) {
        if (teacherServices.existTeacher(email)) {
            passwordResetService.sendPasswordResetEmail(email);
            model.addAttribute("message", "Password reset instructions have been sent to your email.");
        } else {
            model.addAttribute("message", "Sorry, we couldn't find an account with this email address.");
        }

        return "forgotpassword";
    }
    @GetMapping("/password-reset")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {

            model.addAttribute("token", token);
            return "reset-password";

    }
    @PostMapping("/password-reset")
    public String handlePasswordReset(@RequestParam("token") String token,
                                      @RequestParam("newPassword") String newPassword,
                                      @RequestParam("confirmPassword") String confirmPassword,
                                      Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("token", token);
            return "reset-password";
        }


        if (passwordResetService.validateToken(token)) {
            passwordResetService.updatePassword(token, newPassword);
            return "redirect:/teacherlogin";
        } else {
            model.addAttribute("error", "Password Reset Link Expired");
            return "reset-password";
        }
    }


}
