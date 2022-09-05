package com.os.upload.service;

import com.os.entity.Article;
import com.os.result.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WordService {

    public ResultEntity<Article> createArticleByWordDoc(MultipartFile multipartFile){
        Article article = new Article();
        Map<String, byte[]> impMap = new HashMap<>();

        List<String> lines = this.parseWord(multipartFile);

        return ResultEntity.success().entity(article).build();
    }

    /**
     * Mỗi dòng Word là 1 phần tử trong list.
     * @param multipartFile
     * @return
     */
    private List<String> parseWord(MultipartFile multipartFile){
        List<String> lines = new ArrayList<>();
        try {
            XWPFDocument document = new XWPFDocument(multipartFile.getInputStream());
            List<IBodyElement> list = document.getBodyElements();
            list.stream().forEach(element -> {
                if(element instanceof XWPFParagraph){
                    XWPFParagraph paragraph = (XWPFParagraph) element;

                }
            });
        } catch (IOException e) {
            log.error("=====> parse Word failed: {}", e.getMessage(), e);
        }
        return lines;
    }
}
