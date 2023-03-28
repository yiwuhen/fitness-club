package cn.tedu.fitnessClub.service.impl;

import cn.tedu.fitnessClub.ex.ServiceException;
import cn.tedu.fitnessClub.mapper.ArticleCategoryMapper;
import cn.tedu.fitnessClub.mapper.ArticleMapper;
import cn.tedu.fitnessClub.pojo.dto.ArticleAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticleUpdateDTO;
import cn.tedu.fitnessClub.pojo.entity.Article;
import cn.tedu.fitnessClub.pojo.vo.ArticleCategoryStandardVO;
import cn.tedu.fitnessClub.pojo.vo.ArticleListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticleStandardVO;
import cn.tedu.fitnessClub.restful.ServiceCode;
import cn.tedu.fitnessClub.service.IArticleService;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;

    @Override
    public Long addNew(@Validated ArticleAddNewDTO articleAddNewDTO) {
        log.debug("开始处理【添加文章】的业务，参数：{}", articleAddNewDTO);
        String title = articleAddNewDTO.getTitle();
        int count = articleMapper.countByTitle(title);
        log.debug("根据名称【{}】统计数量，结果：{}", title, count);
        if (count > 0) {
            String message = "添加文章失败，文章标题已经被占用！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        ArticleCategoryStandardVO articleCategoryStandardVO =
                articleCategoryMapper.getStandardById(articleAddNewDTO.getCategoryId());
        String categoryName = articleCategoryStandardVO.getName();

        Article article = new Article();
        BeanUtils.copyProperties(articleAddNewDTO, article);
        article.setCategoryName(categoryName);
        log.debug("即将执行插入数据，参数：{}", article);
        int rows = articleMapper.insert(article);
        if (rows != 1) {
            String message = "添加失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }
        return article.getId();
    }

    @Override
    public void delete(Long id) {
        log.debug("开始处理【根据ID删除文章】的业务，参数：{}", id);
        ArticleStandardVO article = articleMapper.getStandardById(id);
        log.debug("根据ID={}检查文章数据是否存在，查询结果：{}", id, article);
        if (article == null) {
            String message = "删除文章失败，尝试删除的文章数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        log.debug("即将执行删除，参数：{}", id);
        int rows = articleMapper.deleteById(id);
        if (rows != 1) {
            String message = "删除失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }
    }

    @Override
    public void updateInfoById(Long id, ArticleUpdateDTO articleUpdateDTO) {
        log.debug("开始处理【修改文章详情】的业务，ID：{}，新数据：{}", id, articleUpdateDTO);
        ArticleStandardVO queryResult = articleMapper.getStandardById(id);
        if (queryResult == null) {
            String message = "修改文章详情失败，尝试修改的文章数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }
        
        int count = articleMapper.countByNameAndNotId(id, articleUpdateDTO.getTitle());
        if (count > 0) {
            String message = "修改文章详情失败，文章名称已经被占用！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }
        
        Article article = new Article();
        BeanUtils.copyProperties(articleUpdateDTO, article);
        article.setId(id);

        ArticleCategoryStandardVO articleCategoryStandardVO =
                articleCategoryMapper.getStandardById(articleUpdateDTO.getCategoryId());
        String categoryName = articleCategoryStandardVO.getName();
        article.setCategoryName(categoryName);

        int rows = articleMapper.update(article);
        if (rows != 1) {
            String message = "更改失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }

    @Override
    public ArticleStandardVO getStandardById(Long id) {
        log.debug("开始处理【根据ID查询文章详情】的业务，参数：{}", id);
        ArticleStandardVO queryResult = articleMapper.getStandardById(id);
        if (queryResult == null) {
            String message = "查询文章详情失败，文章数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }
        return queryResult;
    }

    @Override
    public List<ArticleListItemVO> listByCategoryId(Long categoryId) {
        log.debug("开始处理【根据文章类别查询其文章列表】的业务，无参数");
        List<ArticleListItemVO> list = articleMapper.listByCategoryId(categoryId);
        return list;
    }

    @Override
    public List<ArticleListItemVO> listByCategoryIds(Long[] categoryIds) {
        log.debug("开始处理【根据多个文章类别查询其文章列表】的业务，无参数");
        List<ArticleListItemVO> list = articleMapper.listByCategoryIds(categoryIds);
        return list;
    }

    @Override
    public List<ArticleListItemVO> list() {
        log.debug("开始处理【查询文章列表】的业务，无参数");
        List<ArticleListItemVO> list = articleMapper.list();
        return list;
    }
}
