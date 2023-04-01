package cn.tedu.fitnessClub.controller;

import cn.tedu.fitnessClub.pojo.entity.Img;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/articlePictures")
@Api(tags = "04. 图片上传模块")
@Slf4j
public class UploadController {
    @Autowired
    IUploadService uploadService;

    @PostMapping("/upload")
    @ApiOperation("上传图片")
    public JsonResult<String> upload( MultipartFile picFile)  {
        log.debug("开始处理上传图片操作，picFile为：{}",picFile);
        String url = uploadService.upload(picFile);
        System.out.println("最终的路径是:"+url);
        return JsonResult.ok(url);
    }
    @PostMapping("/uploadImg")
    @ApiOperation("上传图片")
    public Map<String,Object> uploadContent(MultipartFile picFile)  {
        Map<String,Object> resultMap = new HashMap<>();
        log.debug("开始处理上传图片操作，picFile为：{}",picFile);
        String fileName = uploadService.upload(picFile);
        System.out.println("最终的路径是:"+fileName);
        resultMap.put("errno", 0);
        String url = "http://localhost:10001/img/"+fileName;
        Img img = new Img();
        img.setUrl(url);
        resultMap.put("data", img);
        return resultMap;
    }
}
