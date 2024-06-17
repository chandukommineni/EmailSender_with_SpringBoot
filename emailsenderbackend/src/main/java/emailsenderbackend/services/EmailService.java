package emailsenderbackend.services;

import emailsenderbackend.helper.Message;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.List;


public interface EmailService {
    //send to single person
    void sendEmail(String to,String subject,String message);
    //send to multiple persons
    void sendEmail(String[] to,String subject,String message);
    //send email with html
    void sendEmailWithHtml(String to,String subject,String htmlContent);
    //send email with file
    void sendEmailWithFile(String to, String subject, String message, File file);
    void sendEmailWithFile(String to, String subject, String message, InputStream is);

 List<Message> getInboxMessages();
}
