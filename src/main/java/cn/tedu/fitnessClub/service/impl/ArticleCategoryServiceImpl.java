package cn.tedu.fitnessClub.service.impl;

import cn.tedu.fitnessClub.ex.ServiceException;
import cn.tedu.fitnessClub.mapper.ArticleCategoryMapper;
import cn.tedu.fitnessClub.mapper.ArticleMapper;
import cn.tedu.fitnessClub.pojo.dto.ArticleCategoryAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticleCategoryUpdateDTO;
import cn.tedu.fitnessClub.pojo.entity.ArticleCategory;
import cn.tedu.fitnessClub.pojo.vo.ArticleCategoryListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticleCategoryStandardVO;
import cn.tedu.fitnessClub.restful.ServiceCode;
import cn.tedu.fitnessClub.service.IArticleCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ArticleCategoryServiceImpl implements IArticleCategoryService {

    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    public ArticleCategoryServiceImpl() {
        log.debug("创建业务类对象：CategoryServiceImpl");
    }

    @Override
    public void addNew(ArticleCategoryAddNewDTO articleCategoryAddNewDTO) {
        // 调用参数对象的getName()得到尝试添加的文章类别的名称
        String name = articleCategoryAddNewDTO.getName();
        // 调用Mapper对象的countByName()执行统计查询
        int count = articleCategoryMapper.countByName(name);
        log.debug("根据名称【{}】统计数量，结果：{}", name, count);
        // 判断统计结果是否大于0
        if (count > 0) {
            // 是：名称被占用，抛出异常
            String message = "添加文章类别失败，文章类别名称已经被占用！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        // 通过参数对象获取parentId
        Long parentId = articleCategoryAddNewDTO.getParentId();
        // 预设depth（深度值）为1
        Integer depth = 1;
        ArticleCategoryStandardVO parentCategory = null;
        // 判断parentId是否不为0
        if (parentId != 0) {
            // 否：调用Mapper对象的getStandardById()查询父级文章类别
            parentCategory = articleCategoryMapper.getStandardById(parentId);
            // 判断查询结果（父级文章类别）是否为null
            if (parentCategory == null) {
                // 是：抛出异常
                String message = "添加文章类别失败，父级文章类别不存在！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
            } else {
                // 否：depth（深度值）为：父级文章类别的depth + 1
                depth = parentCategory.getDepth() + 1;
            }
        }

        // 创建Category对象
        ArticleCategory articleCategory = new ArticleCategory();
        // 复制属性
        BeanUtils.copyProperties(articleCategoryAddNewDTO, articleCategory);
        // 补全Category对象的属性值：depth >>>
        articleCategory.setDepth(depth);
        // 补全Category对象的属性值：isParent >>> 固定为0
        articleCategory.setIsParent(0);
        // 调用Mapper对象的insert()方法执行插入
        int rows = articleCategoryMapper.insert(articleCategory);
        if (rows != 1) {
            String message = "添加文章类别失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }

        // 判断parentId是否不为0，并判断父级文章类别的isParent是否为0
        if (parentId != 0 && parentCategory.getIsParent() == 0) {
            // 是：将父级文章类别的isParent更新为1
            ArticleCategory updateParentCategory = new ArticleCategory();
            updateParentCategory.setId(parentId);
            updateParentCategory.setIsParent(1);
            rows = articleCategoryMapper.update(updateParentCategory);
            if (rows != 1) {
                String message = "添加文章类别失败，服务器忙，请稍后再尝试！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
            }
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("开始处理【根据ID删除文章类别】的业务，参数：{}", id);
        // 调用Mapper对象的getStandardById()执行查询
        ArticleCategoryStandardVO currentCategory = articleCategoryMapper.getStandardById(id);
        // 判断查询结果是否为null，如果是，则抛出异常
        if (currentCategory == null) {
            // 是：数据不存在，抛出异常
            String message = "删除文章类别失败，尝试删除的文章类别数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        // 判断查询结果中的isParent是否为1，如果是，则抛出异常
        if (currentCategory.getIsParent() == 1) {
            String message = "删除文章类别失败，该文章类别仍包含子级文章类别！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        //判断该文章类别是否仍有文章关联,如果有.则抛出异常
        if (!articleMapper.listByCategoryId(id).isEmpty()) {
            log.debug("查出的文章详情:{}", articleMapper.listByCategoryId(id));
            String message = "删除文章类别失败，该文章类别仍有文章关联！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        // 调用Mapper对象的deleteById()方法执行删除
        int rows = articleCategoryMapper.deleteById(id);
        if (rows != 1) {
            String message = "删除文章类别失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }

        // 调用Mapper对象的countByParentId方法，根据以上查询结果中的parentId，执行统计
        Long parentId = currentCategory.getParentId();
        int count = articleCategoryMapper.countByParentId(parentId);
        // 判断统计结果为0，则将父级文章类别的isParent更新为0
        if (count == 0) {
            ArticleCategory parentCategory = new ArticleCategory();
            parentCategory.setId(parentId);
            parentCategory.setIsParent(0);
            rows = articleCategoryMapper.update(parentCategory);
            if (rows != 1) {
                String message = "删除文章类别失败，服务器忙，请稍后再尝试！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
            }
        }
    }

    @Override
    public void setEnable(Long id) {
        log.debug("开始处理【启用文章类别】的业务，参数：{}", id);
        updateEnableById(id, 1);
    }

    @Override
    public void setDisable(Long id) {
        log.debug("开始处理【禁用文章类别】的业务，参数：{}", id);
        updateEnableById(id, 0);
    }

    @Override
    public void setDisplay(Long id) {
        log.debug("开始处理【将文章类别显示在导航栏】的业务，参数：{}", id);
        updateDisplayById(id, 1);
    }

    @Override
    public void setHidden(Long id) {
        log.debug("开始处理【将文章类别不显示在导航栏】的业务，参数：{}", id);
        updateDisplayById(id, 0);
    }

    @Override
    public void updateInfoById(Long id, ArticleCategoryUpdateDTO articleCategoryUpdateDTO) {
        log.debug("开始处理【修改文章类别详情】的业务，ID：{}，新数据：{}", id, articleCategoryUpdateDTO);
        // 调用Mapper对象的getStandardById()执行查询
        ArticleCategoryStandardVO queryResult = articleCategoryMapper.getStandardById(id);
        // 判断查询结果是否为null
        if (queryResult == null) {
            // 是：抛出异常
            String message = "修改文章类别详情失败，尝试修改的文章类别数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        // 调用Mapper对象的countByNameAndNotId()执行统计
        int count = articleCategoryMapper.countByNameAndNotId(id, articleCategoryUpdateDTO.getName());
        // 判断统计结果是否大于0
        if (count > 0) {
            // 是：名称被占用，抛出异常
            String message = "修改文章类别详情失败，文章类别名称已经被占用！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        // 创建Category对象
        ArticleCategory category = new ArticleCategory();
        // 复制属性，设置ID
        BeanUtils.copyProperties(articleCategoryUpdateDTO, category);
        category.setId(id);
        // 调用Mapper对象的update()方法执行修改
        int rows = articleCategoryMapper.update(category);
        if (rows != 1) {
            String message = "修改文章类别详情失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    @Override
    public ArticleCategoryStandardVO getStandardById(Long id) {
        log.debug("开始处理【根据ID查询文章类别详情】的业务，参数：{}", id);
        ArticleCategoryStandardVO queryResult = articleCategoryMapper.getStandardById(id);
        if (queryResult == null) {
            // 是：抛出异常
            String message = "查询文章类别详情失败，文章类别数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }
        return queryResult;
    }

    @Override
    public List<ArticleCategoryListItemVO> listByParentId(Long parentId) {
        log.debug("开始处理【根据父级文章类别查询子级文章类别列表】的业务，参数：{}", parentId);
        List<ArticleCategoryListItemVO> list = articleCategoryMapper.listByParentId(parentId);
        return list;
    }

    @Override
    public List<ArticleCategoryListItemVO> listChildrenByParentId() {
        //开始查询
        List<ArticleCategoryListItemVO> list = articleCategoryMapper.listByParentId(0L);
        for (ArticleCategoryListItemVO categoryList : list) {
            getChildren(categoryList);

        }
        return list;
    }

    private void getChildren(ArticleCategoryListItemVO articleCategoryList) {
        //获得这个集合的所有子集
        List<ArticleCategoryListItemVO> childrens = articleCategoryMapper.listByParentId(articleCategoryList.getId());

        //如果子集为空,则停止
        if (childrens == null) {
            return;
        }
        //将子集放进父级的子集列表
        articleCategoryList.setChildren(childrens);

        //遍历子集,
        for (ArticleCategoryListItemVO c : childrens) {
            getChildren(c);
        }
    }

    private void updateEnableById(Long id, Integer enable) {
        // 调用Mapper对象的getStandardById()方法执行查询
        ArticleCategoryStandardVO currentCategory = articleCategoryMapper.getStandardById(id);
        // 判断查询结果是否为null，如果是，则抛出异常
        if (currentCategory == null) {
            String message = ENABLE_TEXT[enable] + "文章类别失败，文章类别数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        // 判断查询结果中的enable与参数enable是否相同，如果是，则抛出异常（当前状态与目标状态相同，没必要执行更新）
        if (currentCategory.getEnable() == enable) {
            String message = ENABLE_TEXT[enable] + "文章类别失败，此文章类别已经处于" + ENABLE_TEXT[enable] + "状态！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        // 创建Category对象
        ArticleCategory updateCategory = new ArticleCategory();
        // 向Category对象中封装属性值：id, enable，均来自方法参数
        updateCategory.setId(id);
        updateCategory.setEnable(enable);
        // 调用Mapper对象的update()方法执行更新
        int rows = articleCategoryMapper.update(updateCategory);
        if (rows != 1) {
            String message = ENABLE_TEXT[enable] + "文章类别失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    private void updateDisplayById(Long id, Integer isDisplay) {
        // 调用Mapper对象的getStandardById()方法执行查询
        ArticleCategoryStandardVO currentCategory = articleCategoryMapper.getStandardById(id);
        // 判断查询结果是否为null，如果是，则抛出异常
        if (currentCategory == null) {
            String message = DISPLAY_TEXT[isDisplay] + "文章类别失败，文章类别数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        // 判断查询结果中的isDisplay与参数isDisplay是否相同，如果是，则抛出异常（当前状态与目标状态相同，没必要执行更新）
        if (currentCategory.getIsDisplay() == isDisplay) {
            String message = DISPLAY_TEXT[isDisplay] + "文章类别失败，此文章类别已经处于" + DISPLAY_TEXT[isDisplay] + "状态！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        // 创建Category对象
        ArticleCategory updateCategory = new ArticleCategory();
        // 向Category对象中封装属性值：id, isDisplay，均来自方法参数
        updateCategory.setId(id);
        updateCategory.setIsDisplay(isDisplay);
        // 调用Mapper对象的update()方法执行更新
        int rows = articleCategoryMapper.update(updateCategory);
        if (rows != 1) {
            String message = DISPLAY_TEXT[isDisplay] + "文章类别失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

}
