package fit.fancyday.common.tencent;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ims.v20201229.ImsClient;
import com.tencentcloudapi.ims.v20201229.models.ImageModerationRequest;
import com.tencentcloudapi.ims.v20201229.models.ImageModerationResponse;
import fit.fancyday.common.propertise.ImageProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Image content censorship
 *
 * @author 喵酱不熬夜
 * @date 2022/10/16
 */
public class GreenImage {
    @Autowired
    private ImageProperties imageProperties;

    public GreenImage(ImageProperties imageProperties) {
        this.imageProperties = imageProperties;
    }

    /**
     * Image content censorship
     *
     * @param fileUrl 图片URL
     * @return {@code String}
     */
    public String greenImageURL(String fileUrl) {
        String secretId = imageProperties.getSecretId();
        String secretKey = imageProperties.getSecretKey();
        ImageModerationResponse resp = null;
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ims.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            ImsClient client = new ImsClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            ImageModerationRequest req = new ImageModerationRequest();
            req.setFileUrl(fileUrl);
            // 返回的resp是一个ImageModerationResponse的实例，与请求对象对应
            resp = client.ImageModeration(req);
        } catch (TencentCloudSDKException e) {
            return e.toString();
        }
        // 输出json格式的字符串回包
        return ImageModerationResponse.toJsonString(resp);
    }

    /**
     * Image content censorship
     *
     * @param base64 base64
     * @return {@code String}
     */
    public String greenImageBase64(String base64) {
        String secretId = imageProperties.getSecretId();
        String secretKey = imageProperties.getSecretKey();
        ImageModerationResponse resp = null;
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ims.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            ImsClient client = new ImsClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            ImageModerationRequest req = new ImageModerationRequest();
            req.setFileContent(base64);
            // 返回的resp是一个ImageModerationResponse的实例，与请求对象对应
            resp = client.ImageModeration(req);
        } catch (TencentCloudSDKException e) {
            return e.toString();
        }
        // 输出json格式的字符串回包
        return ImageModerationResponse.toJsonString(resp);
    }
}
