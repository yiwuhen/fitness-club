package cn.tedu.fitnessClub.service;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {

    String upload(MultipartFile picFile);
}
