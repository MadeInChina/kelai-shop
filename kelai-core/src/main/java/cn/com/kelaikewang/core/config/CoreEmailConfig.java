package cn.com.kelaikewang.core.config;

import cn.com.kelaikewang.core.email.service.message.ThymeleafMessageCreator;
import org.broadleafcommerce.common.email.service.info.EmailInfo;
import org.broadleafcommerce.common.email.service.message.MessageCreator;
import org.broadleafcommerce.common.notification.service.type.NotificationEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;

import java.util.Properties;

/**
 * Shared email configuration
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Configuration
public class CoreEmailConfig {

    @Value("${mailSender.host}")
    private String host;

    @Value("${mailSender.port}")
    private int port;

    @Value("${mailSender.username}")
    private String username;

    @Value("${mailSender.password}")
    private String password;

    @Value("${mailSender.fromAddress}")
    private String fromAddress;

    /**
     * A dummy mail sender has been set to send emails for testing purposes only
     * To view the emails sent use "DevNull SMTP" (download separately) with the following setting: 
     *   Port: 30000
     */
    @Bean
    public JavaMailSender blMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setHost(host);
        sender.setPassword(password);
        sender.setUsername(username);
        sender.setPort(port);
        sender.setDefaultEncoding("UTF-8");

        //sender.setProtocol("smtp");
        Properties javaMailProps = new Properties();
        //javaMailProps.setProperty("mail.smtp.starttls.enable", ""+true);
        //javaMailProps.setProperty("mail.smtp.timeout", "25000");
        javaMailProps.setProperty("mail.smtp.port", port + "");//设置端口
        javaMailProps.setProperty("mail.smtp.socketFactory.port", port + "");//设置ssl端口
        javaMailProps.setProperty("mail.smtp.socketFactory.fallback", "false");
        javaMailProps.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProps.put("mail.smtp.ssl.trust", host);

        javaMailProps.put("mail.smtp.ssl.enable", true);
        sender.setJavaMailProperties(javaMailProps);
        return sender;
    }
    
    /**
     * Uncomment this bean to send real emails
     */
    @Bean
    @Autowired
    public MessageCreator blMessageCreator(@Qualifier("blEmailTemplateEngine") TemplateEngine tlTemplateEngine, @Qualifier("blMailSender") JavaMailSender mailSender) {
        return new ThymeleafMessageCreator(tlTemplateEngine, mailSender);
    }
    
/*    @Bean
    @Autowired
    public MessageCreator blMessageCreator(@Qualifier("blMailSender") JavaMailSender mailSender) {
        return new NullMessageCreator(mailSender);
    }*/
    
    //@Bean
    public EmailInfo blEmailInfo() {
        EmailInfo info = new EmailInfo();
        info.setFromAddress(fromAddress);
        info.setSendAsyncPriority("2");
        info.setSendEmailReliableAsync("false");
        return info;
    }
    
    @Bean
    public EmailInfo blRegistrationEmailInfo() {
        EmailInfo info = blEmailInfo();
        info.setSubject("You have successfully registered!");
        info.setEmailTemplate("register-email");
        return info;
    }
    
    @Bean
    public EmailInfo blForgotPasswordEmailInfo() {
        EmailInfo info = blEmailInfo();
        info.setSubject("Reset password request");
        info.setEmailTemplate("resetPassword-email");
        return info;
    }
    
    @Bean
    public EmailInfo blOrderConfirmationEmailInfo() {
        EmailInfo info = blEmailInfo();
        info.setEmailType(NotificationEventType.ORDER_CONFIRMATION.getType());
        info.setSubject("蔚来商务订单确认");
        info.setEmailTemplate("orderConfirmation-email");
        return info;
    }
    
    @Bean
    public EmailInfo blFulfillmentOrderTrackingEmailInfo() {
        EmailInfo info = blEmailInfo();
        info.setSubject("Your order with The Heat Clinic Has Shipped");
        info.setEmailTemplate("fulfillmentOrderTracking-email");
        return info;
    }
    
    @Bean
    public EmailInfo blReturnAuthorizationEmailInfo() {
        EmailInfo info = blEmailInfo();
        info.setSubject("Your return with The Heat Clinic");
        info.setEmailTemplate("returnAuthorization-email");
        return info;
    }
    
    @Bean
    public EmailInfo blReturnConfirmationEmailInfo() {
        EmailInfo info = blEmailInfo();
        info.setSubject("Your return with The Heat Clinic");
        info.setEmailTemplate("returnConfirmation-email");
        return info;
    }

    @Bean("blCodeEmailInfo")
    public EmailInfo blCodeEmailInfo(){
        EmailInfo info = blEmailInfo();
        info.setSubject("邮件验证码");
        info.setEmailTemplate("code-email");
        return info;
    }
    @Bean
    public EmailInfo blInventoryNotificationEmailInfo(){
        EmailInfo info = blEmailInfo();
        info.setSubject("到货通知");
        info.setEmailTemplate("inventoryNotification");
        return info;
    }
}
