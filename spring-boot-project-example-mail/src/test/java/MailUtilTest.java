import com.chasegnil.Application;
import com.chasegnil.mail.MailUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.context.Context;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class MailUtilTest {

    @Autowired
    private MailUtil mailUtil;

    // TODO 设置发件人
    private String deliver = "xxx@xxx.com";
    private Set<String> receivers = new HashSet<>();
    private Set<String> carbonCopys = new HashSet<>();

    @Before
    public void initReceiversAndCarbonCopys(){
        // TODO 设置收件人和抄送人
        receivers.add("xxxx@xxx.com");
        carbonCopys.add("xxxxx@xxx.com");
    }

    @Test
    public void sendSimpleMail(){
        String subject = "This is a simple email";
        String content = "This is a simple content";
        mailUtil.sendSimpleMail(deliver, receivers, carbonCopys, subject, content);
    }

    @Test
    public void sendHtmlMail(){
        String subject = "This is a Html Style email";
        String content = "<h1> This is a Html Style content mail </h1>";
        mailUtil.sendHtmlMail(deliver, receivers, carbonCopys, subject, content, true);
    }

    @Test
    public void sendAttachmentFileMail(){
        String subject = "This is an attachment file email";
        String content = "<h1> This is an attachment file email </h1>";
        File file = new File("E:\\opt\\netty\\netty-demo-telnet\\logs\\info.log");
        String fileName = file.getName();
        mailUtil.sendAttachmentFileMail(deliver, receivers, carbonCopys, subject, content, true, fileName, file);
    }

    @Test
    public void sendTemplateEmail() {
        String template = "mail/InternalServerErrorTemplate";
        String subject = "This is a template email";
        Context context = new Context();
        String errorMessage;

        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            errorMessage = writer.toString();
        }

        context.setVariable("username", "HelloWood");
        context.setVariable("methodName", "MailUtilTest.sendTemplateMail()");
        context.setVariable("errorMessage", errorMessage);
        context.setVariable("occurredTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        mailUtil.sendTemplateMail(deliver, receivers, carbonCopys, subject, template, context);
    }



}
