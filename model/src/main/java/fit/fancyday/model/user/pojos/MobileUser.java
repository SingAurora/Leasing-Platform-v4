package fit.fancyday.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * APP用户信息表
 */
@Data
@TableName("mobile_user")
public class MobileUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 密码、通信等加密盐
     */
    @TableField("salt")
    private String salt;

    /**
     * 用户名
     */
    @TableField("name")
    private String name;

    /**
     * 密码,md5加密
     */
    @TableField("password")
    private String password;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 头像
     */
    @TableField("image")
    private String image;

    /**
     * 账号状态
     * 0 正常 1 锁定
     */
    @TableField("status")
    private Boolean status;

    /**
     * 账号身份
     * 0 普通用户 1 商家
     */
    @TableField("identity")
    private Short identity;

    /**
     * 注册时间
     */
    @TableField("created_time")
    private Date createdTime;

}