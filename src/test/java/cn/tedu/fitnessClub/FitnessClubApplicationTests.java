package cn.tedu.fitnessClub;

import cn.tedu.fitnessClub.mapper.ArticlePictureMapper;
import cn.tedu.fitnessClub.pojo.entity.Article;
import cn.tedu.fitnessClub.pojo.entity.Img;
import cn.tedu.fitnessClub.service.IArticleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class FitnessClubApplicationTests {
    @Autowired
    ArticlePictureMapper articlePictureMapper;
    @Autowired
    private IArticleService articleService;

    @Test
    void contextLoads() {
    }
    @Test
    void aVoid(){
        // 根据文章id删除封面的本地文件
        String url = articlePictureMapper.getStandardByArticleId(5L).getUrl();


        log.debug("得到了文章id对应服务器图片数据：{}",url);

    }

    // 测试取出content正文图片src的方法

}
