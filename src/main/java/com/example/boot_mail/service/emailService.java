package com.example.boot_mail.service;


import com.example.boot_mail.controller.Result;
import com.example.boot_mail.domain.ToEmail;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface emailService {
    Result commonEmail(ToEmail email);
    Result htmlEmail(ToEmail email) throws MessagingException;
    Result staticEmail(ToEmail email, MultipartFile multipartFile,String resId);
    Result enclosureEmail(ToEmail email,MultipartFile multipartFile);
    Result sendMultipleCommonEmails(List<ToEmail> toEmails);
    Result sendMultipleEmails(List<ToEmail> toEmails);
}
