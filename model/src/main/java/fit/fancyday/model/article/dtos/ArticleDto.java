package fit.fancyday.model.article.dtos;

import fit.fancyday.model.article.pojos.Article;
import lombok.Data;

@Data
public class ArticleDto  extends Article {

    /**
     * 文章内容
     */
    private String content;
}
