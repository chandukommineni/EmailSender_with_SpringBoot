package emailsenderbackend.controllers;

import emailsenderbackend.helper.CustomResponse;
import emailsenderbackend.helper.EmailRequest;
import emailsenderbackend.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private EmailService service;
    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request){

     service.sendEmailWithHtml(request.getTo(), request.getSubject(), request.getMessage());
     return ResponseEntity.ok(CustomResponse.builder().message("Email Sent Successfully").status(HttpStatus.OK).success(true).build());
    }

    @PostMapping("/sendwithfile")
    public ResponseEntity<CustomResponse> sendEmailWithFile(@RequestParam("recipient") String recipient,
                                                            @RequestParam("subject") String subject,
                                                            @RequestParam("message") String message,
                                                            @RequestParam("file") MultipartFile file) throws IOException {
     service.sendEmailWithFile(recipient, subject, message,file);
     return ResponseEntity.ok(CustomResponse.builder().message("Email Sent Successfully").status(HttpStatus.OK).success(true).build());

    }

}
