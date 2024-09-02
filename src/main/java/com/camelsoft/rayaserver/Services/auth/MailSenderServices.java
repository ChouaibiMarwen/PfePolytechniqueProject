package com.camelsoft.rayaserver.Services.auth;

import com.camelsoft.rayaserver.Models.User.users;
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

    public void sendEmailResetPassword(String content, users to, Date date) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");

        helper.setTo(to.getEmail());
        msg.setFrom("noreply@rest.net.sa");
        helper.setSubject("Raya Reset password");
        helper.setText(
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Title</title>\n" +
                        "</head>\n" +
                        "<body style=\"margin: 0;padding: 0;font-family: 'Poppins', sans-serif;width:100%;text\">\n" +
                        "    <table style=\"width: 100%;max-width: 600px;margin: 50px auto;background-color: #ffffff;border-radius: 10px;box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);padding: 30px;border-collapse: collapse;\">\n" +
                        "        <tr>\n" +
                        "            <td style=\"text-align: center;\">\n" +
                        /*"                <img src=\"https://restforyou.nyc3.cdn.digitaloceanspaces.com/mail/header-img.png\" alt=\"Header Image\" style=\"max-width: 100%;height: auto;\">\n" +*/
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"text-align: center;margin-top: 30px;\">\n" +
                        "                <h2>Forgot your password?</h2>\n" +
                        "                <p>We received a request to reset your password. If you didn't make this request, simply ignore this email.</p>\n" +
                        "                <p>If you did make this request, just enter the following code below:</p>\n" +
                        "                <button class=\"button\" style=\"background-color: #932de8;border: none;color: #ffffff;padding: 10px 20px;font-size: 16px;border-radius: 5px;text-decoration: none;\">"+content+"</button>\n" +
                        "                <p>If you didn't request to change your brand password, you don't have to do anything. So that's easy.</p>\n" +
                        "            </td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "            <td style=\"text-align: center;\">\n" +
                       /* "\t\t\t\t<img src=\"https://restforyou.nyc3.cdn.digitaloceanspaces.com/mail/Rest%20for%20u%20Logo-01.png\" style=\"width: 90px;height: 30px;margin:20px 0\">\n" +*/
                        "            </td>\n" +
                        "        </tr>\n" +
                        "    </table>\n" +
                        "</body>\n" +
                        "</html>", true);
        this.mailSender.send(msg);
    }


}
