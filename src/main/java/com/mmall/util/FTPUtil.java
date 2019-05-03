package com.mmall.util;

import com.mmall.vo.FileList;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author wupeiliang
 */
public class FTPUtil {

    private static Logger  logger = LoggerFactory.getLogger(FTPUtil.class);
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    public static boolean uploadFile(FileList fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        boolean isSuccess = ftpUtil.uploadFile(fileList.getRemotePath(), fileList.getFiles());
        return isSuccess;
    }

    private boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        boolean isSuccess = true;
        FileInputStream fis = null;
        if(connectServer(this.ip, this.port, this.user, this.psw)){
            try {
                //切换工作目录
                ftpClient.changeWorkingDirectory("mmall");
                ftpClient.makeDirectory(remotePath);
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();//打开被动模式
                for(File fileItem : fileList){
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
                logger.info("FTPUtil--> uploadFile() 文件上传成功");
            } catch (IOException e) {
                isSuccess = false;
                logger.info("FTPUtil--> uploadFile() 文件上传异常", e);
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }

        return isSuccess;
    }

    private boolean connectServer(String ip,int port,String user, String psw ){
        boolean isSuccess = false;
        this.ftpClient = new FTPClient();
        logger.info("开始链接ftp服务器");
        try {
            ftpClient.connect(ip,port);
            ftpClient.login(user, psw);
            isSuccess = true;
        } catch (IOException e) {
            logger.info("链接文件服务器异常：",e);
        }
        return isSuccess;
    }

    private String ip;
    private int port;
    private String user;
    private String psw;
    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String psw){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.psw = psw;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public static String getDirName() {
        return simpleDateFormat.format(new Date());
    }

}
