//package passwordvaltapp;
//
//import java.util.Properties;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//public class mailtest {
//    
//    private JavaMailSender createMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com"); //  SMTP host
//        mailSender.setPort(587); //  SMTP port
//        mailSender.setUsername("inventorymanagementsystem2023@gmail.com"); //  SMTP username
//        mailSender.setPassword("qvaqyxpfkhufguas"); // SMTP password
//
//        // Enable TLS
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.smtp.starttls.enable", "true");
//
//        return mailSender;
//    }
//    
//    JavaMailSender mailSender = createMailSender();
//    
//    SendEmail test = new SendEmail(mailSender);
//    
//    public void testBro(){
//         String toEmail = "jayac5818@gmail.com";
//        String subject = "Subject of the email";
//        String body = "Body of the email";
//        String filePath = "C:\\IMS3\\Bills\\jj.pdf";
//        
//        test.sendOtp(toEmail);
//    }
//        
//    
//}
