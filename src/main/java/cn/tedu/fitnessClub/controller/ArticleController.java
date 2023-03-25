package cn.tedu.fitnessClub.controller;

import cn.tedu.fitnessClub.pojo.dto.ArticleAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticleUpdateDTO;
import cn.tedu.fitnessClub.pojo.vo.ArticleListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticleStandardVO;
import cn.tedu.fitnessClub.restful.JsonResult;
import cn.tedu.fitnessClub.service.IArticleService;
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
 * 处理文章相关请求的控制器类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Api(tags = "02. 文章管理模块")
@Slf4j
@RestController
@RequestMapping("/articles")
@Validated
public class ArticleController {

    @Autowired
    private IArticleService articleService;
    
    @PostMapping("/add-new")
    @ApiOperation("添加文章")
    @ApiOperationSupport(order = 100)
    public JsonResult<Long> addNew(@Valid ArticleAddNewDTO articleAddNewDTO) {
        log.debug("开始处理【添加文章】的请求，参数：{}", articleAddNewDTO);
        Long articleId = articleService.addNew(articleAddNewDTO);
        return JsonResult.ok(articleId);
    }

    @PostMapping("/{id:[0-9]+}/delete")
    @ApiOperation("根据ID删除文章")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "long")
    })
    public JsonResult<Void> delete(@PathVariable @Range(min = 1, message = "请提交有效的ID值！") Long id) {
        log.debug("开始处理【根据ID删除文章】的请求，参数：{}", id);
        articleService.delete(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/update")
    @ApiOperation("修改文章详情")
    @ApiOperationSupport(order = 300)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "long")
    })
    public JsonResult<Void> updateInfoById(@PathVariable @Range(min = 1, message = "请提交有效的ID值！") Long id,
                                           @Valid ArticleUpdateDTO articleUpdateDTO) {
        log.debug("开始处理【修改文章详情】的请求，ID：{}，新数据：{}", id, articleUpdateDTO);
        articleService.updateInfoById(id, articleUpdateDTO);
        return JsonResult.ok();
    }

    @GetMapping("/{id:[0-9]+}")
    @ApiOperation("根据ID查询文章详情")
    @ApiOperationSupport(order = 410)
    public JsonResult<ArticleStandardVO> getStandardById(
            @PathVariable @Range(min = 1, message = "请提交有效的ID值！") Long id) {
        log.debug("开始处理【根据ID查询文章详情】的请求，参数：{}", id);
        ArticleStandardVO queryResult = articleService.getStandardById(id);
        return JsonResult.ok(queryResult);
    }

    @GetMapping("")
    @ApiOperation("查询文章列表")
    @ApiOperationSupport(order = 420)
    public JsonResult<List<ArticleListItemVO>> list() {
        log.debug("开始处理【查询文章列表】的请求，无参数");
        List<ArticleListItemVO> list = articleService.list();
        return JsonResult.ok(list);
    }

    @GetMapping("/{id:[0-9]+}/list")
    @ApiOperation("根据文章类别查询文章列表")
    @ApiOperationSupport(order = 430)
    public JsonResult<List<ArticleListItemVO>> listByCategoryId(
            @PathVariable("id") @Range(min = 1, message = "请提交有效的ID值！") Long categoryId
    ) {
        log.debug("开始处理【根据文章类别查询文章列表】的请求，无参数");
        List<ArticleListItemVO> listByCategoryId = articleService.listByCategoryId(categoryId);
        return JsonResult.ok(listByCategoryId);
    }

    @PostMapping("/list-by-categoryIds")
    @ApiOperation("根据多个文章类别查询文章列表")
    @ApiOperationSupport(order = 440)
    public JsonResult<List<ArticleListItemVO>> list(Long[] categoryIds) {
        log.debug("开始处理【查询文章列表】的请求，无参数");
        List<ArticleListItemVO> list = articleService.listByCategoryIds(categoryIds);
        return JsonResult.ok(list);
    }
}
