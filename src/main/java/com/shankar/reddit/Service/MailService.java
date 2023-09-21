package com.shankar.reddit.Service;

import com.shankar.reddit.entity.NotificationEmail;
import com.shankar.reddit.exception.SpringRedditException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    void sendEmail(NotificationEmail notificationEmail){

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("admin@reddit.co");
            messageHelper.setTo(notificationEmail.getReciepient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build((notificationEmail.getBody())));
        };

        try {
            javaMailSender.send(messagePreparator);
            log.info("Activation mail sent");
        } catch (MailException e){
            throw new SpringRedditException("Activation mail sending is failed");
        }


    }
}
