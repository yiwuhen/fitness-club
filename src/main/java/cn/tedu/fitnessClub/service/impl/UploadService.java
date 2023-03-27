package cn.tedu.fitnessClub.service.impl;

import cn.tedu.fitnessClub.service.IUploadService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class UploadService implements IUploadService {


    @Override
    public String upload(MultipartFile picFile)  {
        //获取文件的原始名字
        String fileName = picFile.getOriginalFilename();
        log.info("上传的文件名是:{}",fileName);
       //截取文件名
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        fileName = UUID.randomUUID()+suffix;

         log.info("修改之后的文件名:{}",fileName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        //将日期格式化为问文件夹
        String dataPath = sdf.format(new Date());
         //获得当前类路径
         String classPath = UploadService.class.getClassLoader().getResource("").getPath();

        File dirFile = new File(classPath+dataPath);
        if (!dirFile.exists()){
            dirFile.mkdirs();
        }
        //要返回的文件目录路径
        String filePath = classPath+dataPath+fileName;


        try {
            File resultFile= new File(filePath);
            picFile.transferTo(resultFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClassPathResource resource = new ClassPathResource(dataPath+fileName);
        try {
            System.out.println("文件的url:"+resource.getURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "http://localhost:10001/"+dataPath+fileName;



    }



















}
