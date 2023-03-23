package cn.tedu.fitnessClub.controller;

import cn.tedu.fitnessClub.service.IUploadService;
import cn.tedu.fitnessClub.restful.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@Api(tags = "02. 图片上传模块")
@Slf4j
public class UploadController {
    @Autowired
    IUploadService uploadService;

    @PostMapping("")
    @ApiOperation("上传图片")
    public JsonResult<String> upload(MultipartFile picFile) {
        log.debug("开始处理上传图片操作，picFile为：{}",picFile);
        String url = uploadService.upload(picFile);
        return JsonResult.ok(url);
    }
}
