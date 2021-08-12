package com.nilin.meteor.common.util;

import com.nilin.meteor.common.constant.Constant;
import com.nilin.meteor.service.SysconfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FtpUtil implements ApplicationContextAware {

    private final static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    private static ApplicationContext applicationContext = null;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(FtpUtil.applicationContext == null){
            FtpUtil.applicationContext  = applicationContext;
        }
    }

    public int getPort(){
        int port = 0;
        SysconfigService sysconfigService = applicationContext.getBean(SysconfigService.class);
        String config = sysconfigService.getSysconfig(Constant.ftp.FTP_PORT);
        if(StringUtils.isNotBlank(config)){
            port = Integer.parseInt(config);
        }
        return port;
    }

    public String getConfig(String key){
        SysconfigService sysconfigService = applicationContext.getBean(SysconfigService.class);
        return sysconfigService.getSysconfig(key);
    }

    /**
     *
     * @return
     */
    public FTPClient getFTPClient() {
        FTPClient ftp = new FTPClient();
        try {
            String server = getConfig(Constant.ftp.FTP_SERVER);
            int port = getPort();
            String username = getConfig(Constant.ftp.FTP_USERNAME);
            String password = getConfig(Constant.ftp.FTP_PASSWORD);

            ftp.connect(server, port);
            ftp.login(username, password);
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                closeConnect(ftp);
                return null;
            }
        } catch (Exception e) {
            logger.error("创建ftp服务器失败",e);
        }
        return ftp;
    }

    private void closeConnect(FTPClient ftp) {
        if (ftp != null && ftp.isConnected()) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException e) {
                logger.error("关闭ftp服务器失败",e);
            }
        }
    }


    /**
     * 向FTP服务器上传文件
     * @param basePath FTP服务器基础目录
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
     * @param filename 上传到FTP服务器上的文件名
     * @param input 输入流
     * @return 成功返回true，否则返回false
     */
    public boolean uploadFile(String basePath, String filePath, String filename, InputStream input) {
        boolean result = false;
        FTPClient ftp = getFTPClient();
        try {
            //切换到上传目录
            if (!ftp.changeWorkingDirectory(basePath + filePath)) {
                //如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) continue;
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            //上传文件
            if (!ftp.storeFile(filename, input)) {
                return result;
            }
            input.close();
            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     * 从FTP服务器下载文件
     * @param remotePath FTP服务器上的相对路径
     * @param fileName 要下载的文件名
     * @param localPath 下载后保存到本地的路径
     * @return
     */
    public boolean downloadFile(String remotePath, String fileName, String localPath) {
        boolean result = false;
        FTPClient ftp = getFTPClient();
        try {
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + "/" + ff.getName());

                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }

            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }





}
