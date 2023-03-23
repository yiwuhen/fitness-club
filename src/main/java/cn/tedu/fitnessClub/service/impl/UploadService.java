package cn.tedu.fitnessClub.service.impl;

import cn.tedu.fitnessClub.service.IUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${dirPath}")
    private String dirPath;

    @Override
    public String upload(MultipartFile picFile) {
        String fileName = picFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID()+suffix;

        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
        String dataPath = sdf.format(new Date());
        File dirFile = new File(dirPath+dataPath);
        if (!dirFile.exists()){
            dirFile.mkdirs();
        }
        String filePath = dirPath+dataPath+fileName;
        try {
            picFile.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataPath+fileName;
    }
}
