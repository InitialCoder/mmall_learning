package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.common.ServerResponse;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import com.mmall.vo.FileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ServerResponse upLoad(MultipartFile file, String path) {
       String fileName = file.getOriginalFilename();
       String remotePath = FTPUtil.getDirName();
       //扩展名
       String fileExtensionName = fileName.substring(fileName.indexOf(".")+1);
       //上传后的名字
       String uploadFileName = remotePath+"/"+UUID.randomUUID().toString()+"."+fileExtensionName;
       logger.info("开始上传文件，上传文件名为：{}，上传路径：{}，新文件名：{}",fileName,path,uploadFileName);

       File fileDir = new File(path);
       if(!fileDir.exists()){
           fileDir.setWritable(true);
           fileDir.mkdirs();
       }
       File fileDir1 = new File(path+"/"+remotePath);
       if(!fileDir1.exists()){
           fileDir1.setWritable(true);
           fileDir1.mkdirs();
       }

       File targetFile = new File(path,uploadFileName);
       try {
           //上传至目录下的upload 文件夹
           file.transferTo(targetFile);
           //上传至ftp 服务器
           FileList fileList = new FileList();
           fileList.setRemotePath(remotePath);
           fileList.setFiles(Lists.newArrayList(targetFile));
          boolean isSuccess = FTPUtil.uploadFile(fileList);
           //上传成功后将targetFile 删除
          if(isSuccess){
              targetFile.delete();
          }

       } catch (IOException e) {
           logger.info("上传文件失败：",e);
           return ServerResponse.createByError("上传文件失败") ;
       }
        return ServerResponse.createBySuccess(uploadFileName) ;
    }
}
