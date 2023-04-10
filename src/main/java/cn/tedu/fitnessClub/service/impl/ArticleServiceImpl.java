package cn.tedu.fitnessClub.service.impl;

import cn.tedu.fitnessClub.ex.ServiceException;
import cn.tedu.fitnessClub.mapper.ArticleCategoryMapper;
import cn.tedu.fitnessClub.mapper.ArticleMapper;
import cn.tedu.fitnessClub.mapper.ArticlePictureMapper;
import cn.tedu.fitnessClub.pojo.dto.ArticleAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticleUpdateDTO;
import cn.tedu.fitnessClub.pojo.entity.Article;
import cn.tedu.fitnessClub.pojo.entity.Img;
import cn.tedu.fitnessClub.pojo.entity.OldContentImg;
import cn.tedu.fitnessClub.pojo.vo.*;
import cn.tedu.fitnessClub.restful.JsonPage;
import cn.tedu.fitnessClub.restful.ServiceCode;
import cn.tedu.fitnessClub.service.IArticleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.File;
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

        // 根据文章id删除正文的本地文件
        Article needDelImagesArticle = new Article();
        BeanUtils.copyProperties(article, needDelImagesArticle);
        if (needDelImagesArticle.getContent()!=null){
            Img[] needDelImages = getImgSrcByArticle(needDelImagesArticle);

            for (int i = 0; i < needDelImages.length; i++) {
                Img img = needDelImages[i];
                String needDelImageUrl = img.getUrl().substring("http://localhost:10001/".length());
                log.debug("要删除的图片url{}：", needDelImageUrl);
                String filePath5 = projectPath + UPLOAD_PATH_PREFIX + needDelImageUrl;
                String filePath6 = filePath5.replace("\\", "/");
                log.debug("要处理的图片的路径是：{}", filePath6);
                File needDelImageFile = new File(filePath6);
                if (needDelImageFile.delete()) {
                    log.debug("文件【{}】删除成功！", needDelImageFile);
                } else {
                    log.debug("文件【{}】删除失败！", needDelImageFile);
                }
            }
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
        if (standardByArticleId != null) {
            // 根据文章id删除封面的本地文件
            log.debug("要删除的图片数据{}", standardByArticleId);
            String deleteUrl = standardByArticleId.getUrl().substring("http://localhost:10001/".length());
            log.debug("要删除的图片url{}", deleteUrl);
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

            Long delPicId = standardByArticleId.getId();
            log.debug("即将删除对应文章的封面，id：{}", delPicId);
            int rows2 = articlePictureMapper.deleteById(delPicId);
            if (rows2 != 1) {
                String message = "删除文章对应封面失败，服务器忙，请稍后再尝试！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_DELETE, message);
            }
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
        log.debug("开始处理【根据文章类别查询其文章列表】的业务，参数:{}", categoryId);
        List<ArticleListItemVO> list = articleMapper.listByCategoryId(categoryId);
        return list;
    }

    @Override
    public List<ArticleListItemVO> listByCategoryIds(Long[] categoryIds) {
        log.debug("开始处理【根据多个文章类别查询其文章列表】的业务，参数:{}", categoryIds);
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
    public JsonPage<ArticleAndPictureStandardVO> getArticleAndPictureByCategoryIdsAndPage(Long[] categoryIds, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        log.debug("开始处理【根据多个文章类别查询文章包含图片列表并分页】的业务");
        List<ArticleAndPictureStandardVO> list = articleMapper.listAllByCategoryIds(categoryIds);
        return JsonPage.restPage(new PageInfo<>(list));
    }

    @Override
    public JsonPage<ArticleAndPictureStandardVO> getAllArticlesAndPicturesByPage(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        log.debug("开始处理【查询全部文章包含图片列表并分页】的业务");
        List<ArticleAndPictureStandardVO> list = articleMapper.listAll();
        return JsonPage.restPage(new PageInfo<>(list));
    }

    @Override
    public JsonPage<ArticleAndPictureStandardVO> getArticleAndPictureByCategoryIdAndPage(Long categoryId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        log.debug("开始处理【根据文章类别查询文章包含图片列表并分页】的业务");
        List<ArticleAndPictureStandardVO> list = articleMapper.listAllByCategoryId(categoryId);
        return JsonPage.restPage(new PageInfo<>(list));
    }

    // 从article取出content中的img图片的src封装进Img对象的url属性内
    @Override
    public Img[] getImgSrcByArticle(Article article) {
        // 得到article的content
        String content = article.getContent();
        Document text = Jsoup.parse(String.valueOf(content));

        // 从中遍历取出<img>标签的src属性，格式为：http://localhost......jpg
        Elements imgTags = text.select("img");// 取出所有img标签

        //new一个Img[]用于接收数据
        int imagesLength = imgTags.size();// 设置数组的容量
        if (imagesLength == 0) return null;
        Img[] images = new Img[imagesLength];

        // for循环遍历取出src
        int count = 0;
        for (Element imgTag : imgTags) {
            Img image = new Img();
            image.setUrl(imgTag.attr("src"));
            log.debug("正在取出第{}个img的标签，属性是：{}", count, imgTag.attr("src"));
            images[count] = image;
            count++;
        }
        log.debug("");
        // 返回数组
        return images;
    }

    @Override
    public OldContentImg[] getOldContentImgByArticle(Article article) {
        // 得到article的content
        String content = article.getContent();
        Document text = Jsoup.parse(String.valueOf(content));

        // 从中遍历取出<img>标签的src属性，格式为：http://localhost......jpg
        Elements imgTags = text.select("img");// 取出所有img标签

        //new一个OldContentImg[]用于接收数据
        int imagesLength = imgTags.size();// 设置数组的容量
        if (imagesLength == 0) return null;
        OldContentImg[] oldContentImg = new OldContentImg[imagesLength];

        // for循环遍历取出src
        int count = 0;
        for (Element imgTag : imgTags) {
            OldContentImg image = new OldContentImg();
            image.setSrc(imgTag.attr("src"));
            log.debug("正在取出第{}个img的标签，属性是：{}", count, imgTag.attr("src"));
            oldContentImg[count] = image;
            count++;
        }
        log.debug("");
        // 返回数组
        return oldContentImg;
    }
}
