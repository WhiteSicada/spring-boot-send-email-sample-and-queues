package com.adnane.sendemail.service.impl;

import com.adnane.sendemail.model.Email;
import com.adnane.sendemail.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class EmailServiceImpl implements EmailService {

   private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


   @Value("${spring.mail.from}")
   private String from;

   @Autowired
   private JavaMailSender mailSender;

   @RabbitListener(queues = {"${rabbitmq.queue.name}"})
   public void consume(String message) {
      LOGGER.info(String.format("Received message -> %s", message));
   }

   @RabbitListener(queues = {"${rabbitmq.queue.json.name}"})
   public void consumeJsonMessage(Email response) throws UnknownHostException {
      sendSimpleEmail(response);
   }


   @Override
   public void sendSimpleEmail(Email response) throws UnknownHostException {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(from);
      message.setTo(response.getEmail());
      message.setText(response.getBody());
      message.setSubject(response.getSubject());
      mailSender.send(message);
   }

   @Override
   public void sendEmailWithAttachment(String toEmail, String subject, String body, Map<String, InputStreamSource> attachments) throws MessagingException {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
      mimeMessageHelper.setFrom(from);
      mimeMessageHelper.setTo(toEmail);
      mimeMessageHelper.setText(body);
      mimeMessageHelper.setSubject(subject);
      for (Map.Entry<String, InputStreamSource> attachment : attachments.entrySet()) {
         mimeMessageHelper.addAttachment(attachment.getKey(), attachment.getValue());
      }
      mailSender.send(mimeMessage);
   }
}
