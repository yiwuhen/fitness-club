package cn.tedu.fitnessClub.mapper;

import cn.tedu.fitnessClub.pojo.entity.ArticleCategory;
import cn.tedu.fitnessClub.pojo.vo.ArticleCategoryListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticleCategoryStandardVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理类别数据的Mapper接口
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Repository
public interface CategoryMapper {

    /**
     * 插入类别数据
     *
     * @param articleCategory 类别数据
     * @return 受影响的行数
     */
    int insert(ArticleCategory articleCategory);

    /**
     * 批量插入类别数据
     *
     * @param articleCategories 类别列表
     * @return 受影响的行数
     */
    int insertBatch(List<ArticleCategory> articleCategories);

    /**
     * 根据id删除类别数据
     *
     * @param id 类别id
     * @return 受影响的行数
     */
    int deleteById(Long id);

    /**
     * 根据若干个id批量删除类别数据
     *
     * @param ids 若干个类别id的数组
     * @return 受影响的行数
     */
    int deleteByIds(Long[] ids);

    /**
     * 根据id修改类别数据
     *
     * @param articleCategory 封装了类别id和新数据的对象
     * @return 受影响的行数
     */
    int update(ArticleCategory articleCategory);

    /**
     * 统计类别表中的数据的数量
     *
     * @return 类别表中的数据的数量
     */
    int count();

    /**
     * 根据类别名称统计当前表中类别数据的数量
     *
     * @param name 类别名称
     * @return 当前表中匹配名称的类别数据的数量
     */
    int countByName(String name);

    /**
     * 统计当前表中非此类别id的匹配名称的类别数据的数量
     *
     * @param id   当前类别id
     * @param name 类别名称
     * @return 当前表中非此类别id的匹配名称的类别数据的数量
     */
    int countByNameAndNotId(@Param("id") Long id, @Param("name") String name);

    /**
     * 根据父级类别，统计其子级类别的数量
     *
     * @param parentId 父级类别的id
     * @return 此类别的子级类别的数量
     */
    int countByParentId(Long parentId);

    /**
     * 根据id查询类别数据详情
     *
     * @param id 类别id
     * @return 匹配的类别数据详情，如果没有匹配的数据，则返回null
     */
    ArticleCategoryStandardVO getStandardById(Long id);

    /**
     * 查询类别数据列表
     *
     * @return 类别数据列表
     */
    List<ArticleCategoryListItemVO> list();

    /**
     * 根据父级类别查询其子级类别列表
     *
     * @param parentId 父级类别的id
     * @return 类别列表
     */
    List<ArticleCategoryListItemVO> listByParentId(Long parentId);

}