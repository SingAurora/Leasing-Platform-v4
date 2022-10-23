package fit.fancyday.common.propertise;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tencent.image")
public class ImageProperties {
    private String secretId;
    private String secretKey;
}
