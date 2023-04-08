package cn.tedu.fitnessClub.service.impl;

import cn.tedu.fitnessClub.ex.ServiceException;
import cn.tedu.fitnessClub.mapper.ArticleCategoryMapper;
import cn.tedu.fitnessClub.mapper.ArticleMapper;
import cn.tedu.fitnessClub.mapper.ArticlePictureMapper;
import cn.tedu.fitnessClub.pojo.dto.ArticleAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticleUpdateDTO;
import cn.tedu.fitnessClub.pojo.entity.Article;
import cn.tedu.fitnessClub.pojo.vo.*;
import cn.tedu.fitnessClub.restful.JsonPage;
import cn.tedu.fitnessClub.restful.ServiceCode;
import cn.tedu.fitnessClub.service.IArticleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements IArticleService {

    //获取当前项目的根路径
    String projectPath = System.getProperty("user.dir");
    //目录相当于是根路径下
    private final static String UPLOAD_PATH_PREFIX = "/src/main/resources/static/";

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticlePictureMapper articlePictureMapper;

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
        article.setViewCount(0L);
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
        // 根据文章id查询到文章
        ArticleStandardVO article = articleMapper.getStandardById(id);
        log.debug("根据ID={}检查文章数据是否存在，查询结果：{}", id, article);
        if (article == null) {
            String message = "删除文章失败，尝试删除的文章数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        // 根据文章id删除文章
        log.debug("即将执行删除文章：{}", id);
        int rows = articleMapper.deleteById(id);
        if (rows != 1) {
            String message = "删除文章失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }

        // 根据文章id删除封面的数据库信息
        ArticlePictureStandardVO standardByArticleId = articlePictureMapper.getStandardByArticleId(id);
        // 如果该文章没有关联图片，return
        if (standardByArticleId == null) {
            return;
        }
        Long delPicId = standardByArticleId.getId();
        log.debug("即将删除对应文章的封面，id：{}",delPicId);
        int rows2 = articlePictureMapper.deleteById(delPicId);
        if (rows2 != 1){
            String message = "删除文章对应封面失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE,message);
        }

        // 根据文章id删除封面的本地文件
        log.debug("要删除的图片数据{}",standardByArticleId);
        String deleteUrl = standardByArticleId.getUrl().substring("http://localhost:10001/".length());
        log.debug("要删除的图片url{}",deleteUrl);
        System.out.println(standardByArticleId);
        //log.debug("得到了文章id对应服务器图片的url：{}",deleteUrl+"即将执行删除操作！");
        String filePath3 = projectPath + UPLOAD_PATH_PREFIX + deleteUrl;
        String filePath4 = filePath3.replace("\\", "/");
        log.debug("要处理的图片的路径是：{}", filePath3);
        File file = new File(filePath4);
        if (file.delete()) {
            log.debug("文件【{}】删除成功！", file);
        } else {
            log.debug("文件【{}】删除失败！", file);
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
        articleMapper.updateViewCountById(id);
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
    public ArticleAndPictureStandardVO getArticleAndPictureStandardById(Long id) {
        log.debug("开始处理【根据ID查询文章包含图片详情】的业务，参数：{}", id);
        ArticleAndPictureStandardVO queryResult = articleMapper.getArticleAndPictureStandardById(id);
        if (queryResult == null) {
            String message = "查询文章包含图片详情失败，文章数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }
        return queryResult;
    }

    @Override
    public List<ArticleListItemVO> listByCategoryId(Long categoryId) {
        log.debug("开始处理【根据文章类别查询其文章列表】的业务，参数:{}",categoryId);
        List<ArticleListItemVO> list = articleMapper.listByCategoryId(categoryId);
        return list;
    }

    @Override
    public List<ArticleListItemVO> listByCategoryIds(Long[] categoryIds) {
        log.debug("开始处理【根据多个文章类别查询其文章列表】的业务，参数:{}",categoryIds);
        List<ArticleListItemVO> list = articleMapper.listByCategoryIds(categoryIds);
        return list;
    }

    @Override
    public List<ArticleListItemVO> list() {
        log.debug("开始处理【查询文章列表】的业务，无参数");
        List<ArticleListItemVO> list = articleMapper.list();
        return list;
    }

    @Override
    public JsonPage<ArticleAndPictureStandardVO> getAllArticlesAndPicturesByPage(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        log.debug("开始处理【查询全部文章包含图片列表并分页】的业务");
        List<ArticleAndPictureStandardVO> list = articleMapper.listAll();
        return JsonPage.restPage(new PageInfo<>(list));
    }

    @Override
    public JsonPage<ArticleAndPictureStandardVO> getArticleAndPictureByCategoryIdAndPage(Long categoryId, Integer page, Integer pageSize) {
        log.debug("页码:{},每页条数{}",page,pageSize);
        Long[] ids = getIds(categoryId);
        log.debug("数组为:{}",ids);
        log.debug("开始处理【根据文章类别查询文章包含图片列表并分页】的业务");
        PageHelper.startPage(page,pageSize);
        List<ArticleAndPictureStandardVO> list = articleMapper.listAllByCategoryIds(ids);
        log.debug("开始处理【根据文章类别查询文章包含图片列表并分页】的业务,查出的文章为:{}",list);
        PageInfo<ArticleAndPictureStandardVO> info = new PageInfo<>(list);
        log.debug("分页信息为:{}",info);
        return JsonPage.restPage(info);
    }

//    private Long[] getId(Long categoryId) {
//        List<Long> list1 = new ArrayList<>();
//        List<ArticleCategoryListItemVO> sonsList = articleCategoryMapper.listByParentId(categoryId);
//        if (sonsList.isEmpty()) {
//            Long[] arr = new Long[1];
//            arr[0] = categoryId;
//            return arr;
//        }
//        for (ArticleCategoryListItemVO sonList : sonsList) {
//            if (sonList.getIsParent()==1) {
//                List<ArticleCategoryListItemVO> grandSons = articleCategoryMapper.listByParentId(sonList.getId());
//                for (ArticleCategoryListItemVO grandSon : grandSons) {
//                    list1.add(grandSon.getId());
//                }
//            } else {
//                list1.add(sonList.getId());
//            }
//        }
//        Long[] array= new Long[list1.size()];
//        for(int i=0; i<array.length;i++){
//            array[i] = list1.get(i);
//        }
//        return array;
//    }

//    private Long[] getIds(Long categoryId) {
//        List<Long> list1 = new ArrayList<>();
//        List<ArticleCategoryListItemVO> listAll = articleCategoryMapper.list();
//        for (ArticleCategoryListItemVO itemVO : listAll) {
//            if (itemVO.getId()==categoryId) {
//                if (itemVO.getIsParent()==1) {
//                    for (ArticleCategoryListItemVO categoryListItemVO : listAll) {
//                        if (categoryListItemVO.getParentId()==itemVO.getId()) {
//                            if (categoryListItemVO.getIsParent()==1) {
//                                for (ArticleCategoryListItemVO listItemVO : listAll) {
//                                    if (listItemVO.getParentId()==categoryListItemVO.getId()) {
//                                        list1.add(listItemVO.getId());
//                                    }
//                                }
//                            } else {
//                                list1.add(categoryListItemVO.getId());
//                            }
//                        }
//                    }
//                } else {
//                    list1.add(categoryId);
//                }
//            }
//        }
//        Long[] array= new Long[list1.size()];
//        for(int i=0; i<array.length;i++){
//            array[i] = list1.get(i);
//        }
//        return array;
//    }
    List<Long> listItem;
    private void getId(List<ArticleCategoryListItemVO> list,Long categoryId) {
        for (ArticleCategoryListItemVO categoryListItemVO : list) {
            if (categoryListItemVO.getId()==categoryId) {
                if (categoryListItemVO.getIsParent()==1) {
                    for (ArticleCategoryListItemVO articleCategoryListItemVO : list) {
                        if (articleCategoryListItemVO.getParentId()==categoryListItemVO.getId()) {
                            getId(list,articleCategoryListItemVO.getId());
                        }
                    }
                } else {
                    listItem.add(categoryId);
                    return;
                }
            }
        }
    }

    private Long[] getIds(Long categoryId) {
        listItem = new ArrayList<>();
        List<ArticleCategoryListItemVO> listAll = articleCategoryMapper.list();
        getId(listAll,categoryId);
        Long[] array= new Long[listItem.size()];
        for(int i=0; i<array.length;i++){
            array[i] = listItem.get(i);
        }
        return array;
    }




}
