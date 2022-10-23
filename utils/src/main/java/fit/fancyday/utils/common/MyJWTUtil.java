package fit.fancyday.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;
import java.util.HashMap;
import java.util.UUID;

/**
 * personal tool kit
 *
 * @author 喵酱不熬夜
 * @date 2022/10/14
 */
public class MyJWTUtil {
    // TODO:there need refresh token
    /** expiration time : one week */
    private static final int TOKEN_TIME_OUT = 3600000 * 24 * 7;

    /** secret key */
    private static final String TOKEN_SIGNER_KEY = "123456";

    /**
     * Create a token
     *
     * @param id id
     * @return {@code String}
     */
    public static String getToken(Long id) {
        HashMap<String, Object> map = new HashMap<>(); // HashMap is the specified format of the payload
        map.put("id", id);  // only one id is put into the token's content
        long timeMillis = System.currentTimeMillis(); // The issue time and effect time are the current time
        byte[] bytes = TOKEN_SIGNER_KEY.getBytes();//使用默认的字符集进行转换
        String token = JWT.create().
                setJWTId(UUID.randomUUID().toString()).
                setIssuedAt(DateUtil.date(timeMillis)).  //签发时间
                        setNotBefore(DateUtil.date()).  //生效时间
                        setExpiresAt(DateUtil.date(timeMillis + TOKEN_TIME_OUT)). //失效时间
                        setSubject("Login Token").
                setIssuer("Server").
                setAudience("mobile").
                setSigner(JWTSignerUtil.hs512(bytes)). //签名的算法和秘钥
                        setPayload("data", map).  //荷载
                        sign();
        return token;
    }
}
