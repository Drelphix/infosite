package info.infosite.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class MailService {
    @Autowired
    public JavaMailSender emailSender;

    @Async
    public void SendEmail(String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("Infosite Alarm");
        message.setTo("admin@autospace.by");
        message.setSubject(title);
        message.setText(text);

        // Send Message!
        this.emailSender.send(message);
    }
}
