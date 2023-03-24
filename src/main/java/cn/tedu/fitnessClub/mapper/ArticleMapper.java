package cn.tedu.fitnessClub.mapper;

import cn.tedu.fitnessClub.pojo.entity.Article;
import cn.tedu.fitnessClub.pojo.vo.ArticleListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticleStandardVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理文章数据的Mapper接口
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Repository
public interface ArticleMapper {

    /**
     * 插入文章数据
     *
     * @param article 文章图片数据
     * @return 受影响的行数
     */
    int insert(Article article);

    /**
     * 批量插入文章数据
     *
     * @param articleList 若干个文章数据的集合
     * @return 受影响的行数
     */
    int insertBatch(List<Article> articleList);

    /**
     * 根据id删除文章数据
     *
     * @param id 文章图片id
     * @return 受影响的行数
     */
    int deleteById(Long id);

    /**
     * 批量删除文章数据
     *
     * @param ids 需要删除的若干个文章的id
     * @return 受影响的行数
     */
    int deleteByIds(Long[] ids);

    /**
     * 更新文章数据
     *
     * @param article 封装了文章的id和需要更新的新数据的对象
     * @return 受影响的行数
     */
    int update(Article article);

    /**
     * 统计文章数据的数量
     *
     * @return 文章数据的数量
     */
    int count();

    /**
     * 根据类别id统计文章图片数据的数量
     *
     * @param categoryId 类别id
     * @return 与此类别id关联的文章数据的数量
     */
    int countByCategoryId(Long categoryId);

    /**
     * 统计当前表中非此文章id的匹配标题的文章数据的数量
     *
     * @param id   当前文章id
     * @param title 文章标题
     * @return 当前表中非此文章id的匹配标题的文章数据的数量
     */
    int countByNameAndNotId(@Param("id") Long id, @Param("name") String title);

    /**
     * 根据id查询文章数据详情
     *
     * @param id 文章图片id
     * @return 匹配的文章图片数据详情，如果没有匹配的数据，则返回null
     */
    ArticleStandardVO getStandardById(Long id);

    /**
     * 根据categoryId查询文章列表
     *
     * @param categoryId 类别id
     * @return 该类别id对应文章数据列表
     */
    List<ArticleListItemVO> listByCategoryId(Long categoryId);

    /**
     * 查询文章数据列表
     *
     * @return 文章数据列表
     */
    List<ArticleListItemVO> list();

}
