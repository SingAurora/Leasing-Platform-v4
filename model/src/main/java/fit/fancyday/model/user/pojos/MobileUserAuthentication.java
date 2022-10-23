package fit.fancyday.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 移动端用户实名表
 * @TableName mobile_user_authentication
 */
@TableName(value ="mobile_user_authentication")
@Data
public class MobileUserAuthentication implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联的用户表ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 真实姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 身份证号
     */
    @TableField(value = "id_number")
    private String idNumber;

    /**
     * 正脸照片网址
     */
    @TableField(value = "font_image")
    private String fontImage;

    /**
     * 拒绝原因
     */
    @TableField(value = "reason")
    private String reason;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    /**
     * 提交时间
     */
    @TableField(value = "submited_time")
    private Date submitedTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time")
    private Date updatedTime;

    /**
     * 
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}