package cn.tedu.fitnessClub.controller;

import cn.tedu.fitnessClub.pojo.dto.AdminAddNewDTO;
import cn.tedu.fitnessClub.pojo.dto.AdminLoginInfoDTO;
import cn.tedu.fitnessClub.pojo.vo.AdminListItemVO;
import cn.tedu.fitnessClub.restful.JsonResult;
import cn.tedu.fitnessClub.security.LoginPrincipal;
import cn.tedu.fitnessClub.service.IAdminService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admins")
@Api(tags = "05. 管理员管理模块")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    public AdminController() {
        log.debug("创建控制器类对象：AdminController");
    }

    // http://localhost:9081/admins/login
    @ApiOperation("管理员登录")
    @ApiOperationSupport(order = 10)
    @PostMapping("/login")
    public JsonResult<String> login(AdminLoginInfoDTO adminLoginInfoDTO) {
        log.debug("开始处理【管理员登录】的请求，参数：{}", adminLoginInfoDTO);
        String jwt = adminService.login(adminLoginInfoDTO);
        return JsonResult.ok(jwt);
    }

    // http://localhost:9081/admins/add-new
    @ApiOperation("添加管理员")
    @ApiOperationSupport(order = 100)
    @PreAuthorize("hasAuthority('/ams/admin/add-new')")
    @PostMapping("/add-new")
    public JsonResult<Void> addNew(AdminAddNewDTO adminAddNewDTO) {
        log.debug("开始处理【添加管理员】的请求，参数：{}", adminAddNewDTO);
        adminService.addNew(adminAddNewDTO);
        return JsonResult.ok();
    }

    // http://localhost:9081/admins/9527/delete
    @ApiOperation("根据ID删除管理员")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParam(name = "id", value = "管理员ID", required = true, dataType = "long")
    @PreAuthorize("hasAuthority('/ams/admin/delete')")
    @PostMapping("/{id:[0-9]+}/delete")
    public JsonResult<Void> delete(@PathVariable Long id) {
        log.debug("开始处理【根据ID删除管理员】的请求，参数：{}", id);
        adminService.delete(id);
        return JsonResult.ok();
    }

    // http://localhost:9081/admins
    @ApiOperation("查询管理员列表")
    @ApiOperationSupport(order = 420)
    @PreAuthorize("hasAuthority('/ams/admin/read')")
    @GetMapping("")
    public JsonResult<List<AdminListItemVO>> list(
            @ApiIgnore @AuthenticationPrincipal LoginPrincipal loginPrincipal) {
        log.debug("开始处理【查询管理员列表】的请求，参数：无");
        log.debug("当事人信息：{}", loginPrincipal);
        log.debug("当事人信息中的ID：{}", loginPrincipal.getId());
        log.debug("当事人信息中的用户名：{}", loginPrincipal.getUsername());
        List<AdminListItemVO> list = adminService.list();
        return JsonResult.ok(list);
    }

}