package fit.fancyday.model.common.enums;

public enum HttpCodeEnum {

    // 操作成功==>200
    SUCCESS(200,"操作成功"),

    // 登录错误==>1~50
    NEED_LOGIN_AFTER(1,"需要登录后操作"),
    LOGIN_PASSWORD_ERROR(2,"密码错误"),
    ACCOUNT_NOT_FOUND(3, "账号不存在"),
    NUMBER_CODE_NOT_EQUAL(4, "验证码不匹配"),

    // TOKEN50~100
    TOKEN_INVALID(50,"无效的TOKEN"),
    TOKEN_REQUIRE(51,"TOKEN是必须的"),
    TOKEN_EXPIRE(52,"TOKEN已过期"),
    TOKEN_KEY(53,"TOKEN的算法或签名不对"),
    TOKEN_NULL(54, "TOKEN不能为空"),

    // SIGN验签 100~120
    SIGN_INVALID(100,"无效的SIGN"),
    SIG_TIMEOUT(101,"SIGN已过期"),

    // 参数错误 500~1000
    PARAM_REQUIRE(500,"缺少参数"),
    PARAM_INVALID(501,"无效参数"),
    PARAM_IMAGE_FORMAT_ERROR(502,"图片格式有误"),
    SERVER_ERROR(503,"服务器内部错误"),

    // 数据错误 1000~2000
    DATA_EXIST(1000,"数据已经存在"),
    DATA_PART_NOT_EXIST(1002, "前端请求，部分必要数据为空"),
    DATA_ALL_NOT_EXIST(1002,"前端请求，数据完全为空"),
    DATA_PART_ILLEGAL(1003, "部分数据不合法"),
    DATA_UPLOAD_ERROR(1004, "服务器上传失败"),


    // 数据错误 3000~3500
    NO_OPERATOR_AUTH(3000,"无权限操作"),
    NEED_ADMIN(3001,"需要管理员权限"),
    DATABASE_ERROR(3002, "数据库错误"),

    // 自媒体文章错误 3501~3600
    MATERIASL_REFERENCE_FAIL(3501,"素材引用失效");


    int code;
    String errorMessage;

    HttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
