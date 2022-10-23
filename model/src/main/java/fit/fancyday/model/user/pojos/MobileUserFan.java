package fit.fancyday.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 移动端用户粉丝表
 * @TableName mobile_user_fan
 */
@TableName(value ="mobile_user_fan")
@Data
public class MobileUserFan implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联的用户表id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 粉丝的用户表ID
     */
    @TableField(value = "fans_id")
    private Integer fansId;

    /**
     * 粉丝的昵称
     */
    @TableField(value = "fans_name")
    private String fansName;

    /**
     * 粉丝忠实度0 僵尸粉 1 偶尔 2 真爱粉
     */
    @TableField(value = "level")
    private Integer level;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    /**
     * 是否可见我动态
     */
    @TableField(value = "is_display")
    private Integer isDisplay;

    /**
     * 是否屏蔽私信
     */
    @TableField(value = "is_shield_letter")
    private Integer shieldLetter;

    /**
     * 是否屏蔽评论
     */
    @TableField(value = "is_shield_comment")
    private Integer shieldComment;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}