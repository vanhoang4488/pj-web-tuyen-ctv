package com.os.upload.controller;

import com.os.result.ResultEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/docx")
public class UploadWordController {


    @RequestMapping("/createArticleByWordDoc")
    public ResultEntity createArticleByWordDoc(HttpServletRequest request,
                                               @RequestParam("file") MultipartFile multipartFile){

        // todo kiểm tra định file và content-type.

        // phân tích file

        return ResultEntity.success().build();
    }
}
