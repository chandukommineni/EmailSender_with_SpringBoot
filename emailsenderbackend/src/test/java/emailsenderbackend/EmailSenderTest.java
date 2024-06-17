package emailsenderbackend;

import emailsenderbackend.helper.Message;
import emailsenderbackend.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
public class EmailSenderTest {

    @Autowired
    private EmailService emailService;

    @Test
    void emailSendTest(){
  emailService.sendEmail("chandukommineni63@gmail.com","dummy","hello");

    }

    @Test
    void sendHtmlInEmail(){
        String html="<h1>hello from chandu</h1>";

        emailService.sendEmailWithHtml("chandukommineni63@gmail.com","hello",html);
    }
//
//    @Test
//    void sendEmailWithFile(){
//        emailService.sendEmailWithFile("chandukommineni63@gmail.com","dummy","hello",new File("C:/Users/chand/IdeaProjects/EmailSenderApplication/emailsenderbackend/src/main/resources/static/dummy"));
//    }
//    @Test
//    void sendEmailWithFileWithStream(){
//        File file=new File("C:/Users/chand/IdeaProjects/EmailSenderApplication/emailsenderbackend/src/main/resources/static/dummy");
//        try {
//            InputStream is=new FileInputStream(file);
//            emailService.sendEmailWithFile("chandukommineni63@gmail.com","dummy","hello from chandu",is);
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Test
    void getInbox(){
     List<Message> inboxMessages =  emailService.getInboxMessages();
     inboxMessages.forEach(item->{
         System.out.println(item.getSubjects());
         System.out.println(item.getContent());
         System.out.println(item.getFiles());


     });
    }
}
