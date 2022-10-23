package fit.fancyday.common.propertise;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "baidu.ocr")
public class OcrProperties {
    private String accessToken;
}
