package com.os.upload.controller;

import com.os.result.ResultEntity;
import com.os.upload.service.WordService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/docx")
@RequiredArgsConstructor
public class UploadWordController {

    private final WordService wordService;

    @RequestMapping("/createArticleByWordDoc")
    public ResultEntity createArticleByWordDoc(HttpServletRequest request,
                                               @RequestParam("file") MultipartFile multipartFile){

        // todo kiểm tra định file và content-type.

        // phân tích file
        try{
            XWPFDocument document = new XWPFDocument(multipartFile.getInputStream());

            return wordService.createArticleByWordDoc(document);
        }
        catch (Exception e){
            return ResultEntity.failed().build();
        }
    }
}
