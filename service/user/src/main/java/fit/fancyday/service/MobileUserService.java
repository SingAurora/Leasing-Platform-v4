package fit.fancyday.service;


import com.baomidou.mybatisplus.extension.service.IService;
import fit.fancyday.model.common.dtos.ResponseResult;
import fit.fancyday.model.user.dtos.LoginNumberDto;
import fit.fancyday.model.user.dtos.LoginPasswordDto;
import fit.fancyday.model.user.dtos.RegisterDto;
import fit.fancyday.model.user.pojos.MobileUser;

/**
 * 移动用户服务
 *
 * @author asus
 * @date 2022/10/11
 */
public interface MobileUserService extends IService<MobileUser> {
    /**
     * Log in using your account and password
     *
     * @param dto dto
     * @return {@code ResponseResult}
     */
    public ResponseResult loginPassword(LoginPasswordDto dto);

    /**
     * Log in using your phone with a verification code
     *
     * @param loginNumberDto dto
     * @return {@code ResponseResult}
     */
    public ResponseResult loginPhone(LoginNumberDto loginNumberDto);

    /**
     * Get a picture of an arithmetic expression：1+1=
     *
     * @return {@code ResponseResult}
     */
    public ResponseResult imageCode();

    /**
     * Register an account
     *
     * @param dto dto
     * @return {@code ResponseResult}
     */
    public ResponseResult loginRegister(RegisterDto dto);




    /**
     * Obtain the mobile phone verification code
     *
     * @param phone 手机号前
     * @return {@code ResponseResult}
     */
    ResponseResult numberCode(String phone);
}
