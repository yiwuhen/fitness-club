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
    @Test
    void getImgTest(){
        Article article1 = new Article();
        article1.setContent("<p><img src=\"http://localhost:10001/img/2023/04/08/e6629aee-c2d5-40e0-a4c8-485546c11dce.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p><p><img src=\"http://localhost:10001/img/2023/04/08/e611bdad-9ceb-422d-96b6-d07ad5926600.jpg\" alt=\"\" data-href=\"\" style=\"\"/></p>");
        Img[] images = articleService.getImgSrcByArticle(article1);

        for (int i = 0; i < images.length; i++) {
            System.out.println("Img " + i + ":");
            System.out.println("src: " + images[i].getUrl());
            System.out.println("alt: " + images[i].getAlt());
        }
    }
}
