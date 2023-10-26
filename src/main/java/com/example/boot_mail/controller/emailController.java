package com.example.boot_mail.controller;

import com.example.boot_mail.domain.ToEmail;
import com.example.boot_mail.service.emailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/emails")
@ResponseBody
public class emailController {
    @Autowired
    emailService email;
    @PostMapping("/common")
    public Result common(ToEmail toEmail){
        return email.commonEmail(toEmail);
    }
    @PostMapping("/html")
    public Result html(ToEmail toEmail) throws MessagingException {
        return email.htmlEmail(toEmail);
    }
    @PostMapping("/static")
    public Result staticEmail(ToEmail toEmail,MultipartFile multipartFile,String resId){
        return email.staticEmail(toEmail,multipartFile,resId);
    }
    @PostMapping("/enclosure")
    public Result enclosure(MultipartFile multipartFile, ToEmail toEmail){
        return email.enclosureEmail(toEmail,multipartFile);
    }
    @PostMapping("/multi")
    public Result MultiMails(@RequestBody List<ToEmail> toEmailList){
        return email.sendMultipleCommonEmails(toEmailList);
    }
    @PostMapping("/scheduled")
    public Result scheduled(@RequestBody List<ToEmail> toEmailList){
        return email.sendMultipleEmails(toEmailList);
    }
}
