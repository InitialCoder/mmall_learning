package com.mmall.service;

import com.mmall.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    //先上传到服务器下的upload 文件下，再上传至ftp 服务器
    //最后返回ftp的相对路径名
    ServerResponse upLoad(MultipartFile file , String path);

}
