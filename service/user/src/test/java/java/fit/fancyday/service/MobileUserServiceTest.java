package fit.fancyday.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import fit.fancyday.UserApplication;
import fit.fancyday.model.common.dtos.ResponseResult;
import fit.fancyday.model.user.dtos.LoginNumberDto;
import fit.fancyday.model.user.dtos.LoginPasswordDto;
import fit.fancyday.model.user.dtos.RegisterDto;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;
import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class MobileUserServiceTest extends TestCase {
    @Autowired
    private MobileUserService mobileUserService;

    @Test
    public void testLoginPassword() {
        LoginPasswordDto loginPasswordDto = new LoginPasswordDto();
        loginPasswordDto.setPhone("13511223453");
        loginPasswordDto.setPassword("root");
        ResponseResult responseResult = mobileUserService.loginPassword(loginPasswordDto);
        System.out.println(responseResult.toString());
    }


    @Test
    public void testImageCode() {
        ResponseResult responseResult = mobileUserService.imageCode();
        System.out.println(responseResult.toString());
    }

    @Test
    public void testLoginRegister() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setName("fancyday");
        registerDto.setPhone("19906908239");
        registerDto.setPassword("root");
        ResponseResult responseResult = mobileUserService.loginRegister(registerDto);
        System.out.println(responseResult);
    }

    @Test
    public void testModifyProfilePicture() throws IOException {
        BufferedInputStream inputStream = FileUtil.getInputStream("g:/aa.jpeg");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("aa.jpeg", inputStream);
//        ResponseResult responseResult = mobileUserService.modifyHeadImage(6, mockMultipartFile);
//        System.out.println(responseResult.toString());
    }

    @Test
    public void test1() {
        ResponseResult responseResult = mobileUserService.numberCode("19906908239");
        System.out.println(responseResult.toString());
    }

    @Test
    public void test2() {
        LoginNumberDto loginNumberDto = new LoginNumberDto();
        loginNumberDto.setPhone("19906908239");
        loginNumberDto.setNumberCode("2459");
        ResponseResult responseResult = mobileUserService.loginPhone(loginNumberDto);
        System.out.println(responseResult.toString());
    }

    @Test
    public void test3() {
        String now = DateUtil.now();
        System.out.println(now);
    }
}