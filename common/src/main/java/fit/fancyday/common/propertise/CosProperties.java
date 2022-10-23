package fit.fancyday.common.propertise;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tencent.cos")
public class CosProperties {
    private String secretId;
    private String secretKey;
    private String bucketName;
    private String pathKey;
}
