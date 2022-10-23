package fit.fancyday.common;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import fit.fancyday.common.tencent.GreenText;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = CommonApplication.class)
@RunWith(SpringRunner.class)
public class GreenTextTest {
    @Autowired
    private GreenText greenText;

    @Test
    public void test1() {
        String a = "Your mom got fucked";
        String encode = Base64.encode(a);
        String result = greenText.greenTestScan(encode);
        JSONObject obj = JSONUtil.parseObj(result);
        System.out.println(obj.toStringPretty());
    }
}
