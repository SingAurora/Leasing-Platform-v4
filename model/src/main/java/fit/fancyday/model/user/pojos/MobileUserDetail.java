package fit.fancyday.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("mobile_user_detail")
public class MobileUserDetail {
    /**
     * 关联的用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联的用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 性别
     * 1 男 2 女
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 自我介绍
     */
    @TableField("introduce")
    private String introduce;

    /**
     * 喜欢的标签
     */
    @TableField("like_tag")
    private String likeTag;

    /**
     * 账号状态
     * 1 启用 2 禁用
     */
    @TableField("status")
    private Integer status;
}
