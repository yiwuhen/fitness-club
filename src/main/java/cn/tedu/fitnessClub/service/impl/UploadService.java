package cn.tedu.fitnessClub.service.impl;

import cn.tedu.fitnessClub.service.IUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class UploadService implements IUploadService {
    //目录相当于是根路径下
    private final static String  UPLOAD_PATH_PREFIX = "src/main/resources/static/img/";
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
        //
        File dirFile = new File(UPLOAD_PATH_PREFIX,dataPath);
        System.out.println("没有图片名称的路径"+dirFile);
        //创建D:\fitness-club\src\main\resources\static\img\2023\03\27的路径,如果存在,就不创建
        // 不存在,创建
        if (!dirFile.exists()){
            dirFile.mkdirs();
        }
        //要返回的文件目录路径
        //时间转换为文件夹+文件名
        //dirFile.getAbsolutePath():获取根目录对应的绝对路径
        String filePath = dirFile.getAbsolutePath()+File.separator+fileName;


        try {
            //获取一个要放图片的路径
            File resultFile= new File(filePath);
            System.out.println(resultFile.getAbsolutePath());
            //向文件复制图片
            picFile.transferTo(resultFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回带时间+名字的字符串
        return dataPath+fileName;
    }

}
