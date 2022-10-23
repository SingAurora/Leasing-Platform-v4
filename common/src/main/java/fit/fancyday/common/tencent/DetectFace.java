package fit.fancyday.common.tencent;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.iai.v20200303.IaiClient;
import com.tencentcloudapi.iai.v20200303.models.DetectFaceRequest;
import com.tencentcloudapi.iai.v20200303.models.DetectFaceResponse;
import fit.fancyday.common.propertise.FaceProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DetectFace {
    @Autowired
    FaceProperties faceProperties;

    public DetectFace(FaceProperties faceProperties) {
        this.faceProperties = faceProperties;
    }


    //查看图片像素和占用空间是否小于规定的大小
    public static Boolean checkImageElement(File file, int imageWidth, int imageHeight) throws IOException {
        Boolean result = false;
        if (!file.exists()) {
            return false;
        }
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(file));
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        double imageSize = file.length() / 1024.0 / 1024.0;
        System.out.println("width->>"+width+"height->>"+height+"imageSize->>"+imageSize+"MB");
        if (height > imageHeight || width > imageWidth) {
            return false;
        }
        if (imageSize > 5.0) {
            return false;
        }
        return true;
    }


    public void detectFace() {
        String secretId = faceProperties.getSecretId();
        String secretKey = faceProperties.getSecretKey();
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            IaiClient client = new IaiClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DetectFaceRequest req = new DetectFaceRequest();
            req.setUrl("https://general-store-1253847355.cos.ap-guangzhou.myqcloud.com/face-img/%E5%B8%BD%E5%AD%90%E5%92%8C%E5%8F%A3%E7%BD%A9.jpg?q-sign-algorithm=sha1&q-ak=AKIDcRuXO4PCNqGWH3e05YKfKquvZK4i934njvrwuqsUyspOK_EmZSrmMEUoMeV11ecR&q-sign-time=1664541506;1664545106&q-key-time=1664541506;1664545106&q-header-list=host&q-url-param-list=&q-signature=8d8679c93953b636d4d2187c68dcec4dfac53652&x-cos-security-token=jbl1cbUwkiKOsufd0gLQxCxVhapPUI0addbd073813a19aaf8251850dca6be887DGteMzFtpudomnC--178Ct2bWnMv71_JTW38P8RVmEKqYkBY-IdgpwyAuGlkUTqkzc6DmZWdwjpJTNhSIUf99KcsQRT8k8Mb9MpNJlC6itsz73fT-FuSlGz4eG5PinjQSFN0FJyrzQLUoM_2xodePRwgFk64wm62T4z_H7McHxE2yT2BzLU0NnTyPXSorpFm");
            //是否要返回人脸属性信息
            req.setNeedFaceAttributes(1L);
            //是否开启人脸图片质量检测
            req.setNeedQualityDetection(1L);
            // 返回的resp是一个DetectFaceResponse的实例，与请求对象对应
            DetectFaceResponse resp = client.DetectFace(req);
            // 输出json格式的字符串回包
            System.out.println(DetectFaceResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }
}
