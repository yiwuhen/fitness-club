package cn.tedu.fitnessClub.service;

import cn.tedu.fitnessClub.pojo.dto.ArticleAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticleUpdateDTO;
import cn.tedu.fitnessClub.pojo.entity.Article;
import cn.tedu.fitnessClub.pojo.vo.*;
import cn.tedu.fitnessClub.restful.JsonPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 处理文章业务的接口
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Transactional
public interface IArticleService {
    /**
     * 添加文章
     *
     * @param articleAddNewDTO 新的文章数据
     */
    Long addNew(ArticleAddNewDTO articleAddNewDTO);

    /**
     * 删除文章
     *
     * @param id 尝试删除的文章数据的ID
     */
    void delete(Long id);

    /**
     * 修改文章数据
     * @param id 被修改的文章数据的ID
     * @param articleUpdateDTO 文章的新数据
     */
    void updateInfoById(Long id, ArticleUpdateDTO articleUpdateDTO);

    /**
     * 根据id查询文章数据详情
     *
     * @param id 文章id
     * @return 匹配的文章数据详情，如果没有匹配的数据，则返回null
     */
    ArticleStandardVO getStandardById(Long id);

    /**
     * 根据id查询文章和图片数据详情
     *
     * @param id 文章id
     * @return 匹配的文章和图片数据详情，如果没有匹配的数据，则返回null
     */
    ArticleAndPictureStandardVO getArticleAndPictureStandardById(Long id);

    /**
     * 根据文章类别查询其文章列表
     *
     * @param categoryId 文章类别的id
     * @return 匹配文章类别的文章列表
     */
    List<ArticleListItemVO> listByCategoryId(Long categoryId);

    /**
     * 根据多个文章类别查询其文章列表
     *
     * @param categoryIds 多个文章类别的id
     * @return 匹配多个文章类别的文章列表
     */
    List<ArticleListItemVO> listByCategoryIds(Long[] categoryIds);

    /**
     * 查询文章列表
     *
     * @return 文章列表
     */
    List<ArticleListItemVO> list();

    /**
     * 根据多个文章类别查询其文章包含图片列表并分页
     *
     * @param categoryIds 多个文章类别的id
     * @param page 页码
     * @param pageSize 每页条数
     * @return 匹配多个文章类别并分页的文章包含图片列表
     */
    JsonPage<ArticleAndPictureStandardVO> getArticleAndPictureByCategoryIdsAndPage(Long[] categoryIds, Integer page, Integer pageSize);

    /**
     * 分页查询文章包含图片列表
     *
     * @param page 页码
     * @param pageSize 每页条数
     * @return 分页的文章包含图片列表
     */
    JsonPage<ArticleAndPictureStandardVO> getAllArticlesAndPicturesByPage(Integer page, Integer pageSize);

    /**
     * 根据文章类别查询其文章包含图片列表并分页
     *
     * @param categoryId 文章类别的id
     * @param page 页码
     * @param pageSize 每页条数
     * @return 匹配文章类别并分页的文章包含图片列表
     */
    JsonPage<ArticleAndPictureStandardVO> getArticleAndPictureByCategoryIdAndPage(Long categoryId, Integer page, Integer pageSize);




}
