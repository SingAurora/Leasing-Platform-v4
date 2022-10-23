package fit.fancyday.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import fit.fancyday.UserApplication;
import fit.fancyday.model.common.dtos.ResponseResult;
import fit.fancyday.service.impl.MobileUserPrivate;
import fit.fancyday.model.user.pojos.MobileUserAuthentication;
import fit.fancyday.model.user.pojos.MobileUserDetail;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
class MobileUserPrivateTest {

    @Autowired
    private MobileUserPrivate mobileUserPrivate;

    @Autowired
    private MobileUserService mobileUserService;

    @Autowired
    private MobileUserDetailService mobileUserDetailService;

    @Autowired
    private MobileUserAuthenticationService mobileUserAuthenticationService;

    @Test
    public void test() {
        ResponseResult responseResult = mobileUserService.numberCode("19906908239");
        System.out.println(responseResult.toString());
    }

    @Test
    void modifyHeadImage() {
//        mobileUserPrivate.modifyHeadImage()
    }

    @Test
    void queryUserDetail() {
        ResponseResult responseResult = mobileUserPrivate.queryUserDetail(6);
        System.out.println(responseResult.toString());
    }

    @Test
    void modifyUserDetail() {
        MobileUserDetail mobileUserDetail = new MobileUserDetail();
        mobileUserDetail.setId(6);
        mobileUserDetail.setUserId(6);
        mobileUserDetail.setSex(2);
        mobileUserDetail.setIntroduce("你好，后天");
        mobileUserDetail.setLikeTag("原神");
        mobileUserDetail.setStatus(1);
        ResponseResult responseResult = mobileUserPrivate.modifyUserDetail(mobileUserDetail);
        System.out.println(responseResult.toString());
    }

    @Test
    void modifyUserPassword() {
        ResponseResult responseResult = mobileUserPrivate.modifyUserPassword(6, "1251", "admin");
        System.out.println(responseResult.toString());
    }

    @Test
    void modifyUserPhone() {
        ResponseResult responseResult = mobileUserPrivate.modifyUserPhone(6, "1251", "19906908240");
        System.out.println(responseResult.toString());
    }

    @Test
    void queryUserAuthentication() {
        ResponseResult responseResult = mobileUserPrivate.queryUserAuthentication(6);
        System.out.println(responseResult.toString());
    }

    @Test
    void sendAuthenticationPicture() throws IOException {
        BufferedInputStream inputStream = FileUtil.getInputStream("C:\\Users\\asus\\Pictures\\图库\\052101326詹中智.jpg");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("052101326詹中智.jpg", inputStream);
        ResponseResult responseResult = mobileUserPrivate.sendAuthenticationPicture(6, mockMultipartFile);
        System.out.println(responseResult.toString());
    }

    @Test
    void modifyAuthenticationInformation() {
        MobileUserAuthentication mobileUserAuthentication =
                mobileUserAuthenticationService.getOne(Wrappers.<MobileUserAuthentication>lambdaQuery().eq(MobileUserAuthentication::getUserId, 6));
        mobileUserAuthentication.setIdNumber("350301200106100712");
        mobileUserAuthentication.setName("胡桃");
        mobileUserAuthentication.setUpdatedTime(DateUtil.date());
        mobileUserAuthentication.setSubmitedTime(DateUtil.date(0));
        mobileUserPrivate.modifyAuthenticationInformation(mobileUserAuthentication);
    }

    @Test
    void queryFanNumber() {
        ResponseResult responseResult = mobileUserPrivate.queryFanNumber(3);
        System.out.println(responseResult.toString());
    }

    @Test
    void queryFollowNumber() {
        ResponseResult responseResult = mobileUserPrivate.queryFollowNumber(3);
        System.out.println(responseResult.toString());
    }

    @Test
    void test3() {

    }

}