package fit.fancyday.common.propertise;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tencent.face")
public class FaceProperties {
    private String secretId;
    private String secretKey;
}
