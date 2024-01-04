package passwordvaltapp;

import java.security.SecureRandom;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendEmail {

    private final JavaMailSender mailSender;
    
    org.apache.logging.log4j.Logger logger = LogManager.getLogger(SendEmail.class);

    @Autowired
    public SendEmail(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String toEmail) {
        String subject = "Password Reset OTP";
        String otp = generateOtp();
        String body = "Your OTP for password reset is: " + otp;

        GlobalContext.setCurrentOtp(otp);
        sendMail(toEmail, subject, body);
        System.out.println(GlobalContext.getCurrentOtp());
         
    }

    public boolean verifyOtp(String enteredOtp, String expectedOtp) {
        // Compare entered OTP with the expected OTP
        return enteredOtp.equals(expectedOtp);
    }

    private String generateOtp() {
        // Generate a 6-digit random OTP
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    
    public void sendMail(String toEmail, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("inventorymanagementsystem2023@gmail.com");
            helper.setTo(toEmail);
            helper.setText(body);
            helper.setSubject(subject);
            
            mailSender.send(message);
           
            logger.info("Mail sent successfully");
        } catch (MessagingException e) {
            logger.error("Failed to send email: " + e.getMessage());
        }
    }
}

