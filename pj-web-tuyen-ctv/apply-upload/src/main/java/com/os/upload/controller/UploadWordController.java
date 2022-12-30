package com.os.upload.controller;

import com.os.entity.Blog;
import com.os.result.ResultEntity;
import com.os.upload.service.WordService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/docx")
@RequiredArgsConstructor
public class UploadWordController {

    private final WordService wordService;

    @PostMapping("/createArticleByWordDoc")
    public ResultEntity<Blog> createArticleByWordDoc(HttpServletRequest request,
                                                     @RequestParam("file") MultipartFile multipartFile) throws IOException {

        // todo kiểm tra định file và content-type.

        // phân tích file
        XWPFDocument document = new XWPFDocument(multipartFile.getInputStream());

        return wordService.createArticleByWordDoc(document);
    }
}
