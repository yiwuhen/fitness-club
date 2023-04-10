package cn.tedu.fitnessClub.controller;


import cn.tedu.fitnessClub.pojo.dto.ArticleCategoryAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.ArticleCategoryUpdateDTO;
import cn.tedu.fitnessClub.pojo.vo.ArticleCategoryListItemVO;
import cn.tedu.fitnessClub.pojo.vo.ArticleCategoryStandardVO;
import cn.tedu.fitnessClub.restful.JsonResult;
import cn.tedu.fitnessClub.service.IArticleCategoryService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 处理文章类别相关请求的控制器类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Api(tags = "01. 文章类别管理模块")
@Slf4j
@RestController
@RequestMapping("/articleCategories")
@Validated
public class ArticleCategoryController {

    @Autowired
    private IArticleCategoryService articleCategoryService;

    public ArticleCategoryController() {
        log.debug("创建控制器类对象：CategoryController");
    }

    @PostMapping("/add-new")
    @ApiOperation("添加文章类别")
    @ApiOperationSupport(order = 100)
    @PreAuthorize("hasAuthority('/articleCategory')")
    public JsonResult<Void> addNew(@Valid ArticleCategoryAddNewDTO articleCategoryAddNewDTO) {
        log.debug("开始处理【添加文章类别】的请求，参数：{}", articleCategoryAddNewDTO);
        articleCategoryService.addNew(articleCategoryAddNewDTO);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/delete")
    @ApiOperation("根据ID删除文章类别")
    @ApiOperationSupport(order = 200)
    @PreAuthorize("hasAuthority('/articleCategory')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章类别ID", required = true, dataType = "long")
    })
    public JsonResult<Void> delete(@PathVariable @Range(min = 1, message = "请提交有效的ID值！") Long id) {
        log.debug("开始处理【根据ID删除文章类别】的请求，参数：{}", id);
        articleCategoryService.delete(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/enable")
    @ApiOperation("启用文章类别")
    @ApiOperationSupport(order = 310)
    @PreAuthorize("hasAuthority('/articleCategory')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章类别ID", required = true, dataType = "long")
    })
    public JsonResult<Void> setEnable(@PathVariable @Range(min = 1, message = "请提交有效的ID值！") Long id) {
        log.debug("开始处理【启用文章类别】的请求，参数：{}", id);
        articleCategoryService.setEnable(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/disable")
    @ApiOperation("禁用文章类别")
    @ApiOperationSupport(order = 311)
    @PreAuthorize("hasAuthority('/articleCategory')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章类别ID", required = true, dataType = "long")
    })
    public JsonResult<Void> setDisable(@PathVariable @Range(min = 1, message = "请提交有效的ID值！") Long id) {
        log.debug("开始处理【禁用文章类别】的请求，参数：{}", id);
        articleCategoryService.setDisable(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/display")
    @ApiOperation("展示文章类别")
    @ApiOperationSupport(order = 312)
    @PreAuthorize("hasAuthority('/articleCategory')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章类别ID", required = true, dataType = "long")
    })
    public JsonResult<Void> setDisplay(@PathVariable @Range(min = 1, message = "请提交有效的ID值！") Long id) {
        log.debug("开始处理【展示文章类别】的请求，参数：{}", id);
        articleCategoryService.setDisplay(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/hidden")
    @ApiOperation("不展示文章类别")
    @ApiOperationSupport(order = 313)
    @PreAuthorize("hasAuthority('/articleCategory')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章类别ID", required = true, dataType = "long")
    })
    public JsonResult<Void> setHidden(@PathVariable @Range(min = 1, message = "请提交有效的ID值！") Long id) {
        log.debug("开始处理【不展示文章类别】的请求，参数：{}", id);
        articleCategoryService.setHidden(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/update")
    @ApiOperation("修改文章类别详情")
    @ApiOperationSupport(order = 300)
    @PreAuthorize("hasAuthority('/articleCategory')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章类别ID", required = true, dataType = "long")
    })
    public JsonResult<Void> updateInfoById(@PathVariable @Range(min = 1, message = "请提交有效的ID值！") Long id,
                                           @Valid ArticleCategoryUpdateDTO articleCategoryUpdateDTO) {
        log.debug("开始处理【修改文章类别详情】的请求，ID：{}，新数据：{}", id, articleCategoryUpdateDTO);
        articleCategoryService.updateInfoById(id, articleCategoryUpdateDTO);
        return JsonResult.ok();
    }

    @GetMapping("/{id:[0-9]+}")
    @ApiOperation("根据ID查询文章类别详情")
    @ApiOperationSupport(order = 410)
    public JsonResult<ArticleCategoryStandardVO> getStandardById(
            @PathVariable @Range(min = 1, message = "请提交有效的ID值！") Long id) {
        log.debug("开始处理【根据ID查询文章类别详情】的请求，参数：{}", id);
        ArticleCategoryStandardVO queryResult = articleCategoryService.getStandardById(id);
        return JsonResult.ok(queryResult);
    }

    @GetMapping("/list-by-parent")
    @ApiOperation("根据父级查询子级列表")
    @ApiOperationSupport(order = 420)
    public JsonResult<List<ArticleCategoryListItemVO>> list(Long parentId) {
        log.debug("开始处理【根据父级文章类别查询子级文章类别列表】的业务，参数：{}", parentId);
        List<ArticleCategoryListItemVO> list = articleCategoryService.listByParentId(parentId);
        return JsonResult.ok(list);
    }

    @GetMapping("/list-children-by-parent")
    @ApiOperation("查询父子级联级列表")
    @ApiOperationSupport(order = 430)
    public JsonResult<List<ArticleCategoryListItemVO>> list() {
        log.debug("开始处理【根据父级文章类别查询子级文章类别列表】的业务");
        List<ArticleCategoryListItemVO> list = articleCategoryService.listChildrenByParentId();
        return JsonResult.ok(list);
    }

}