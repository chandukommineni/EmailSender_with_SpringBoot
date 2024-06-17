package emailsenderbackend.services.implementations;

import emailsenderbackend.helper.Message;
import emailsenderbackend.services.EmailService;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.management.remote.JMXServerErrorException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom("your-email@gmail.com");
        mailSender.send(simpleMailMessage);
        logger.info("Email has been sent");
    }

    @Override
    public void sendEmail(String[] to, String subject, String message) {
        // Implementation here
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom("your-email@gmail.com");
        mailSender.send(simpleMailMessage);
        logger.info("Email has been sent");
    }

    @Override
    public void sendEmailWithHtml(String to, String subject, String htmlContent) {
        MimeMessage simpleMailMessage=mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper=new MimeMessageHelper(simpleMailMessage,true,"UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("abc@gmail.com");
            helper.setText(htmlContent,true);
            mailSender.send(simpleMailMessage);
            logger.info("Email has been sent");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sendEmailWithFile(String to, String subject, String message, File file) {
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("abc@gmail.com");
            helper.setText(message);
            FileSystemResource fileSystemResource=new FileSystemResource(file);
            helper.addAttachment(fileSystemResource.getFilename(),file);
            mailSender.send(mimeMessage);
            logger.info("email with file sended");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void sendEmailWithFile(String to, String subject, String message, InputStream is) {
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("abc@gmail.com");
            helper.setText(message);

            File file=new File("src/main/resources/email/test.text");
            Files.copy(is,file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            FileSystemResource fileSystemResource=new FileSystemResource(file);
            helper.addAttachment(fileSystemResource.getFilename(),file);
            mailSender.send(mimeMessage);
            logger.info("email with file sended");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Value("${mail.store.protocol}")
   String protocol;

    @Value("${mail.imaps.host}")
    String host;
    @Value("${mail.imaps.port}")
    String port;

    @Value(("${spring.mail.username}"))
    String username;

    @Value("${spring.mail.password}")
    String password;
    @Override
    public List<Message> getInboxMessages() {
        Properties configurations=new Properties();
        configurations.setProperty("mail.store.protocol",protocol);
        configurations.setProperty("mail.imaps.host",host);
        configurations.setProperty("mail.imaps.port",port);
        Session session=Session.getDefaultInstance(configurations);
        try {
            Store store=session.getStore();
            store.connect(username,password);
            Folder inbox=store.getFolder("INBOX");
            inbox.open((Folder.READ_ONLY));
            jakarta.mail.Message[] messages=inbox.getMessages();
            List<Message> list=new ArrayList<>();
            for(jakarta.mail.Message message:messages){
                String content=getContentFromEmailMessage(message);
                List<String> files=getFilesFromEmailMessage(message);
                list.add(Message.builder().subjects(message.getSubject()).content(content).files(files).build());
            }
            return list;
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private List<String> getFilesFromEmailMessage(jakarta.mail.Message message) throws MessagingException, IOException {
        List<String> files=new ArrayList<>();
        if (message.isMimeType("multipart/*")){
           Multipart content=(Multipart) message.getContent();
            for (int i=0;i<content.getCount();i++){
                BodyPart bodyPart=content.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())){
                 InputStream inputStream=  bodyPart.getInputStream();
                 File file=new File("src/main/resources/email"+bodyPart.getFileName());
                 Files.copy(inputStream,file.toPath(),StandardCopyOption.REPLACE_EXISTING);


                 files.add(file.getAbsolutePath());
                }
            }
        }
        return files;


    }

    private String getContentFromEmailMessage(jakarta.mail.Message message) {
        try {
            if (message.isMimeType("text/plain")|| message.isMimeType("text/html")){
           String content=  (String) message.getContent();
           return content;
            }
            else if (message.isMimeType("multipart/*")){
                Multipart part=  (Multipart) message.getContent();
                for (int i=0;i<part.getCount();i++){
                    BodyPart bodyPart=part.getBodyPart(i);
                    if (bodyPart.isMimeType("text/plain")){
                        return (String )bodyPart.getContent();
                    }
                }
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
