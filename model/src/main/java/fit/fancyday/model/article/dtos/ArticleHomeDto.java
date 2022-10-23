package fit.fancyday.model.article.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleHomeDto {

    // 最大时间
    Date maxBehotTime;
    // 最小时间
    Date minBehotTime;
    // 要加载的条数
    Integer size;
    // 频道ID
    String tag;
}