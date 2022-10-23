package fit.fancyday.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 移动端用户关注表
 * @TableName mobile_user_follow
 */
@TableName(value ="mobile_user_follow")
@Data
public class MobileUserFollow implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 关注人ID
     */
    @TableField(value = "follow_id")
    private Integer followId;

    /**
     * 关注人昵称
     */
    @TableField(value = "follow_name")
    private String followName;

    /**
     * 关注度0 偶尔1 一般2 经常3 高度
     */
    @TableField(value = "level")
    private Integer level;

    /**
     * 是否动态通知
     */
    @TableField(value = "is_notice")
    private Integer isNotice;

    /**
     * 创建时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}