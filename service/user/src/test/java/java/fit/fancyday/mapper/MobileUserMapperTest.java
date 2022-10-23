package fit.fancyday.mapper;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.lang.Console;
import cn.hutool.core.math.Calculator;
import cn.hutool.core.util.ReUtil;
import fit.fancyday.UserApplication;
import fit.fancyday.model.user.dtos.RegisterDto;
import fit.fancyday.model.user.pojos.MobileUser;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;


@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringRunner.class)
public class MobileUserMapperTest extends TestCase {


//    @Test
//    public void test1() {
//        LambdaQueryWrapper<MobileUser> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(MobileUser::getId, 1L);
//        MobileUser mobileUser = mobileUserMapper.selectOne(lambdaQuery(MobileUser.class).eq(MobileUser::getId, 1));
//        System.out.println(mobileUser.toString());
//    }

    @Test
    public void test2() {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100, 6, 20);
        lineCaptcha.write("G:\\黑马\\line.png");
        Console.log(lineCaptcha.getCode());
        boolean verify = lineCaptcha.verify("1234");
        System.out.println("verify = " + verify);

        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(200, 100, 6, 20);
        circleCaptcha.write("G:\\黑马\\circle.png");
        Console.log(circleCaptcha.getCode());
        boolean verify1 = circleCaptcha.verify(circleCaptcha.getCode());
        System.out.println("verify1 = " + verify1);

        RandomGenerator randomGenerator = new RandomGenerator("123abc", 4);
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 0);
        shearCaptcha.setGenerator(randomGenerator);
        shearCaptcha.createCode();
        shearCaptcha.write("g:/黑马/shear.png");
        String imageBase64 = shearCaptcha.getImageBase64();
        System.out.println(imageBase64);
        MathGenerator mathGenerator = new MathGenerator(4);
        String generate = randomGenerator.generate();
        System.out.println("generate = " + generate);
        shearCaptcha.setGenerator(mathGenerator);
        shearCaptcha.createCode();

        System.out.println("shearCaptcha.getCode()--" + shearCaptcha.getCode());
        int conversion = (int) Calculator.conversion(shearCaptcha.getCode());
        System.out.println("conversion = " + conversion);
        shearCaptcha.write("g:/黑马/shear1.png");

    }

    @Test
    public void test3() {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0-8])|(18[0-9])|(19[1、5、8、9]))\\d{8}$";
        if (ReUtil.isMatch(regex, "13860920465")) {
            System.out.println("true");
        }
        if (ReUtil.isMatch(regex, "18890822291")) {
            System.out.println("true");
        }
        if (ReUtil.isMatch(regex, "13107989138")) {
            System.out.println("true");
        }
        if (ReUtil.isMatch(regex, "18005045868")) {
            System.out.println("true");
        }
    }

    @Test
    public void test4() {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        lineCaptcha.setGenerator(new MathGenerator(2));
        String code = lineCaptcha.getCode();
        System.out.println("code = " + code);
        Integer conversion = (int) Calculator.conversion(code);
//        lineCaptcha.write("g:/a.png");
        System.out.println(lineCaptcha.getImageBase64());
    }

    @Test
    public void test5() {
        String base64 = "iVBORw0KGgoAAAANSUhEUgAAAMgAAABkCAYAAADDhn8LAAAZDElEQVR4Xu2dCXRV1bnHu" +
                "/TZvtW3VmttrVVrkTrV6cGrEyCigoUFqFDUCioqoIKAA4KiDCqIAloRBBGcQEQGGQSSEAhTwAABAgoyE0KAEMKYQAK58373O" +
                "/Q72efbZ57uvcn9rfVfknPPcMHvl332Ofvs8wuWxjSdH6umi9LUcn5BFyQTX9TfLCWZSJQk" +
                "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";
        BufferedImage bufferedImage = ImgUtil.toImage(base64);
        ImgUtil.write(bufferedImage, new File("g:/a.png"));
    }

    @Test
    public void test6() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setName("fancyday");
        registerDto.setPhone("19906908239");
        registerDto.setPassword("fdsjfdfbdu");
        MobileUser mobileUser = new MobileUser();
//        BeanUtils.copyProperties(registerDto,mobileUser);
        CopyOptions copyOptions = CopyOptions.create().ignoreNullValue();
        BeanUtil.copyProperties(registerDto,mobileUser,"");
        System.out.println(mobileUser);
    }

    @Test
    public void test7() {
        BufferedInputStream bufferedInputStream = FileUtil.getInputStream("g:/a1.txt");
        BufferedOutputStream bufferedOutputStream = FileUtil.getOutputStream("g:/a2.txt");
        long copy = IoUtil.copy(bufferedInputStream, bufferedOutputStream, IoUtil.DEFAULT_BUFFER_SIZE);
        System.out.println(copy);
        for (File file : FileUtil.ls("g:/")) {
            String name = file.getName();
            System.out.println("name = " + name);
        }
        FileUtil.touch("g:/aa/bb/cc.txt");
        FileUtil.touch("g:/bb/bb/cc.txt");
        FileUtil.del("g:/bb");
    }

    @Test
    public void test8() {
        File file = FileUtil.file("g:/a1.txt");
        String type = FileTypeUtil.getType(file);
        System.out.println(type);
        File file1 = FileUtil.file("g:/aa.jpeg");
        String type1 = FileTypeUtil.getType(file1);
        System.out.println(type1);
    }

    @Test
    public void test9() {
        FileReader fileReader = new FileReader("g:/a2.txt", "UTF-8");
        String readString = fileReader.readString();
        Console.log(readString);
        for (String readLine : fileReader.readLines()) {
            System.out.println(readLine);
        }
        byte[] bytes = fileReader.readBytes();
        System.out.println(bytes);
    }

    public static void main(String[] args) {
        File file = FileUtil.file("G:\\aa\\bb");
        WatchMonitor watchMonitor = WatchMonitor.create(file, WatchMonitor.EVENTS_ALL);
        watchMonitor.setWatcher(new Watcher() {
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                Object context = event.context();
                Console.log("创建：{}-> {}", currentPath, context);
            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                Object context = event.context();
                Console.log("修改：{}-> {}", currentPath, context);
            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
                Object context = event.context();
                Console.log("删除：{}-> {}", currentPath, context);
            }

            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {
                Object context = event.context();
                Console.log("Overflow：{}-> {}", currentPath, context);
            }
        });
        watchMonitor.setMaxDepth(3);
        watchMonitor.start();
    }

    @Test
    public void test10() {
        FileWriter fileWriter = new FileWriter("g:/a2.txt");
        fileWriter.write("test\n",true);
        fileWriter.write("哈哈哈",true);
    }

    @Test
    public void test11() {
        FileAppender fileAppender = new FileAppender(new File("g:/a2.txt"), 2, true);
        fileAppender.append("123");
        fileAppender.append("xyz");
        fileAppender.flush();
        String toString = fileAppender.toString();
        System.out.println(toString);
    }

    @Test
    public void test12() {
        File file = FileUtil.file("g:/a2.txt");
        String name = FileNameUtil.getName(file);
        String prefix = FileNameUtil.getPrefix(file);
        String suffix = FileNameUtil.getSuffix(file);
        Console.log("{}--{}--{}",name,prefix,suffix);
    }
}