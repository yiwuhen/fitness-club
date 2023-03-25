package cn.tedu.fitnessClub.service.impl;

import cn.tedu.fitnessClub.ex.ServiceException;
import cn.tedu.fitnessClub.mapper.ArticlePictureMapper;
import cn.tedu.fitnessClub.pojo.dto.ArticlePictureAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticlePictureUpdateDTO;
import cn.tedu.fitnessClub.pojo.entity.ArticlePicture;
import cn.tedu.fitnessClub.pojo.vo.ArticlePictureListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticlePictureStandardVO;
import cn.tedu.fitnessClub.restful.ServiceCode;
import cn.tedu.fitnessClub.service.IArticlePictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class ArticlePictureServiceImpl implements IArticlePictureService {

    @Autowired
    ArticlePictureMapper articlePictureMapper;

    @Value("${dirPath}")
    private String dirPath;

    @Override
    public void addNew(ArticlePictureAddNewDTO articlePictureAddNewDTO) {
        log.debug("开始处理【添加文章图片】的业务，参数：{}", articlePictureAddNewDTO);

        ArticlePicture articlePicture = new ArticlePicture();
        BeanUtils.copyProperties(articlePictureAddNewDTO, articlePicture);

        log.debug("即将执行插入数据，参数：{}", articlePicture);
        int rows = articlePictureMapper.insert(articlePicture);
        if (rows != 1) {
            String message = "添加失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_INSERT, message);
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("开始处理【根据ID删除文章图片】的业务，参数：{}", id);
        ArticlePictureStandardVO articlePicture= articlePictureMapper.getStandardById(id);
        log.debug("根据ID={}检查文章图片数据是否存在，查询结果：{}", id, articlePicture);
        if (articlePicture == null) {
            String message = "删除文章失败，尝试删除的文章图片数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
        }

        log.debug("即将执行删除，参数：{}", id);
        new File(dirPath+articlePicture.getUrl()).delete();
        int rows = articlePictureMapper.deleteById(id);
        if (rows != 1) {
            String message = "删除失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_DELETE, message);
        }
    }

    @Override
    public void updateInfoById(Long id, ArticlePictureUpdateDTO articlePictureUpdateDTO) {
        log.debug("开始处理【修改文章图片详情】的业务，ID：{}，新数据：{}", id, articlePictureUpdateDTO);
        ArticlePictureStandardVO queryResult = articlePictureMapper.getStandardById(id);
        new File(dirPath+queryResult.getUrl()).delete();

        ArticlePicture articlePicture = new ArticlePicture();
        BeanUtils.copyProperties(articlePictureUpdateDTO,articlePicture);
        articlePicture.setId(id);
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
    public List<ArticlePictureListItemVO> listByArticleId(Long articleId) {
        log.debug("开始处理【根据文章id查询其文章图片列表】的业务，无参数");
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
