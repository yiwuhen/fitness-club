package cn.tedu.fitnessClub.service;

import cn.tedu.fitnessClub.pojo.dto.ArticleCategoryAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticleCategoryUpdateDTO;
import cn.tedu.fitnessClub.pojo.vo.ArticleCategoryListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticleCategoryStandardVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 处理文章类别业务的接口
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Transactional
public interface IArticleCategoryService {

    /**
     * 文章类别“是否启用”的状态文本
     */
    String ENABLE_TEXT[] = {"禁用", "启用"};
    /**
     * 文章类别“是否显示在导航栏”的状态文本
     */
    String DISPLAY_TEXT[] = {"隐藏", "显示"};

    /**
     * 添加文章类别
     *
     * @param articleCategoryAddNewDTO 新的文章类别数据
     */
    void addNew(ArticleCategoryAddNewDTO articleCategoryAddNewDTO);

    /**
     * 删除文章类别
     *
     * @param id 尝试删除的文章类别数据的ID
     */
    void delete(Long id);

    /**
     * 启用文章类别
     *
     * @param id 尝试启用的文章类别的id
     */
    void setEnable(Long id);

    /**
     * 禁用文章类别
     *
     * @param id 尝试禁用的文章类别的id
     */
    void setDisable(Long id);

    /**
     * 显示文章类别
     *
     * @param id 尝试显示的文章类别的id
     */
    void setDisplay(Long id);

    /**
     * 隐藏文章类别
     *
     * @param id 尝试隐藏的文章类别的id
     */
    void setHidden(Long id);

    /**
     * 修改文章类别数据
     *
     * @param id                被修改的文章类别数据的ID
     * @param articleCategoryUpdateDTO 文章类别的新数据
     */
    void updateInfoById(Long id, ArticleCategoryUpdateDTO articleCategoryUpdateDTO);

    /**
     * 根据id查询文章类别数据详情
     *
     * @param id 文章类别id
     * @return 匹配的文章类别数据详情，如果没有匹配的数据，则返回null
     */
    ArticleCategoryStandardVO getStandardById(Long id);

    /**
     * 根据父级文章类别查询其子级文章类别列表
     *
     * @param parentId 父级文章类别的id
     * @return 文章类别列表
     */
    List<ArticleCategoryListItemVO> listByParentId(Long parentId);

    /**
     * 查询父子级联列表
     *
     * @return 父子级联的集合
     */
    List<ArticleCategoryListItemVO> listChildrenByParentId();

}
