package cn.tedu.fitnessClub.mapper;

import cn.tedu.fitnessClub.pojo.entity.ArticlePicture;
import cn.tedu.fitnessClub.pojo.vo.ArticlePictureListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticlePictureStandardVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理文章图片数据的Mapper接口
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Repository
public interface ArticlePictureMapper {

    /**
     * 插入文章图片数据
     *
     * @param articlePicture 文章图片数据
     * @return 受影响的行数
     */
    int insert(ArticlePicture articlePicture);

    /**
     * 批量插入文章图片数据
     *
     * @param pictureList 若干个文章图片数据的集合
     * @return 受影响的行数
     */
    int insertBatch(List<ArticlePicture> pictureList);

    /**
     * 根据id删除文章图片数据
     *
     * @param id 文章图片id
     * @return 受影响的行数
     */
    int deleteById(Long id);

    /**
     * 批量删除文章图片数据
     *
     * @param ids 需要删除的若干个文章图片的id
     * @return 受影响的行数
     */
    int deleteByIds(Long[] ids);

    /**
     * 更新文章图片数据
     *
     * @param picture 封装了文章图片的id和需要更新的新数据的对象
     * @return 受影响的行数
     */
    int update(ArticlePicture picture);

    /**
     * 统计文章图片数据的数量
     *
     * @return 文章图片数据的数量
     */
    int count();

    /**
     * 根据文章id统计文章图片数据的数量
     *
     * @param articleId 文章id
     * @return 与此文章关联的文章图片数据的数量
     */
    int countByArticleId(Long articleId);

    /**
     * 根据id查询文章图片数据详情
     *
     * @param id 文章图片id
     * @return 匹配的文章图片数据详情，如果没有匹配的数据，则返回null
     */
    ArticlePictureStandardVO getStandardById(Long id);

    /**
     * 根据article_id查询封面图片id
     *
     * @param articleId 文章id
     * @return 对应的图片id
     */
    Long selectPictureIdByArticleId (Long articleId);

    /**
     * 根据article_id查询文章图片数据详情
     *
     * @param articleId 文章id
     * @return 匹配的文章图片数据详情，如果没有匹配的数据，则返回null
     */
    ArticlePictureStandardVO getStandardByArticleId(Long articleId);

    /**
     * 根据articleId查询文章图片列表
     *
     * @param articleId 文章id
     * @return 该文章id对应文章图片数据列表
     */
    List<ArticlePictureListItemVO> listByArticleId(Long articleId);

    /**
     * 查询文章图片数据列表
     *
     * @return 文章图片数据列表
     */
    List<ArticlePictureListItemVO> list();

    ArticlePictureStandardVO getStandardByArticle(Long id);
}
