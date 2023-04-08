package cn.tedu.fitnessClub.service.impl;

import cn.tedu.fitnessClub.ex.ServiceException;
import cn.tedu.fitnessClub.mapper.ArticlePictureMapper;
import cn.tedu.fitnessClub.pojo.dto.ArticlePictureAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticlePictureDeleteCoverDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticlePictureDeleteUnnecessaryPicDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticlePictureUpdateDTO;
import cn.tedu.fitnessClub.pojo.entity.ArticlePicture;
import cn.tedu.fitnessClub.pojo.vo.ArticlePictureListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticlePictureStandardVO;
import cn.tedu.fitnessClub.restful.ServiceCode;
import cn.tedu.fitnessClub.service.IArticlePictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class ArticlePictureServiceImpl implements IArticlePictureService {

    @Autowired
    ArticlePictureMapper articlePictureMapper;


    //获取当前项目的根路径
    String projectPath = System.getProperty("user.dir");
    //目录相当于是根路径下
    private final static String UPLOAD_PATH_PREFIX = "/src/main/resources/static/";

    @Override
    public void addNew(ArticlePictureAddNewDTO articlePictureAddNewDTO) {
        log.debug("开始处理【将图片添加到数据库】的业务，参数{}", articlePictureAddNewDTO);
        Long articleId = articlePictureAddNewDTO.getArticleId();

        // 拿articleId查DB有无数据，如果有，则删除后再添加，如果无，则直接添加。

        // 查询DB有无图片数据
        int row = articlePictureMapper.countByArticleId(articleId);
        log.debug("根据文章id【{}】查询数据库中有【{}】条对应数据",articleId,row);
        if (row != 0){
            // 删掉DB中的图片
            articlePictureMapper.deleteByArticleId(articleId);
            log.debug("数据库中存在图片信息，即将删除【{}】的信息！",articleId);
        }
        // 取出DTO的url
        String url = articlePictureAddNewDTO.getUrl();
        if (url=="") return;
        log.debug("取出了写入图片的url【{}】：",url);
        // new一个图片对象并传值
        ArticlePicture articlePicture = new ArticlePicture();
        articlePicture.setArticleId(articleId);
        articlePicture.setUrl(url);
        log.debug("将DTO的信息写入ArticlePicture",articlePicture);
        // 写入DB
        articlePictureMapper.insert(articlePicture);
        log.debug("【将图片添加到数据库】业务处理完毕！");
    }

    @Override
    public void delete(Long id) {
        log.debug("开始处理【根据ID删除文章图片】的业务，参数：{}", id);
        ArticlePictureStandardVO articlePicture = articlePictureMapper.getStandardById(id);
        log.debug("根据ID={}检查文章图片数据是否存在，查询结果：{}", id, articlePicture);
        if (articlePicture == null) {
            String message = "删除文章失败，尝试删除的文章图片数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        log.debug("即将执行删除，参数：{}", id);
        new File(UPLOAD_PATH_PREFIX + articlePicture.getUrl()).delete();
        int rows = articlePictureMapper.deleteById(id);
        if (rows != 1) {
            String message = "删除失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }
    }

    @Override
    public void deleteUnnecessaryPic(ArticlePictureDeleteUnnecessaryPicDTO[] articlePictureDeleteUnnecessaryPicDTO) {
        for (ArticlePictureDeleteUnnecessaryPicDTO image : articlePictureDeleteUnnecessaryPicDTO) {
            log.debug("开始处理【删除服务器静态资源文件夹冗余图片】的业务，参数：{}", image);
            String subSrc = image.getSrc().substring("http://localhost:10001/".length()); // .substring("")
            log.debug("去除localhost路径之后的路径是：{}", subSrc);
            String filePath1 = projectPath + UPLOAD_PATH_PREFIX + subSrc;
            String filePath2 = filePath1.replace("\\", "/");
            log.debug("要处理的图片的路径是：{}", filePath2);
            File file = new File(filePath2);
            if (file.delete()) {
                log.debug("文件【{}】删除成功！", file);
            } else {
                log.debug("文件【{}】删除失败！", file);
            }
        }
    }

    // 当前端点击删除封面按钮时，后端执行删除静态资源文件夹内的封面图片的方法
    // 根据isDelDB属性来决定是否对数据库发送【删除图片url】请求
    @Override
    public String deleteCoverByIsDelDB(ArticlePictureDeleteCoverDTO articlePictureDeleteCoverDTO) {
        log.debug("开始处理【删除静态资源文件夹内的封面图片】的业务，参数：{}", articlePictureDeleteCoverDTO);
        String subSrc = "";
        if (articlePictureDeleteCoverDTO.getUrl().startsWith("http://localhost:10001/")) {
            subSrc = articlePictureDeleteCoverDTO.getUrl().substring("http://localhost:10001/".length()); // .substring("")
        }
        log.debug("去除localhost后的路径是：{}", subSrc);
        String filePath3 = projectPath + UPLOAD_PATH_PREFIX + subSrc;
        String filePath4 = filePath3.replace("\\", "/");
        log.debug("要处理的图片的路径是：{}", filePath3);
        File file = new File(filePath4);
        if (file.delete()) {
            log.debug("文件【{}】删除成功！", file);
        } else {
            log.debug("文件【{}】删除失败！", file);
        }

        // 判断isDelDB的值
        // 1=删除数据库响应的url
        // 0=不删除……
        //
        if (articlePictureDeleteCoverDTO.getIsDelDB() == 1) {
            int count = articlePictureMapper.countByArticleId(articlePictureDeleteCoverDTO.getArticleId());
            if (count == 0) return "数据库里没有了";
            // 得到url
            String url = (articlePictureMapper.getStandardByArticleId(articlePictureDeleteCoverDTO.getArticleId())).getUrl();
            // 拿文章id去删除
            int rows = articlePictureMapper.deleteByArticleId(articlePictureDeleteCoverDTO.getArticleId());
            if (rows >= 1) {
                log.debug("检测到isDelDB=1，删除对应URL成功！");
            } else {
                String message = "删除失败，服务器忙，请稍后再尝试！";
                log.warn(message);
                throw new ServiceException(ServiceCode.ERROR_DELETE, message);
            }
            return url;
        }
        return "OK";
    }

    @Override
    public void updateInfoById(Long id, ArticlePictureUpdateDTO articlePictureUpdateDTO) {
        log.debug("开始处理【修改文章图片详情】的业务，ID：{}，新数据：{}", id, articlePictureUpdateDTO);
        ArticlePictureStandardVO queryResult = articlePictureMapper.getStandardById(id);
        new File(UPLOAD_PATH_PREFIX + queryResult.getUrl()).delete();

        ArticlePicture articlePicture = new ArticlePicture();
        BeanUtils.copyProperties(articlePictureUpdateDTO, articlePicture);
        articlePicture.setId(id);// TODO
        int rows = articlePictureMapper.update(articlePicture);
        if (rows != 1) {
            String message = "更改失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }


    @Override
    public ArticlePictureStandardVO getStandardById(Long id) {
        log.debug("开始处理【根据ID查询文章图片详情】的业务，参数：{}", id);
        ArticlePictureStandardVO queryResult = articlePictureMapper.getStandardById(id);
        if (queryResult == null) {
            String message = "查询文章详情失败，文章数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }
        return queryResult;
    }

    @Override
    public ArticlePictureStandardVO getStandardByArticleId(Long articleId) {
        log.debug("开始处理【根据文章ID查询文章图片详情】的业务，参数：{}", articleId);
        ArticlePictureStandardVO queryResult = articlePictureMapper.getStandardByArticleId(articleId);
        if (queryResult == null) {
            String message = "查询封面失败，图片数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }
        return queryResult;
    }

    @Override
    public List<ArticlePictureListItemVO> listByArticleId(Long articleId) {
        log.debug("开始处理【根据文章id查询其文章图片列表】的业务");
        List<ArticlePictureListItemVO> list = articlePictureMapper.listByArticleId(articleId);
        return list;
    }

    @Override
    public List<ArticlePictureListItemVO> list() {
        log.debug("开始处理【查询文章图片列表】的业务，无参数");
        List<ArticlePictureListItemVO> list = articlePictureMapper.list();
        return list;
    }


}
