package fit.fancyday.common.propertise;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tencent.sms")
public class SmsProperties {
    private String secretId;
    private String secretKey;
    private String smsSdkAppId;
    private String signName;
    private String waitTime;
}
