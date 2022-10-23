package fit.fancyday.controller;

import fit.fancyday.model.common.dtos.ResponseResult;
import fit.fancyday.model.user.dtos.LoginNumberDto;
import fit.fancyday.model.user.dtos.LoginPasswordDto;
import fit.fancyday.model.user.dtos.RegisterDto;
import fit.fancyday.service.MobileUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Operations before login and login operation
 *
 * @author 喵酱不熬夜
 * @date 2022/10/14
 */
@RestController
@RequestMapping("/mobile/login")
public class MobileUserLoginController {
    @Autowired
    private MobileUserService mobileUserService;

    /**
     * Log in using your account and password
     *
     * @param dto dto
     * @return {@code ResponseResult}
     */
    @PostMapping("/password")
    public ResponseResult loginPassword(@RequestBody LoginPasswordDto dto) {
        return mobileUserService.loginPassword(dto);
    }

    /**
     * Log in using your phone with a verification code
     *
     * @param loginNumberDto dto
     * @return {@code ResponseResult}
     */
    @PostMapping("/phone")
    public ResponseResult loginPhone(@RequestBody LoginNumberDto loginNumberDto) {
        return mobileUserService.loginPhone(loginNumberDto);
    }

    /**
     * Register an account
     *
     * @param registerDto dto
     * @return {@code ResponseResult}
     */
    @PostMapping("/register")
    public ResponseResult loginRegister(@RequestBody RegisterDto registerDto) {
        return mobileUserService.loginRegister(registerDto);
    }

    /**
     * Get a picture of an arithmetic expression：1+1=
     *
     * @return {@code ResponseResult}
     */
    @GetMapping("/imageCode")
    public ResponseResult loginImageCode() {
        return mobileUserService.imageCode();
    }

    /**
     * Obtain the mobile phone verification code
     *
     * @param phone 手机号前
     * @return {@code ResponseResult}
     */
    @GetMapping("/numberCode")
    public ResponseResult loginNumberCode(@RequestParam String phone) {
        return mobileUserService.numberCode(phone);
    }
}
