package com.adnane.sendemail.controller;

import com.adnane.sendemail.model.Email;
import com.adnane.sendemail.publisher.RabbitMQJsonProducer;
import com.adnane.sendemail.publisher.RabbitMQProducer;
import com.adnane.sendemail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

   @Autowired
   private EmailService emailService;

   @Autowired
   private RabbitMQProducer producer;

   @Autowired
   private RabbitMQJsonProducer jsonProducer;

   @PostMapping("/sendEmail")
   @ResponseStatus(OK)
   public void sendEmail(@RequestBody Email request) throws UnknownHostException {
      emailService.sendSimpleEmail(request);
   }

   // http://localhost:8080/api/v1/publish?message=hello
   @GetMapping("/publishString")
   public String sendMessage(@RequestParam("message") String message){
      producer.sendMessage(message);
      return "Message sent to RabbitMQ ...";
   }

   @PostMapping("/publishJson")
   public String sendJsonMessage(@RequestBody Email request){
      jsonProducer.sendJsonMessage(request);
      return "Json message sent to RabbitMQ ...";
   }
}
