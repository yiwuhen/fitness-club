package cn.tedu.fitnessClub;

import cn.tedu.fitnessClub.mapper.ArticlePictureMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class FitnessClubApplicationTests {
    @Autowired
    ArticlePictureMapper articlePictureMapper;

    @Test
    void contextLoads() {
    }
    @Test
    void aVoid(){
        // 根据文章id删除封面的本地文件
        String delUrl = articlePictureMapper.getStandardByArticleId(28L).getUrl().substring("http://localhost:10001/".length());
        log.debug("得到了文章id对应服务器图片的url：{}",delUrl+"即将执行删除操作！");

    }

}
