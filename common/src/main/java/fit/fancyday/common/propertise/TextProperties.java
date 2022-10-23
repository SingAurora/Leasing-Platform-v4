package fit.fancyday.common.propertise;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tencent.text")
public class TextProperties {
    private String secretId;
    private String secretKey;
}
