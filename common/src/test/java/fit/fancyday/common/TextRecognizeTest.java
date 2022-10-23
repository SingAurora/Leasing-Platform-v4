package fit.fancyday.common;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import fit.fancyday.common.tencent.TextRecognition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

@SpringBootTest(classes = CommonApplication.class)
@RunWith(SpringRunner.class)
public class TextRecognizeTest {
    @Autowired
    private TextRecognition textRecognition;

    @Test
    public void test1() {
        String path = "http://fancyday.fit:9000/leasingplatform/2022/10/15/aa.png";
        String result = textRecognition.oCR(path);

        JSONObject jsonObject = JSONUtil.parseObj(result);
        String words_result = jsonObject.getStr("words_result");
        JSONArray objects = JSONUtil.parseArray(words_result);
        StringBuilder builder = new StringBuilder("");
        List<HashMap> maps = JSONUtil.toList(words_result, HashMap.class);
        for (HashMap map : maps) {
            builder.append(map.get("words"));
        }
        System.out.println(builder);
//        String pretty = jsonObject.toStringPretty();
//        System.out.println(pretty);
    }
}
