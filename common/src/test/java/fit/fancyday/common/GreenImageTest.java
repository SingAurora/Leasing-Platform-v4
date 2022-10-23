package fit.fancyday.common;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import fit.fancyday.common.CommonApplication;
import fit.fancyday.common.tencent.GreenImage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = CommonApplication.class)
@RunWith(SpringRunner.class)
public class GreenImageTest {
    @Autowired
    private GreenImage greenImage;

    @Test
    public void test1() {
        String fileURL = "http://fancyday.fit:9000/leasingplatform/2022/10/20/052101326詹中智.jpg";
        String scan = greenImage.greenImageURL(fileURL);
        JSONObject obj = JSONUtil.parseObj(scan);
        System.out.println(obj.getStr("Suggestion"));
        System.out.println(obj.toStringPretty());
    }
}
