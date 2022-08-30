package com.adnane.sendemail.service;

import com.adnane.sendemail.model.Email;
import org.springframework.core.io.InputStreamSource;

import javax.mail.MessagingException;
import java.net.UnknownHostException;
import java.util.Map;

public interface EmailService {
   void sendSimpleEmail(Email response) throws UnknownHostException;
   void sendEmailWithAttachment(String toEmail, String subject, String body, Map<String, InputStreamSource> attachments) throws MessagingException;
}
