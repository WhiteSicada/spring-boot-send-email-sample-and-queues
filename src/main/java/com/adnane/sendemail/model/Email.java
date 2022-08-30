package com.adnane.sendemail.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Email {
   private String email;
   private String subject;
   private String body;
}
