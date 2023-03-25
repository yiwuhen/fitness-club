package cn.tedu.fitnessClub.controller;
import cn.tedu.fitnessClub.pojo.dto.ArticlePictureAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticlePictureUpdateDTO;
import cn.tedu.fitnessClub.pojo.vo.ArticlePictureListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticlePictureStandardVO;
import cn.tedu.fitnessClub.restful.JsonResult;
import cn.tedu.fitnessClub.service.IArticlePictureService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 处理文章图片相关请求的控制器类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Api(tags = "03. 文章图片管理模块")
@Slf4j
@RestController
@RequestMapping("/articlePictures")
@Validated
public class ArticlePictureController {

    @Autowired
    private IArticlePictureService articlePictureService;
    
    @PostMapping("/add-new")
    @ApiOperation("添加文章图片")
    @ApiOperationSupport(order = 100)
    public JsonResult<Void> addNew(@Valid ArticlePictureAddNewDTO articlePictureAddNewDTO) {
        log.debug("开始处理【添加文章图片】的请求，参数：{}", articlePictureAddNewDTO);
        articlePictureService.addNew(articlePictureAddNewDTO);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/delete")
    @ApiOperation("根据ID删除文章图片")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章图片ID", required = true, dataType = "long")
    })
    public JsonResult<Void> delete(@PathVariable @Range(min = 1, message = "请提交有效的ID值！") Long id) {
        log.debug("开始处理【根据ID删除文章图片】的请求，参数：{}", id);
        articlePictureService.delete(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/update")
    @ApiOperation("修改文章图片详情")
    @ApiOperationSupport(order = 300)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章图片ID", required = true, dataType = "long")
    })
    public JsonResult<Void> updateInfoById(@PathVariable @Range(min = 1, message = "请提交有效的ID值！") Long id,
                                           @Valid ArticlePictureUpdateDTO articlePictureUpdateDTO) {
        log.debug("开始处理【修改文章图片详情】的请求，ID：{}，新数据：{}", id, articlePictureUpdateDTO);
        articlePictureService.updateInfoById(id, articlePictureUpdateDTO);
        return JsonResult.ok();
    }

    @GetMapping("/{id:[0-9]+}")
    @ApiOperation("根据ID查询文章图片详情")
    @ApiOperationSupport(order = 410)
    public JsonResult<ArticlePictureStandardVO> getStandardById(
            @PathVariable @Range(min = 1, message = "请提交有效的ID值！") Long id) {
        log.debug("开始处理【根据ID查询文章图片详情】的请求，参数：{}", id);
        ArticlePictureStandardVO queryResult = articlePictureService.getStandardById(id);
        return JsonResult.ok(queryResult);
    }

    @GetMapping("")
    @ApiOperation("查询文章图片列表")
    @ApiOperationSupport(order = 420)
    public JsonResult<List<ArticlePictureListItemVO>> list() {
        log.debug("开始处理【查询文章图片列表】的请求，无参数");
        List<ArticlePictureListItemVO> list = articlePictureService.list();
        return JsonResult.ok(list);
    }

    @GetMapping("/{id:[0-9]+}/list")
    @ApiOperation("根据文章图片类别查询文章图片列表")
    @ApiOperationSupport(order = 430)
    public JsonResult<List<ArticlePictureListItemVO>> listByCategoryId(
            @PathVariable("id") @Range(min = 1, message = "请提交有效的ID值！") Long articleId
    ) {
        log.debug("开始处理【根据文章图片类别查询文章图片列表】的请求，无参数");
        List<ArticlePictureListItemVO> listByArticleId = articlePictureService.listByArticleId(articleId);
        return JsonResult.ok(listByArticleId);
    }
}
