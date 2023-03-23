package cn.tedu.fitnessClub.mapper;

import cn.tedu.fitnessClub.pojo.entity.ArticleCategory;
import cn.tedu.fitnessClub.pojo.vo.ArticleCategoryListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticleCategoryStandardVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理文章类别数据的Mapper接口
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Repository
public interface ArticleCategoryMapper {

    /**
     * 插入文章类别数据
     *
     * @param articleCategory 文章类别数据
     * @return 受影响的行数
     */
    int insert(ArticleCategory articleCategory);

    /**
     * 批量插入文章类别数据
     *
     * @param articleCategories 文章类别列表
     * @return 受影响的行数
     */
    int insertBatch(List<ArticleCategory> articleCategories);

    /**
     * 根据id删除文章类别数据
     *
     * @param id 文章类别id
     * @return 受影响的行数
     */
    int deleteById(Long id);

    /**
     * 根据若干个id批量删除文章类别数据
     *
     * @param ids 若干个文章类别id的数组
     * @return 受影响的行数
     */
    int deleteByIds(Long[] ids);

    /**
     * 根据id修改文章类别数据
     *
     * @param articleCategory 封装了文章类别id和新数据的对象
     * @return 受影响的行数
     */
    int update(ArticleCategory articleCategory);

    /**
     * 统计文章类别表中的数据的数量
     *
     * @return 文章类别表中的数据的数量
     */
    int count();

    /**
     * 根据文章类别名称统计当前表中文章类别数据的数量
     *
     * @param name 文章类别名称
     * @return 当前表中匹配名称的文章类别数据的数量
     */
    int countByName(String name);

    /**
     * 统计当前表中非此文章类别id的匹配名称的文章类别数据的数量
     *
     * @param id   当前文章类别id
     * @param name 文章类别名称
     * @return 当前表中非此文章类别id的匹配名称的文章类别数据的数量
     */
    int countByNameAndNotId(@Param("id") Long id, @Param("name") String name);

    /**
     * 根据父级文章类别，统计其子级文章类别的数量
     *
     * @param parentId 父级文章类别的id
     * @return 此文章类别的子级文章类别的数量
     */
    int countByParentId(Long parentId);

    /**
     * 根据id查询文章类别数据详情
     *
     * @param id 文章类别id
     * @return 匹配的文章类别数据详情，如果没有匹配的数据，则返回null
     */
    ArticleCategoryStandardVO getStandardById(Long id);

    /**
     * 查询文章类别数据列表
     *
     * @return 文章类别数据列表
     */
    List<ArticleCategoryListItemVO> list();

    /**
     * 根据父级文章类别查询其子级文章类别列表
     *
     * @param parentId 父级文章类别的id
     * @return 文章类别列表
     */
    List<ArticleCategoryListItemVO> listByParentId(Long parentId);

}