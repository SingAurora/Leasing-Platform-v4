package fit.fancyday.common.tencent;


import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.*;
import fit.fancyday.common.propertise.SmsProperties;
import org.apache.commons.lang3.StringUtils;


/**
 * 腾讯云云短信服务
 *
 * @author asus
 * @date 2022/09/29
 */
public class SmsTemplate {

    private SmsProperties smsProperties;

    public SmsTemplate(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    /**
     * 发送短信
     *
     * @param mobile 电话号码
     * @param code   验证码
     */
    public void sendSms(String mobile, String code) {
        //获取基本参数
        String secretId = smsProperties.getSecretId();
        String secretKey = smsProperties.getSecretKey();
        String smsSdkAppId = smsProperties.getSmsSdkAppId();
        String signName = smsProperties.getSignName();
        String phone86 = "+86" + mobile;
        String waitTime = smsProperties.getWaitTime();
        if (StringUtils.isNotEmpty(mobile)) {
            try {
                //实例化一个认证对象Credential
                //密钥管理：https://console.cloud.tencent.com/cam/capi
                Credential cred = new Credential(secretId, secretKey);
                //实例化要请求产品的客户端对象SmsClient
                //第二个参数是地域信息，可以直接填写字符串ap-guangzhou
                SmsClient client = new SmsClient(cred, "ap-guangzhou");
                //实例化一个请求对象
                SendSmsRequest req = new SendSmsRequest();
                //那个APPID：https://console.cloud.tencent.com/smsv2/app-manage
                req.setSmsSdkAppId(smsSdkAppId);//是短信应用列表的那个AppId
                req.setSignName(signName);//签名内容
                req.setTemplateId("1538941");//签名ID
                String[] templateParamSet = {code, waitTime};//参数集合
                req.setTemplateParamSet(templateParamSet);//输入参数集合
                String[] phoneNumberSet = {phone86};//手机号集合
                req.setPhoneNumberSet(phoneNumberSet);//输入手机号集合
                //通过 client 对象调用 SendSms 方法发起请求
                SendSmsResponse res = client.SendSms(req);
                // 输出json格式的字符串回包
                System.out.println(SendSmsResponse.toJsonString(res));
            } catch (TencentCloudSDKException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拉取回执状态，服务是否接通
     */
    public void pullSmsSendStatus() {
        //获取基本参数
        String secretId = smsProperties.getSecretId();
        String secretKey = smsProperties.getSecretKey();
        String smsSdkAppId = smsProperties.getSmsSdkAppId();
        try {
            //实例化一个认证对象Credential
            //密钥管理：https://console.cloud.tencent.com/cam/capi
            Credential cred = new Credential(secretId, secretKey);
            //实例化要请求产品的客户端对象SmsClient
            //第二个参数是地域信息，可以直接填写字符串ap-guangzhou
            SmsClient client = new SmsClient(cred, "ap-guangzhou");
            /* 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
             * 你可以直接查询SDK源码确定接口有哪些属性可以设置
             * 属性可能是基本类型，也可能引用了另一个数据结构
             * 推荐使用IDE进行开发，可以方便地跳转查阅各个接口和数据结构的文档说明 */
            PullSmsSendStatusRequest req = new PullSmsSendStatusRequest();
            /* 短信应用ID: 短信SdkAppId在 [短信控制台] 添加应用后生成的实际SdkAppId，示例如1400006666 */
            req.setSmsSdkAppId(smsSdkAppId);
            // 设置拉取最大条数，最多100条
            Long limit = 5L;
            req.setLimit(limit);
            /* 通过 client 对象调用 PullSmsSendStatus 方法发起请求。注意请求方法名与请求对象是对应的
             * 返回的 res 是一个 PullSmsSendStatusResponse 类的实例，与请求对象对应 */
            PullSmsSendStatusResponse res = client.PullSmsSendStatus(req);
            // 输出json格式的字符串回包
            System.out.println(PullSmsSendStatusResponse.toJsonString(res));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计该APPID短信发送数据
     */
    public void sendStatusStatistics() {
        //获取基本参数
        String secretId = smsProperties.getSecretId();
        String secretKey = smsProperties.getSecretKey();
        String smsSdkAppId = smsProperties.getSmsSdkAppId();
        try {
            //实例化一个认证对象Credential
            //密钥管理：https://console.cloud.tencent.com/cam/capi
            Credential cred = new Credential(secretId, secretKey);
            //实例化要请求产品的客户端对象SmsClient
            //第二个参数是地域信息，可以直接填写字符串ap-guangzhou
            SmsClient client = new SmsClient(cred, "ap-guangzhou");

            /* 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
             * 你可以直接查询SDK源码确定接口有哪些属性可以设置
             * 属性可能是基本类型，也可能引用了另一个数据结构
             * 推荐使用IDE进行开发，可以方便地跳转查阅各个接口和数据结构的文档说明 */
            SendStatusStatisticsRequest req = new SendStatusStatisticsRequest();
            /* 短信应用ID: 短信SdkAppId在 [短信控制台] 添加应用后生成的实际SdkAppId，示例如1400006666 */
            req.setSmsSdkAppId(smsSdkAppId);
            // 设置拉取最大条数，最多100条
            Long limit = 5L;
            req.setLimit(limit);
            /* 偏移量 注：目前固定设置为0 */
            Long offset = 0L;
            req.setOffset(offset);
            /* 开始时间，yyyymmddhh 需要拉取的起始时间，精确到小时 */
            String beginTime = "2019071100";
            req.setBeginTime(beginTime);
            /* 结束时间，yyyymmddhh 需要拉取的截止时间，精确到小时
             * 注：EndTime 必须大于 beginTime */
            String endTime = "2022093000";
            req.setEndTime(endTime);
            /* 通过 client 对象调用 SendStatusStatistics 方法发起请求。注意请求方法名与请求对象是对应的
             * 返回的 res 是一个 SendStatusStatisticsResponse 类的实例，与请求对象对应 */
            SendStatusStatisticsResponse res = client.SendStatusStatistics(req);
            // 输出json格式的字符串回包
            System.out.println(SendStatusStatisticsResponse.toJsonString(res));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
    }
}
/* 当出现以下错误码时，快速解决方案参考
 * [FailedOperation.SignatureIncorrectOrUnapproved](https://cloud.tencent.com/document/product/382/9558#.E7.9F.AD.E4.BF.A1.E5.8F.91.E9.80.81.E6.8F.90.E7.A4.BA.EF.BC.9Afailedoperation.signatureincorrectorunapproved-.E5.A6.82.E4.BD.95.E5.A4.84.E7.90.86.EF.BC.9F)
 * [FailedOperation.TemplateIncorrectOrUnapproved](https://cloud.tencent.com/document/product/382/9558#.E7.9F.AD.E4.BF.A1.E5.8F.91.E9.80.81.E6.8F.90.E7.A4.BA.EF.BC.9Afailedoperation.templateincorrectorunapproved-.E5.A6.82.E4.BD.95.E5.A4.84.E7.90.86.EF.BC.9F)
 * [UnauthorizedOperation.SmsSdkAppIdVerifyFail](https://cloud.tencent.com/document/product/382/9558#.E7.9F.AD.E4.BF.A1.E5.8F.91.E9.80.81.E6.8F.90.E7.A4.BA.EF.BC.9Aunauthorizedoperation.smssdkappidverifyfail-.E5.A6.82.E4.BD.95.E5.A4.84.E7.90.86.EF.BC.9F)
 * [UnsupportedOperation.ContainDomesticAndInternationalPhoneNumber](https://cloud.tencent.com/document/product/382/9558#.E7.9F.AD.E4.BF.A1.E5.8F.91.E9.80.81.E6.8F.90.E7.A4.BA.EF.BC.9Aunsupportedoperation.containdomesticandinternationalphonenumber-.E5.A6.82.E4.BD.95.E5.A4.84.E7.90.86.EF.BC.9F)
 * 更多错误，可咨询[腾讯云助手](https://tccc.qcloud.com/web/im/index.html#/chat?webAppId=8fa15978f85cb41f7e2ea36920cb3ae1&title=Sms)
 */