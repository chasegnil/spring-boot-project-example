package com.chasegnil.mail;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.*;

/**
 * 邮件工具类
 */
@SuppressWarnings("unused")
@Component
public class MailUtil {

    private final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     *
     * @param deliver 发送者邮箱
     * @param receiver 接收者邮箱
     * @param carbonCopy 抄送者邮件地址
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String deliver, String receiver, String carbonCopy, String subject, String content){
        Set<String> receivers = new HashSet<>();
        receivers.add(receiver);
        Set<String> carbonCopys = new HashSet<>();
        carbonCopys.add(carbonCopy);
        this.sendSimpleMail(deliver, receivers, carbonCopys, subject, content);
    }

    /**
     *
     * @param deliver 发送者邮箱
     * @param receivers 接收者们邮箱
     * @param carbonCopys 抄送者们邮件地址
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    @SuppressWarnings("Duplicates")
    public void sendSimpleMail(String deliver, Collection<String> receivers, Collection<String> carbonCopys, String subject, String content){
        checkEmpty(deliver, receivers, subject, content);
        Set<String> receiversSet = new HashSet<>(receivers);
        Set<String> carbonCopysSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(carbonCopys)) {
            carbonCopysSet.addAll(carbonCopys);
            carbonCopysSet.removeAll(receiversSet);
        }

        long startTimestamp  = System.currentTimeMillis();
        logger.info("Start send Mail...");
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(deliver);
            message.setTo(convertCollectionToArray(receiversSet));
            message.setCc(convertCollectionToArray(carbonCopysSet));
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            logger.info("Send mail success, cost {} million seconds", System.currentTimeMillis() - startTimestamp);
        } catch (MailException e) {
            e.printStackTrace();
            logger.error("Send mail failed, error message is : {} \n", e.getMessage());
        }
    }

    /**
     *
     * @param deliver 发送者邮箱
     * @param receiver 接收者们邮箱
     * @param carbonCopy 抄送者们邮件地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml 内容是否是否是Html格式
     */
    public void sendHtmlMail(String deliver, String receiver, String carbonCopy, String subject, String content, boolean isHtml){
        Set<String> receivers = new HashSet<>();
        receivers.add(receiver);
        Set<String> carbonCopys = new HashSet<>();
        carbonCopys.add(carbonCopy);
        this.sendHtmlMail(deliver, receivers, carbonCopys, subject, content, isHtml);
    }

    /**
     *
     * @param deliver 发送者邮箱
     * @param receivers 接收者们邮箱
     * @param carbonCopys 抄送者们邮件地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml 内容是否是否是Html格式
     */
    @SuppressWarnings("Duplicates")
    public void sendHtmlMail(String deliver, Collection<String> receivers, Collection<String> carbonCopys, String subject, String content, boolean isHtml){
        checkEmpty(deliver, receivers, subject, content);
        Set<String> receiversSet = new HashSet<>(receivers);
        Set<String> carbonCopysSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(carbonCopys)) {
            carbonCopysSet.addAll(carbonCopys);
            carbonCopysSet.removeAll(receiversSet);
        }

        long startTimestamp  = System.currentTimeMillis();
        logger.info("Start send Mail...");
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom(deliver);
            messageHelper.setTo(convertCollectionToArray(receiversSet));
            messageHelper.setCc(convertCollectionToArray(carbonCopysSet));
            messageHelper.setSubject(subject);
            messageHelper.setText(content, isHtml);
            mailSender.send(message);
            logger.info("Send mail success, cost {} million seconds", System.currentTimeMillis() - startTimestamp);
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("Send mail failed, error message is : {} \n", e.getMessage());
        }
    }

    /**
     *
     * @param deliver 发送者邮箱
     * @param receiver 接收者邮箱
     * @param carbonCopy 抄送者邮件地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml 内容是否是否是Html格式
     * @param fileName 文件名
     * @param file 文件
     */
    public void sendAttachmentFileMail(String deliver, String receiver, String carbonCopy, String subject, String content, boolean isHtml, String fileName, File file){
        Set<String> receivers = new HashSet<>();
        receivers.add(receiver);
        Set<String> carbonCopys = new HashSet<>();
        carbonCopys.add(carbonCopy);
        this.sendAttachmentFileMail(deliver, receivers, carbonCopys, subject, content, isHtml, fileName, file);
    }

    /**
     *
     * @param deliver 发送者邮箱
     * @param receivers 接收者们邮箱
     * @param carbonCopys 抄送者们邮件地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml 内容是否是否是Html格式
     * @param fileName 文件名
     * @param file 文件
     */
    @SuppressWarnings("Duplicates")
    public void sendAttachmentFileMail(String deliver, Collection<String> receivers, Collection<String> carbonCopys, String subject, String content, boolean isHtml, String fileName, File file){
        checkEmpty(deliver, receivers, subject, content);
        Set<String> receiversSet = new HashSet<>(receivers);
        Set<String> carbonCopysSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(carbonCopys)) {
            carbonCopysSet.addAll(carbonCopys);
            carbonCopysSet.removeAll(receiversSet);
        }

        long startTimestamp  = System.currentTimeMillis();
        logger.info("Start send Mail...");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(deliver);
            messageHelper.setTo(convertCollectionToArray(receiversSet));
            messageHelper.setCc(convertCollectionToArray(carbonCopysSet));
            messageHelper.setSubject(subject);
            messageHelper.setText(content, isHtml);
            messageHelper.addAttachment(fileName, file);
            mailSender.send(message);
            logger.info("Send mail success, cost {} million seconds", System.currentTimeMillis() - startTimestamp);
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("Send mail failed, error message is : {} \n", e.getMessage());
        }
    }

    /**
     *
     * @param deliver 发送者邮箱
     * @param receiver 接收者邮箱
     * @param carbonCopy 抄送者邮件地址
     * @param subject 邮件主题
     * @param template 模板地址
     * @param context 邮件内容
     */
    public void sendTemplateMail(String deliver, String receiver, String carbonCopy, String subject, String template, Context context){
        Set<String> receivers = new HashSet<>();
        receivers.add(receiver);
        Set<String> carbonCopys = new HashSet<>();
        carbonCopys.add(carbonCopy);
        this.sendTemplateMail(deliver, receivers, carbonCopys, subject, template, context);
    }

    /**
     *
     * @param deliver 发送者邮箱
     * @param receivers 接收者们邮箱
     * @param carbonCopys 抄送者们邮件地址
     * @param subject 邮件主题
     * @param template 模板地址
     * @param context 邮件内容
     */
    @SuppressWarnings("Duplicates")
    public void sendTemplateMail(String deliver, Collection<String> receivers, Collection<String> carbonCopys, String subject, String template, Context context){
        checkEmpty(deliver, receivers, subject, template, context);
        Set<String> receiversSet = new HashSet<>(receivers);
        Set<String> carbonCopysSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(carbonCopys)) {
            carbonCopysSet.addAll(carbonCopys);
            carbonCopysSet.removeAll(receiversSet);
        }

        long startTimestamp  = System.currentTimeMillis();
        logger.info("Start send Mail...");
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
            String content = templateEngine.process(template, context);
            messageHelper.setFrom(deliver);
            messageHelper.setTo(convertCollectionToArray(receiversSet));
            messageHelper.setCc(convertCollectionToArray(carbonCopysSet));
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
            mailSender.send(message);
            logger.info("Send mail success, cost {} million seconds", System.currentTimeMillis() - startTimestamp);
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("Send mail failed, error message is : {} \n", e.getMessage());
        }
    }

    private static void checkEmpty(String deliver, Collection<String> receivers, String subject, String content) {
        if (StringUtils.isEmpty(deliver))
            throw new IllegalArgumentException("deliver can not be empty");
        if (CollectionUtils.isEmpty(receivers))
            throw new IllegalArgumentException("receivers can not be empty");
        if (StringUtils.isEmpty(subject))
            throw new IllegalArgumentException("subject can not be empty");
        if (StringUtils.isEmpty(content))
            throw new IllegalArgumentException("content can not be empty");
    }

    private static void checkEmpty(String deliver, Collection<String> receivers, String subject, String template, Context context) {
        if (StringUtils.isEmpty(deliver))
            throw new IllegalArgumentException("deliver can not be empty");
        if (CollectionUtils.isEmpty(receivers))
            throw new IllegalArgumentException("receivers can not be empty");
        if (StringUtils.isEmpty(subject))
            throw new IllegalArgumentException("subject can not be empty");
        if (StringUtils.isEmpty(template))
            throw new IllegalArgumentException("template can not be empty");
        if (StringUtils.isEmpty(context))
            throw new IllegalArgumentException("context can not be empty");
    }

    private static String[] convertCollectionToArray(Collection<String> c){
        String[] result = new String[]{};
        if (CollectionUtils.isEmpty(c))
            return result;
        Iterator<String> iterator = c.iterator();
        if (iterator.hasNext()){
            String s = iterator.next();
            if (s == null || "".equals(s))
                iterator.remove();
        }

        int size = c.size();
        if (size > 0) {
            result = new String[size];
            result = c.toArray(result);
        }

        return result;
    }

}
