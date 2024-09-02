package com.camelsoft.rayaserver.Services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

@Service
public class MailSenderServices {

    @Autowired
    private JavaMailSender mailSender;

    /*@Autowired
    private SimpleMailMessage preConfiguredMessage;
*/


  /*  public boolean sendEmailCheck(String subject, String to, users user,String passwd) {
        try {

            SimpleMailMessage msg = new SimpleMailMessage(preConfiguredMessage);
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText("Hi "+user.getName()+"\n" +
                    "You recently requested a code to verify you email .Your Password is: " +passwd + ". \n"
                    +"\n" + "Thanks,\n Rest For you Team"
            );
            mailSender.send(msg);
            return true;
        } catch (Exception ex) {
            return false; // Email does not exist
        }
    }*/

    public boolean emailnewpassword(String name, String email, String pass) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            msg.setFrom("test@camel-soft.com");

            // true = multipart message
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("New password for Raya dashboard");

            // External URLs for images
          /*  String image1Url = "https://waynekstorage.nyc3.cdn.digitaloceanspaces.com/contactus/contactus/1724841089609contactus.png";
            String backgroundImageUrl = "https://waynekstorage.nyc3.cdn.digitaloceanspaces.com/contactus/contactus/1724840930032background.png";*/

            String htmlContent = "<html>" +
                    "<head>" +
                    "<style>" +
/*
                    "body { font-family: Arial, sans-serif; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; min-height: 100vh; background: url('" + backgroundImageUrl + "') no-repeat center center fixed; background-size: cover; }" +
*/
                    ".container { background-color: rgba(255, 255, 255, 0.9); padding: 20px; width: 100%; max-width: 600px; border-radius: 8px; text-align: left; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); }" +
                    ".content-line { display: flex; justify-content: flex-start; align-items: center; margin-bottom: 10px; }" +
                    ".value { word-wrap: break-word; flex-grow: 1; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                 /*   "<img src='" + image1Url + "' alt='Contact Us Image' class='header-image' style='width: 150px; height: 120px; margin-bottom: 20px;'>" +*/
                  /*  "<div class='content-line' style='display: flex; justify-content: flex-start; align-items: center; margin-bottom: 10px;'>" +
                    "<span style='font-weight: bold; width: 120px; flex-shrink: 0;'>Name:</span>" +
                    "<span class='value'>" + name + "</span>" +
                    "</div>" +*/
                    "<div class='content-line' style='display: flex; justify-content: flex-start; align-items: center; margin-bottom: 10px;'>" +
                    "<span style='font-weight: bold; width: 120px; flex-shrink: 0;'>Your new password is :</span>" +
                    "<span class='value'>" + pass + "</span>" +
                    "</div>" +
                    /*"<div class='content-line' style='display: flex; justify-content: flex-start; align-items: center; margin-bottom: 10px;'>" +
                    "<span style='font-weight: bold; width: 120px; flex-shrink: 0;'>Phone number:</span>" +
                    "<span class='value'>" + phone + "</span>" +
                    "</div>" +*/
                   /* "<div class='content-line' style='display: flex; justify-content: flex-start; align-items: center; margin-bottom: 10px;'>" +
                    "<span style='font-weight: bold; width: 120px; flex-shrink: 0;'>Content:</span>" +
                    "</div>" +*/
                    "<p>Thanks,<br>Raya Support Team</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";


            // Set the email content as HTML
            helper.setText(htmlContent, true);

            mailSender.send(msg);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false; // Email does not exist
        }
    }


}
