package fit.fancyday.model.article.vos;

import fit.fancyday.model.article.pojos.Article;
import lombok.Data;

@Data
public class HotArticleVo extends Article {
    /**
     * 文章分值
     */
    private Integer score;
}
