package com.example.boot_mail.service.impl;

import com.example.boot_mail.Utils.MultipartFileToFileUtils;
import com.example.boot_mail.controller.Result;
import com.example.boot_mail.domain.ToEmail;
import com.example.boot_mail.service.emailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class emailServiceImpl implements emailService {
    @Autowired
    JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    String from;

    @Override
    public Result commonEmail(ToEmail toEmail) {
        //创建简单邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        //谁发的
        message.setFrom(from);
        //谁要接收
        message.setTo(toEmail.getTos());
        //邮件标题
        message.setSubject(toEmail.getSubject());
        //邮件内容
        message.setText(toEmail.getContent());
        try {
            mailSender.send(message);
            return new Result("true","发送普通邮件成功");
        } catch (MailException e) {
            e.printStackTrace();
            return new Result("false","普通邮件方失败");
        }

    }

    @Override
    public Result htmlEmail(ToEmail toEmail) throws MessagingException {
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
        //谁发
        minehelper.setFrom(from);
        //谁要接收
        minehelper.setTo(toEmail.getTos());
        //邮件主题
        minehelper.setSubject(toEmail.getSubject());
        //邮件内容   true 表示带有附件或html
        minehelper.setText(toEmail.getContent(), true);
        try {
            mailSender.send(message);
            return  new Result("true","HTML邮件成功");
        } catch (MailException e) {
            e.printStackTrace();
            return new Result("false","HTML邮件成功");
        }
    }

    @Override
    public Result staticEmail(ToEmail toEmail, MultipartFile multipartFile, String resId) {
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //谁发
            helper.setFrom(from);
            //谁接收
            helper.setTo(toEmail.getTos());
            //邮件主题
            helper.setSubject(toEmail.getSubject());
            //邮件内容   true 表示带有附件或html
            //邮件内容拼接
            String content =
                    "<html><body><img width='250px' src=\'cid:" + resId + "\'>" + toEmail.getContent()
                            + "</body></html>";
            helper.setText(content, true);
            //蒋 multpartfile 转为file
            File multipartFileToFile = MultipartFileToFileUtils.MultipartFileToFile(multipartFile);
            FileSystemResource res = new FileSystemResource(multipartFileToFile);

            //添加内联资源，一个id对应一个资源，最终通过id来找到该资源
            helper.addInline(resId, res);
            mailSender.send(message);
            return new Result("true","嵌入静态资源的邮件已经发送");
        } catch (MessagingException e) {
            return new Result("false","嵌入静态资源的邮件发送失败");
        }
    }

    @Override
    public Result enclosureEmail(ToEmail toEmail, MultipartFile multipartFile) {
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //谁发
            helper.setFrom(from);
            //谁接收
            helper.setTo(toEmail.getTos());
            //邮件主题
            helper.setSubject(toEmail.getSubject());
            //邮件内容   true 表示带有附件或html
            helper.setText(toEmail.getContent(), true);
            File multipartFileToFile = MultipartFileToFileUtils.MultipartFileToFile(multipartFile);
            FileSystemResource file = new FileSystemResource(multipartFileToFile);
            String filename = file.getFilename();
            //添加附件
            helper.addAttachment(filename, file);
            mailSender.send(message);
            return new Result("true","附件邮件成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            return new Result("false","附件邮件发送失败");
        }
    }
    @Override
    public Result sendMultipleCommonEmails(List<ToEmail> toEmails) {
        ExecutorService executorService = Executors.newFixedThreadPool(5); // 创建一个具有5个线程的线程池

        for (ToEmail toEmail : toEmails) {
            executorService.execute(() -> {
                Result result = commonEmail(toEmail);
                System.out.println(result.getMessage());
            });
        }

        executorService.shutdown(); // 关闭线程池

        return new Result("true", "多线程发送邮件任务已启动");
    }
    @Override
    public Result sendMultipleEmails(List<ToEmail> toEmails) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        for (int i = 0; i < toEmails.size(); i++) {
            final ToEmail toEmail = toEmails.get(i);
            scheduler.schedule(() -> {
                Result result = commonEmail(toEmail);
                System.out.println(result.getMessage());
            }, i, TimeUnit.MINUTES);
        }

        return new Result("true", "定时发送邮件任务已启动");
    }



}
