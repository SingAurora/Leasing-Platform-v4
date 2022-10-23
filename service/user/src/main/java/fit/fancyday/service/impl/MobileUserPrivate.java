package fit.fancyday.service.impl;

import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import fit.fancyday.common.tencent.GreenImage;
import fit.fancyday.model.common.dtos.ResponseResult;
import fit.fancyday.model.common.enums.HttpCodeEnum;
import fit.fancyday.model.user.pojos.*;
import fit.fancyday.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@Component
public class MobileUserPrivate {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private GreenImage greenImage;
    @Autowired
    private MobileUserService mobileUserService;
    @Autowired
    private MobileUserDetailService mobileUserDetailService;

    /** Fans of users */
    @Autowired
    private MobileUserFanService mobileUserFanService;

    /** People the user follows */
    @Autowired
    private MobileUserFollowService mobileUserFollowService;

    @Autowired
    private MobileUserAuthenticationService mobileUserAuthenticationService;

    /**
     * modify head image
     *
     * @param id id
     * @return {@code ResponseResult}
     */
    public ResponseResult modifyHeadImage(Integer id, MultipartFile multipartFile) {
        // 1
        // Make sure the id is valid
        MobileUser mobileUser = mobileUserService.getOne(Wrappers.<MobileUser>lambdaQuery().eq(MobileUser::getId, id));
        if (mobileUser == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_ALL_NOT_EXIST, "该id下的用户不存在");
        }

        // 2
        // Get the file name
        String name = multipartFile.getName();
        String extName = FileNameUtil.extName(name);
        String filename = IdUtil.simpleUUID() + "." + extName;

        // 3
        // Upload image and save data into database
        String imageUrl;
        try {
            imageUrl = fileStorageService.uploadImgFile("", filename, multipartFile.getInputStream());
            mobileUser.setImage(imageUrl);
            mobileUserService.updateById(mobileUser);
        } catch (IOException e) {
            return ResponseResult.errorResult(HttpCodeEnum.DATABASE_ERROR);
        }

        // 4
        // Return data
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("imageUrl", imageUrl);
        return ResponseResult.okResult(hashMap);
    }

    /**
     * query user's detailed information
     *
     * @param id id
     * @return {@code ResponseResult}
     */
    public ResponseResult queryUserDetail(Integer id) {
        // 1
        // Make sure the id is valid
        MobileUserDetail mobileUserDetail =
                mobileUserDetailService.getOne(Wrappers.<MobileUserDetail>lambdaQuery().eq(MobileUserDetail::getUserId,
                        id));
        if (mobileUserDetail == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_ALL_NOT_EXIST, "该id在数据库不存在");
        }

        // 2
        // Return data
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("mobileUserDetail", mobileUserDetail);
        return ResponseResult.okResult(hashMap);
    }

    /**
     * modify user's detailed information
     *
     * @param mobileUserDetail mobileUserDetail
     * @return {@code ResponseResult}
     */
    public ResponseResult modifyUserDetail(MobileUserDetail mobileUserDetail) {
        // 1
        // Ensure that the requested data is not empty overall
        if (mobileUserDetail == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_ALL_NOT_EXIST);
        }

        // 2
        // Ensure that significant parts of the requested data are not empty
        if (mobileUserDetail.getId() == 0 && mobileUserDetail.getUserId() == 0) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_PART_NOT_EXIST);
        }

        // 3
        // Return data
        boolean update = mobileUserDetailService.updateById(mobileUserDetail);
        if (update) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(HttpCodeEnum.DATABASE_ERROR);
        }
    }

    /**
     * modify user's password
     *
     * @param id         id
     * @param numberCode 数字代码
     * @param password   密码
     * @return {@code ResponseResult}
     */
    public ResponseResult modifyUserPassword(Integer id, String numberCode, String password) {
        // 1
        // Make sure the id is valid
        MobileUser mobileUser = mobileUserService.getOne(Wrappers.<MobileUser>lambdaQuery().eq(MobileUser::getId, id));
        if (mobileUser == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_ALL_NOT_EXIST, "该id在数据库中不存在");
        }

        // 2
        // Make sure the numberCode is valid
        String phone = mobileUser.getPhone();
        String numberCodeBack = (String) redisTemplate.opsForValue().get(phone);
        if (!numberCodeBack.equals(numberCode)) {
            return ResponseResult.errorResult(HttpCodeEnum.NUMBER_CODE_NOT_EQUAL);
        }

        // 3
        // Store data into database
        RandomGenerator randomGenerator = new RandomGenerator("abcdefghijklmnopqrstqvwxyz", 3);
        String salt = randomGenerator.generate();
        String md5 = SecureUtil.md5(password + salt);
        mobileUser.setPassword(md5);
        mobileUser.setSalt(salt);
        boolean update = mobileUserService.updateById(mobileUser);
        redisTemplate.delete(phone);

        // 4
        // Return data
        if (update) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(HttpCodeEnum.DATABASE_ERROR);
        }
    }

    /**
     * modify user's phone
     *
     * @param id         id
     * @param numberCode 数字代码
     * @param phone      电话
     * @return {@code ResponseResult}
     */
    public ResponseResult modifyUserPhone(Integer id, String numberCode, String phone) {
        // 1
        // Make sure the id is valid
        MobileUser mobileUser = mobileUserService.getOne(Wrappers.<MobileUser>lambdaQuery().eq(MobileUser::getId, id));
        if (mobileUser == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_ALL_NOT_EXIST, "该id在数据库中不存在");
        }

        // 2
        // Make sure your phone number is legitimate
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0-8])|(18[0-9])|(19[1、5、8、9]))\\d{8}$";
        if (!ReUtil.isMatch(regex, phone)) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_PART_ILLEGAL);
        }

        // 3
        // Make sure the numberCode is valid
        String oldPhone = mobileUser.getPhone();
        String numberCodeBack = (String) redisTemplate.opsForValue().get(oldPhone);
        if (!numberCodeBack.equals(numberCode)) {
            return ResponseResult.errorResult(HttpCodeEnum.NUMBER_CODE_NOT_EQUAL);
        }

        // 4
        // Store data into database
        mobileUser.setPhone(phone);
        boolean update = mobileUserService.updateById(mobileUser);
        redisTemplate.delete(oldPhone);

        // 5
        // Return data
        if (update) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(HttpCodeEnum.DATABASE_ERROR);
        }
    }

    /**
     * Query the real-name authentication data of a user
     *
     * @param id id
     * @return {@code ResponseResult}
     */
    public ResponseResult queryUserAuthentication(Integer id) {
        // 1
        // Make sure the id is valid
        MobileUserAuthentication mobileUserAuthentication =
                mobileUserAuthenticationService.getOne(Wrappers.<MobileUserAuthentication>lambdaQuery().eq(MobileUserAuthentication::getUserId, id));
        if (mobileUserAuthentication == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_ALL_NOT_EXIST, "该id在数据库中不存在");
        }

        // 2
        // Return data
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("mobileUserAuthentication", mobileUserAuthentication);
        return ResponseResult.okResult(hashMap);
    }

    /**
     * modify authenticated head picture
     *
     * @param id id
     * @return {@code ResponseResult}
     */
    public ResponseResult sendAuthenticationPicture(@RequestParam Integer id, MultipartFile multipartFile) {
        // 1
        // Ensure that significant parts of the requested data are not empty
        MobileUserAuthentication mobileUserAuthentication =
                mobileUserAuthenticationService.getOne(Wrappers.<MobileUserAuthentication>lambdaQuery().eq(MobileUserAuthentication::getUserId, id));
        if (mobileUserAuthentication == null || multipartFile == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_PART_NOT_EXIST);
        }

        // 2
        // Get the file name
        String name = multipartFile.getName();
        String extName = FileNameUtil.extName(name);
        String filename = IdUtil.simpleUUID() + "." + extName;

        // 3
        // Store data into database
        try {
            String imgFile = fileStorageService.uploadImgFile("", filename, multipartFile.getInputStream());
            mobileUserAuthentication.setStatus(1);
            mobileUserAuthentication.setFontImage(imgFile);
            mobileUserAuthenticationService.updateById(mobileUserAuthentication);

            // 4
            // censorship
            greenImageImpl(id, imgFile);
        } catch (IOException e) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_UPLOAD_ERROR, "数据库出错或上传失败");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ResponseResult.okResult(200, "上传成功");
    }

    /**
     * modify user's authenticated information
     *
     * @param mobileUserAuthentication 移动用户身份验证
     * @return {@code ResponseResult}
     */
    public ResponseResult modifyAuthenticationInformation(MobileUserAuthentication mobileUserAuthentication) {
        // 1
        // Ensure that the requested data is not empty overall
        if (mobileUserAuthentication == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_ALL_NOT_EXIST);
        }

        // 2
        // Ensure that significant parts of the requested data are not empty
        if (mobileUserAuthentication.getId() == null ||
                mobileUserAuthentication.getUserId() == null ||
                mobileUserAuthentication.getFontImage() == null ||
                mobileUserAuthentication.getName() == null ||
                mobileUserAuthentication.getIdNumber() == null
        ) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_PART_NOT_EXIST);
        }

        // 3
        // Store data into database
        mobileUserAuthentication.setUpdatedTime(DateUtil.date());
        mobileUserAuthentication.setStatus(1);
        boolean update = mobileUserAuthenticationService.updateById(mobileUserAuthentication);

        // 4
        // Return data
        if (update) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(HttpCodeEnum.DATABASE_ERROR);
        }
    }

    /**
     * Query the total number of fans
     *
     * @param id id
     * @return {@code ResponseResult}
     */
    public ResponseResult queryFanNumber(Integer id) {
        long count = mobileUserFanService.count(Wrappers.<MobileUserFan>lambdaQuery().eq(MobileUserFan::getUserId, id));
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fanCount", count);
        return ResponseResult.okResult(hashMap);
    }

    /**
     * Query the total number of follows
     *
     * @param id id
     * @return {@code ResponseResult}
     */
    public ResponseResult queryFollowNumber(Integer id) {
        long count =
                mobileUserFollowService.count(Wrappers.<MobileUserFollow>lambdaQuery().eq(MobileUserFollow::getUserId
                        , id));
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("followCount", count);
        return ResponseResult.okResult(hashMap);
    }

    /**
     * The implementation method of content review
     *
     * @param id      id
     * @param fileURL 文件url
     * @throws InterruptedException 中断异常
     */
    @Async
    public void greenImageImpl(Integer id, String fileURL) throws InterruptedException {
        // 1
        // Obtain data
        Thread.sleep(10000);
        String scan = greenImage.greenImageURL(fileURL);
        JSONObject obj = JSONUtil.parseObj(scan);
        MobileUserAuthentication mobileUserAuthentication =
                mobileUserAuthenticationService.getOne(Wrappers.<MobileUserAuthentication>lambdaQuery().eq(MobileUserAuthentication::getUserId, id));

        // 2
        // Return data
        if (obj.getStr("Suggestion").equals("Pass")) {
            mobileUserAuthentication.setStatus(4);
            mobileUserAuthentication.setSubmitedTime(DateUtil.date());
            mobileUserAuthenticationService.updateById(mobileUserAuthentication);
        } else {
            mobileUserAuthentication.setStatus(2);
            mobileUserAuthentication.setReason("图片不符合要求");
            mobileUserAuthenticationService.updateById(mobileUserAuthentication);
        }
    }
}
