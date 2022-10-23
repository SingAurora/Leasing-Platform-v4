package fit.fancyday.common;

import cn.hutool.extra.mail.MailUtil;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest(classes = CommonApplication.class)
@RunWith(SpringRunner.class)
public class MailTemplateTest extends TestCase {

    @Test
    public void testGetJavaMailSender() {
//        SimpleMailMessage simpleMessage = new SimpleMailMessage();
//        simpleMessage.setFrom("2414786389@qq.com");
//        simpleMessage.setTo("2414786389@qq.com");
//        simpleMessage.setSubject("test");
//        simpleMessage.setText("hello");
//        JavaMailSender javaMailSender = mailTemplate.getJavaMailSender();
//        javaMailSender.send(simpleMessage);
        MailUtil.send("2414786389@qq.com", "测试", "邮件来自Hutool测试", false);
    }


}