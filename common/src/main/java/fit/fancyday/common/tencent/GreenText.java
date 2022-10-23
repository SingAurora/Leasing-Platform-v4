package fit.fancyday.common.tencent;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tms.v20201229.TmsClient;
import com.tencentcloudapi.tms.v20201229.models.TextModerationRequest;
import com.tencentcloudapi.tms.v20201229.models.TextModerationResponse;
import fit.fancyday.common.propertise.TextProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 喵酱不熬夜
 * @date 2022/10/16
 */
public class GreenText {
    @Autowired
    private TextProperties textProperties;

    public GreenText(TextProperties textProperties) {
        this.textProperties = textProperties;
    }

    /**
     * 文本内容审查
     *
     * @param detectedContent 要被检测的文本字符串
     * @return
     */
    public String greenTestScan(String detectedContent) {
        String secretId = textProperties.getSecretId();
        String secretKey = textProperties.getSecretKey();
        TextModerationResponse resp = null;
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            TmsClient client = new TmsClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            TextModerationRequest req = new TextModerationRequest();
            // 注入要被检测的内容
            req.setContent(detectedContent);
            // 返回的resp是一个TextModerationResponse的实例，与请求对象对应
            resp = client.TextModeration(req);

        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        // 输出json格式的字符串回包
        return TextModerationResponse.toJsonString(resp);
    }
}
