package fit.fancyday.common.tencent;

import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import fit.fancyday.common.propertise.OcrProperties;
import org.springframework.beans.factory.annotation.Autowired;

// 通用文字识别-标准版 示例代码
public class TextRecognition {
    @Autowired
    private OcrProperties ocrProperties;

    public TextRecognition(OcrProperties ocrProperties) {
        this.ocrProperties = ocrProperties;
    }

    public String oCR(String path) {
        // 往哪里发送请求
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic");
        // 设置header参数
        request.addHeaderParameter("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        // 设置query参数
        request.addQueryParameter("access_token", ocrProperties.getAccessToken());
        // 设置jsonBody参数
        String jsonBody = "url=" + path;
        request.setJsonBody(jsonBody);
        ApiExplorerClient client = new ApiExplorerClient();
        try {
            ApiExplorerResponse response = client.sendRequest(request);
            // 返回结果格式为Json字符串
            return response.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}