package cn.tedu.fitnessClub.service;

import cn.tedu.fitnessClub.pojo.dto.ArticleAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticlePictureAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticlePictureUpdateDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticleUpdateDTO;
import cn.tedu.fitnessClub.pojo.vo.ArticleListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticlePictureListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticlePictureStandardVO;
import cn.tedu.fitnessClub.pojo.vo.ArticleStandardVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 处理文章图片业务的接口
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Transactional
public interface IArticlePictureService {
    /**
     * 添加文章图片
     *
     * @param articlePictureAddNewDTO 新的文章图片数据
     */
    void addNew(ArticlePictureAddNewDTO articlePictureAddNewDTO);

    /**
     * 删除文章图片
     *
     * @param id 尝试删除的文章图片数据的ID
     */
    void delete(Long id);

    /**
     * 修改文章图片数据
     * @param id 被修改的文章图片数据的ID
     * @param articlePictureUpdateDTO 文章图片的新数据
     */
    void updateInfoById(Long id, ArticlePictureUpdateDTO articlePictureUpdateDTO);

    /**
     * 根据id查询文章图片数据详情
     *
     * @param id 文章图片id
     * @return 匹配的文章图片数据详情，如果没有匹配的数据，则返回null
     */
    ArticlePictureStandardVO getStandardById(Long id);

    /**
     * 根据文章id类别查询其文章图片列表
     *
     * @param articleId 文章id
     * @return 匹配文章id的文章图片列表
     */
    List<ArticlePictureListItemVO> listByArticleId(Long articleId);

    /**
     * 查询文章图片列表
     *
     * @return 文章图片列表
     */
    List<ArticlePictureListItemVO> list();


}
