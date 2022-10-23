package fit.fancyday.controller;

import fit.fancyday.service.impl.MobileUserPrivate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/mobile/private")
public class MobileUserPrivateController {
    @Autowired
    private MobileUserPrivate mobileUserPrivate;

    /**
     * modify head picture
     *
     * @param id id
     * @return {@code ResponseResult}
     */
    @PutMapping("/uploadHeadImage")
    public ResponseResult modifyHeadImage(@RequestParam Integer id, MultipartFile multipartFile) {
        return mobileUserPrivate.modifyHeadImage(id, multipartFile);
    }

    /**
     * query user's detailed information
     *
     * @param id id
     * @return {@code ResponseResult}
     */
    @GetMapping("/queryDetail")
    public ResponseResult queryUserDetail(@RequestParam Integer id) {
        return mobileUserPrivate.queryUserDetail(id);
    }

    /**
     * modify user's detailed information
     *
     * @param mobileUserDetail mobileUserDetail
     * @return {@code ResponseResult}
     */
    @PostMapping("/modifyDetail")
    public ResponseResult modifyUserDetail(@RequestBody MobileUserDetail mobileUserDetail) {
        return mobileUserPrivate.modifyUserDetail(mobileUserDetail);
    }

    /**
     * modify user's password
     *
     * @param id         id
     * @param numberCode 数字代码
     * @param password   密码
     * @return {@code ResponseResult}
     */
    @GetMapping("/modifyPassword")
    public ResponseResult modifyUserPassword(@RequestParam Integer id, String numberCode, String password) {
        return mobileUserPrivate.modifyUserPassword(id, numberCode, password);
    }

    /**
     * modify user's phone
     *
     * @param id         id
     * @param numberCode 数字代码
     * @param phone      电话
     * @return {@code ResponseResult}
     */
    @GetMapping("/modifyPhone")
    public ResponseResult modifyUserPhone(@RequestParam Integer id, String numberCode, String phone) {
        return mobileUserPrivate.modifyUserPhone(id, numberCode, phone);
    }

    /**
     * Query the real-name authentication data of a user
     *
     * @param id id
     * @return {@code ResponseResult}
     */
    @GetMapping("/queryAuthentication")
    public ResponseResult queryUserAuthentication(@RequestParam Integer id) {
        return mobileUserPrivate.queryUserAuthentication(id);
    }

    /**
     * modify authenticated head picture
     *
     * @param id id
     * @return {@code ResponseResult}
     */
    @GetMapping("/authenticationPicture")
    public ResponseResult sendAuthenticationPicture(@RequestParam Integer id, MultipartFile multipartFile) {
        return mobileUserPrivate.sendAuthenticationPicture(id, multipartFile);
    }

    /**
     * modify user's authenticated information
     *
     * @param mobileUserAuthentication 移动用户身份验证
     * @return {@code ResponseResult}
     */
    @PutMapping("/modifyAuthentication")
    public ResponseResult modifyAuthenticationInformation(@RequestBody MobileUserAuthentication mobileUserAuthentication) {
        return mobileUserPrivate.modifyAuthenticationInformation(mobileUserAuthentication);
    }

    /**
     * Query the total number of fans
     *
     * @param id id
     * @return {@code ResponseResult}
     */
    @GetMapping("/queryFanNumber")
    public ResponseResult queryFanNumber(@RequestParam Integer id) {
        return mobileUserPrivate.queryFanNumber(id);
    }

    /**
     * Query the total number of follows
     *
     * @param id id
     * @return {@code ResponseResult}
     */
    @GetMapping("/queryFollowNumber")
    public ResponseResult queryFollowNumber(@RequestParam Integer id) {
        return mobileUserPrivate.queryFollowNumber(id);
    }
}
