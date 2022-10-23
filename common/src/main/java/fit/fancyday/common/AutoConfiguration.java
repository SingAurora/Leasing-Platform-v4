package fit.fancyday.common;


import fit.fancyday.common.propertise.*;
import fit.fancyday.common.tencent.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@EnableConfigurationProperties({
        SmsProperties.class,
        CosProperties.class,
        FaceProperties.class,
        TextProperties.class,
        ImageProperties.class,
        OcrProperties.class
})
public class AutoConfiguration {
    @Bean
    public SmsTemplate smsTemplate(SmsProperties properties) {
        return new SmsTemplate(properties);
    }

    @Bean
    public CosTemplate cosTemplate(CosProperties properties) {
        return new CosTemplate(properties);
    }

    @Bean
    public DetectFace detectFace(FaceProperties faceProperties) {
        return new DetectFace(faceProperties);
    }

    @Bean
    public GreenImage greenImage(ImageProperties imageProperties) {
        return new GreenImage(imageProperties);
    }

    @Bean
    public GreenText greenText(TextProperties textProperties) {
        return new GreenText(textProperties);
    }

    @Bean
    public TextRecognition textRecognition(OcrProperties ocrProperties) {
        return new TextRecognition(ocrProperties);
    }

    @Bean
    public MailTemplate mailTemplate() {
        return new MailTemplate();
    }
}
