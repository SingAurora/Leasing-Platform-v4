package fit.fancyday.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.math.Calculator;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fit.fancyday.common.MyJWTUtil;
import fit.fancyday.model.common.dtos.ResponseResult;
import fit.fancyday.model.common.enums.HttpCodeEnum;
import fit.fancyday.mapper.MobileUserMapper;
import fit.fancyday.service.MobileUserAuthenticationService;
import fit.fancyday.service.MobileUserDetailService;
import fit.fancyday.service.MobileUserService;
import fit.fancyday.common.tencent.SmsTemplate;
import fit.fancyday.model.user.dtos.LoginNumberDto;
import fit.fancyday.model.user.dtos.LoginPasswordDto;
import fit.fancyday.model.user.dtos.RegisterDto;
import fit.fancyday.model.user.pojos.MobileUser;
import fit.fancyday.model.user.pojos.MobileUserAuthentication;
import fit.fancyday.model.user.pojos.MobileUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class MobileUserServiceImpl extends ServiceImpl<MobileUserMapper, MobileUser> implements MobileUserService {
    /** cloud SMS service */
    @Autowired
    private SmsTemplate smsTemplate;

    /** redis cache */
    @Autowired
    private RedisTemplate redisTemplate;

    /** user details */
    @Autowired
    private MobileUserDetailService mobileUserDetailService;

    /** user's authentication */
    @Autowired
    private MobileUserAuthenticationService mobileUserAuthenticationService;

    /**
     * Log in using your account and password
     *
     * @param dto dto
     * @return {@code ResponseResult}
     */
    @Override
    public ResponseResult loginPassword(LoginPasswordDto dto) {
        // 1
        // Ensure that the requested data is not empty overall
        if (dto == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_ALL_NOT_EXIST);
        }

        // 2.1.
        // Visitors can log in directly
        if (StrUtil.isBlank(dto.getPhone()) && StrUtil.isBlank(dto.getPassword())) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("token", MyJWTUtil.getToken(0L));
            return ResponseResult.okResult(hashMap);
        }

        // 2.2
        // Make sure the id is valid
        MobileUser mobileUser = getOne(Wrappers.<MobileUser>lambdaQuery().eq(MobileUser::getPhone, dto.getPhone()));
        if (mobileUser == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_ALL_NOT_EXIST, "没有该id的用户");
        }

        // 3.2
        // Make sure the password is correct
        String salt = mobileUser.getSalt();
        String password = dto.getPassword();
        String md5 = SecureUtil.md5(password + salt);
        if (!md5.equals(mobileUser.getPassword())) {
            return ResponseResult.errorResult(HttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        // 4.2.
        // Return data
        HashMap<String, Object> hashMap = new HashMap<>();
        String token = MyJWTUtil.getToken(mobileUser.getId().longValue());
        hashMap.put("token", token);
        mobileUser.setSalt("");
        mobileUser.setPassword("");
        hashMap.put("mobileUser", mobileUser);
        return ResponseResult.okResult(hashMap);
    }

    /**
     * Log in using your phone with a verification code
     *
     * @param loginNumberDto dto
     * @return {@code ResponseResult}
     */
    @Override
    public ResponseResult loginPhone(LoginNumberDto loginNumberDto) {
        // 1
        // Ensure that the requested data is not empty overall
        if (loginNumberDto == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_PART_NOT_EXIST, "请求数据全为空");
        }

        // 2
        // Make sure your phone number is legitimate
        String phone = loginNumberDto.getPhone();
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0-8])|(18[0-9])|(19[1、5、8、9]))\\d{8}$";
        if (!ReUtil.isMatch(regex, phone)) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_PART_ILLEGAL, "手机号不合法");
        }

        // 3
        // The verification code cannot be empty
        String code = (String) redisTemplate.opsForValue().get(loginNumberDto.getPhone());
        if (StrUtil.isBlank(code)) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_ALL_NOT_EXIST, "请先获取验证码");
        }

        // 4
        // The phone must be registered
        MobileUser mobileUser = getOne(Wrappers.<MobileUser>lambdaQuery().eq(MobileUser::getPhone, phone));
        if (mobileUser == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_ALL_NOT_EXIST, "用户未注册");
        }

        // 5
        // The verification code must match
        if (!code.equals(loginNumberDto.getNumberCode())) {
            return ResponseResult.errorResult(HttpCodeEnum.NUMBER_CODE_NOT_EQUAL);
        }
        redisTemplate.delete(phone);

        // 6
        // Return data
        String token = MyJWTUtil.getToken(mobileUser.getId().longValue());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        mobileUser.setSalt("");
        mobileUser.setPassword("");
        hashMap.put("user", mobileUser);
        return ResponseResult.okResult(hashMap);
    }

    /**
     * Register an account
     *
     * @param registerDto dto
     * @return {@code ResponseResult}
     */
    @Override
    public ResponseResult loginRegister(RegisterDto registerDto) {
        // 1
        // Ensure that the requested data is not empty overall
        if (registerDto == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_PART_NOT_EXIST, "请求数据全为空");
        }

        // 2
        // Ensure that significant parts of the requested data are not empty
        if (registerDto.getName() == null || registerDto.getPhone() == null || registerDto.getPassword() == null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_PART_NOT_EXIST);
        }

        // 3
        // Make sure your phone number is legitimate
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0-8])|(18[0-9])|(19[1、5、8、9]))\\d{8}$";
        if (!ReUtil.isMatch(regex, registerDto.getPhone())) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_PART_ILLEGAL);
        }

        // 4
        // The phone number must not be registered
        MobileUser one = getOne(Wrappers.<MobileUser>lambdaQuery().eq(MobileUser::getPhone, registerDto.getPhone()));
        if (one != null) {
            return ResponseResult.errorResult(HttpCodeEnum.DATA_EXIST, "该手机号已经被注册过");
        }

        // 5
        // Store the data to a database
        MobileUser two = new MobileUser();
        BeanUtil.copyProperties(registerDto, two);
        RandomGenerator randomGenerator = new RandomGenerator("abcdefghijklmnopqrstqvwxyz", 3);
        String salt = randomGenerator.generate();
        String md5 = SecureUtil.md5(registerDto.getPassword() + salt);
        two.setPassword(md5);
        two.setSalt(salt);
        two.setCreatedTime(DateUtil.date());
        boolean save = save(two);
        if (!save) {
            return ResponseResult.errorResult(HttpCodeEnum.DATABASE_ERROR);
        }

        // 6
        // Retrieve the data with id
        MobileUser three = getOne(Wrappers.<MobileUser>lambdaQuery().eq(MobileUser::getPhone, registerDto.getPhone()));
        three.setSalt("");
        three.setPassword("");

        // 7
        // Store the data to mobile_user_detail
        MobileUserDetail mobileUserDetail = new MobileUserDetail();
        mobileUserDetail.setUserId(three.getId());
        boolean save1 = mobileUserDetailService.save(mobileUserDetail);
        if (!save1) {
            return ResponseResult.errorResult(HttpCodeEnum.DATABASE_ERROR);
        }

        // 8
        // Store the data to mobile_user_authentication
        MobileUserAuthentication mobileUserAuthentication = new MobileUserAuthentication();
        mobileUserAuthentication.setUserId(three.getId());
        mobileUserAuthentication.setCreatedTime(DateUtil.date());
        mobileUserAuthentication.setUpdatedTime(DateUtil.date());
        boolean save2 = mobileUserAuthenticationService.save(mobileUserAuthentication);
        if (!save2) {
            return ResponseResult.errorResult(HttpCodeEnum.DATABASE_ERROR);
        }

        // 9
        // Return data
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("mobileUser", three);
        return ResponseResult.okResult(hashMap);
    }

    /**
     * Get a picture of an arithmetic expression：1+1=
     *
     * @return {@code ResponseResult}
     */
    @Override
    public ResponseResult imageCode() {
        // 1
        // Set up the captcha generator
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        lineCaptcha.setGenerator(new MathGenerator(1));
        lineCaptcha.createCode();

        // 2
        // Get the image and captcha
        String imageBase64 = lineCaptcha.getImageBase64();
        String code = lineCaptcha.getCode();
        Integer conversion = (int) Calculator.conversion(code);

        // 3
        // Return data
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sum", conversion.toString());
        hashMap.put("image", imageBase64);
        return ResponseResult.okResult(hashMap);
    }


    /**
     * Obtain the mobile phone verification code
     *
     * @param phone 手机号前
     * @return {@code ResponseResult}
     */
    @Override
    public ResponseResult numberCode(String phone) {
        // 1
        // Generate a verification code
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
        String generate = randomGenerator.generate();

        // 2
        // Store data into redis cache
        smsTemplate.sendSms(phone, generate);
        redisTemplate.opsForValue().set(phone, generate, 10, TimeUnit.MINUTES);
        return ResponseResult.okResult(200, "验证码发送成功");
    }
}
