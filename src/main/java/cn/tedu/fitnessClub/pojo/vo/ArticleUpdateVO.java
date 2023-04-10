package cn.tedu.fitnessClub.pojo.vo;

import cn.tedu.fitnessClub.pojo.entity.OldContentImg;
import lombok.Data;

import java.util.List;


@Data
public class ArticleUpdateVO {
    private ArticleStandardVO articleStandardVO;
    private List<OldContentImg[]> oldContentImg;
}
