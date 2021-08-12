package com.nilin.meteor.web.controller.file;

import com.nilin.meteor.common.util.ThreadUtils;
import com.nilin.meteor.entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/fileManagement")
public class FileManagementController {

    @RequestMapping(value = "upload")
    public Result upload(HttpServletRequest request){
        Result result = new Result();
        try{
            MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = mulRequest.getFileMap();
            MultipartFile file = fileMap.get("file");
            InputStream stream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String name = file.getName();
            long size = file.getSize();
            String ext = getFileExt(originalFilename);
            String filename = ThreadUtils.getRTex().getUUID();
            StringBuilder sb = new StringBuilder(filename);
            String randomName = sb.append(".").append(ext).toString();

            boolean b = ThreadUtils.getFtpUtil().uploadFile("", "/01", randomName, stream);
            result.setData(b);
        }catch (Exception e){
            result.setErrorMsg("上传文件失败");
        }
        return result;
    }

    public String getFileExt(String filePath) {
        String ext = null;
        if (!ThreadUtils.getRTex().isEmpty(filePath)) {
            ext = filePath.substring(filePath.lastIndexOf(".") + 1);
        }
        return ext;
    }
}
