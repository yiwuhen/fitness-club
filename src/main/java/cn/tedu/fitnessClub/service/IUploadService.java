package cn.tedu.fitnessClub.service;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {
    /**
     * 上传图片
     *
     * @param picFile 上传的图片数据
     * @return 图片的url
     */
    String upload(MultipartFile picFile);
}
