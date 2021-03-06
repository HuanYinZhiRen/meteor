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
            logger.error("??????ftp???????????????",e);
        }
        return ftp;
    }

    private void closeConnect(FTPClient ftp) {
        if (ftp != null && ftp.isConnected()) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException e) {
                logger.error("??????ftp???????????????",e);
            }
        }
    }


    /**
     * ???FTP?????????????????????
     * @param basePath FTP?????????????????????
     * @param filePath FTP??????????????????????????????????????????????????????/2015/01/01?????????????????????basePath+filePath
     * @param filename ?????????FTP????????????????????????
     * @param input ?????????
     * @return ????????????true???????????????false
     */
    public boolean uploadFile(String basePath, String filePath, String filename, InputStream input) {
        boolean result = false;
        FTPClient ftp = getFTPClient();
        try {
            //?????????????????????
            if (!ftp.changeWorkingDirectory(basePath + filePath)) {
                //?????????????????????????????????
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
            //????????????
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
     * ???FTP?????????????????????
     * @param remotePath FTP???????????????????????????
     * @param fileName ?????????????????????
     * @param localPath ?????????????????????????????????
     * @return
     */
    public boolean downloadFile(String remotePath, String fileName, String localPath) {
        boolean result = false;
        FTPClient ftp = getFTPClient();
        try {
            ftp.changeWorkingDirectory(remotePath);// ?????????FTP???????????????
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
